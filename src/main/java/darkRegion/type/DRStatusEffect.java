package darkRegion.type;

import arc.struct.*;
import darkRegion.world.meta.DRStat;
import mindustry.entities.abilities.*;
import mindustry.entities.units.StatusEntry;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.meta.Stat;
import mindustry.world.meta.Stats;

public class DRStatusEffect extends StatusEffect {
    
    /** 免疫的效果集合 - 存储该效果免疫的其他状态效果 */
    public ObjectSet<StatusEffect> immunities = new ObjectSet<>();
    
    /** 护盾抑制标志 - 为true时抑制单位的护盾 */
    public boolean isShieldS = false;
    
    /** 能力备份序列 - 存储单位的原始能力，用于在效果移除后恢复 */
    public Seq<Ability> abs;
    
    /**
     * 构造函数
     * @param name 状态效果的名称
     */
    public DRStatusEffect(String name){
        super(name);
        // 初始化能力备份序列
        abs = new Seq<Ability>();
    }

    /**
     * 设置免疫关系
     * 使该效果提供对指定效果的免疫，形成单向豁免关系
     * 被免疫的效果在应用到单位时会被完全阻止
     * 
     * @param effects 要免疫的状态效果数组
     */
    public void immuneTo(StatusEffect... effects){
        for(StatusEffect effect : effects){
            // 添加到免疫集合
            immunities.add(effect);
            // 设置转换处理器来阻止效果应用
            trans(effect, (unit, result, time) -> {
                // 完全阻止被免疫效果的应用
                // 通过不修改result来阻止效果应用
                // 此处的lambda表达式返回void，result保持原状，从而阻止转换
            });
        }
    }

    /**
     * 重写反应检查方法
     * 扩展原版方法，包含免疫关系的检查
     * 
     * @param effect 要检查的状态效果
     * @return 如果与指定效果有反应或免疫该效果，返回true
     */
    @Override
    public boolean reactsWith(StatusEffect effect){
        // 调用父类方法检查标准反应，或检查是否免疫该效果
        return super.reactsWith(effect) || immunities.contains(effect);
    }
    
    /**
     * 设置状态显示信息
     * 重写以在游戏界面显示额外的免疫信息
     */
    @Override
    public void setStats(){
        super.setStats();
        
        // 如果有免疫效果，添加到状态显示中
        if(immunities.size > 0){
            // 对免疫效果排序以确保显示一致性
            for(var immuneEffect : immunities.toSeq().sort()){
                // 使用Stat.immunities统计类别显示免疫信息
                stats.add(Stat.immunities, immuneEffect.emoji() + immuneEffect);
            }
        }
    }
    
    /**
     * 每帧更新方法
     * 处理护盾抑制逻辑：如果启用了护盾抑制且单位有护盾，将护盾值清零
     * 
     * @param unit 受效果影响的单位
     * @param statusEntry 状态条目
     */
    @Override
    public void update(Unit unit, StatusEntry statusEntry) {
        // 调用父类更新逻辑
        super.update(unit , statusEntry);
        
        // 检查是否需要抑制护盾
        if(isShieldS == true && unit.shield > 0) {
            // 将单位的护盾值设为0
            unit.shield = 0;
        }
    }
    
    /**
     * 效果应用时的处理
     * 如果启用了护盾抑制，临时移除单位的护盾相关能力
     * 
     * @param unit 受效果影响的单位
     * @param time 效果持续时间
     * @param extend 是否延长现有效果（true）或应用新效果（false）
     */
    @Override
    public void applied(Unit unit, float time, boolean extend) {
        // 调用父类应用逻辑
        super.applied(unit, time, extend);
        
        // 仅在新应用效果时处理能力移除（非延长）
        if(!extend && unit.abilities != null && isShieldS) {
            // 备份单位的原始能力
            abs.addAll(unit.abilities);
            
            // 创建临时序列，移除护盾相关能力
            Seq<Ability> seq = new Seq<>(unit.abilities);
            seq.removeAll(a -> a instanceof ForceFieldAbility || a instanceof ShieldArcAbility);
            
            // 更新单位的能力数组
            unit.abilities = seq.toArray(Ability.class);
            // 注意：此时单位的护盾能力已被临时移除
        }
    }
    
    /**
     * 效果移除时的处理
     * 恢复单位原始的护盾相关能力
     * 
     * @param unit 受效果影响的单位
     */
    @Override
    public void onRemoved(Unit unit) {
        // 调用父类移除逻辑
        super.onRemoved(unit);
        
        // 如果有备份的能力且启用了护盾抑制
        if(abs.any() && isShieldS) {
            // 恢复单位的原始能力数组
            unit.abilities = abs.toArray();
            // 注意：abs序列在下次效果应用时会被重新赋值
        }
    }
}