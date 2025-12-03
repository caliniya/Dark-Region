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
    
    /**
     * 构造函数
     * @param name 状态效果的名称
     */
    public DRStatusEffect(String name){
        super(name);
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
        return super.reactsWith(effect) || immunities.contains(effect);
    }
    
    /**
     * 设置状态显示信息
     * 以在游戏界面显示信息
     */
    @Override
    public void setStats(){
        super.setStats();
        
        // 如果有免疫效果，添加到状态显示中
        if(immunities.size > 0){
            for(var immuneEffect : immunities.toSeq().sort()){
                // 使用Stat.immunities统计类别显示免疫信息(就是单位的免疫)
                stats.add(Stat.immunities, immuneEffect.emoji() + immuneEffect);
            }
        }
        if(isShieldS) {
        	stats.add(DRStat.护盾抑制 ,true);
        }
    }
    
    /**
     * 每帧更新方法
     * 
     * @param unit 受效果影响的单位
     * @param statusEntry 状态条目
     ?
     */
    @Override
    public void update(Unit unit, StatusEntry statusEntry) {
        // 调用父类更新逻辑
        super.update(unit , statusEntry);
    }
    
    /**
     * 效果应用时的处理
     * 如果启用了护盾抑制，临时移除单位的护盾相关能力
     * 
     * @param unit 受效果影响的单位
     * @param time 效果持续时间
     * @param extend 是否延长现有效果或应用新效果?
     */
    @Override
    public void applied(Unit unit, float time, boolean extend) {
        // 调用父类应用逻辑
        super.applied(unit, time, extend);
        
        if(extend && unit.abilities != null && isShieldS) {
            
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
        
        if(isShieldS) {
            // 恢复单位的原始能力数组
            unit.abilities = unit.type.abilities.toArray(Ability.class);
        }
        
    }
}