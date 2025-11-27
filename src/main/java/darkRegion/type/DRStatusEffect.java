package darkRegion.type;

import arc.struct.*;
import darkRegion.world.meta.DRStat;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.meta.Stat;
import mindustry.world.meta.Stats;

public class DRStatusEffect extends StatusEffect {
    
    public ObjectSet<StatusEffect> immunities = new ObjectSet<>();
    
    public DRStatusEffect(String name){
        super(name);
    }

    /**
     使用此方法创建免疫
     使该效果提供对指定效果的免疫。这形成了一种单向豁免关系。
     */
    public void immuneTo(StatusEffect... effects){
        for(StatusEffect effect : effects){
            immunities.add(effect);
            // 设置转换处理器来阻止效果应用
            trans(effect, (unit, result, time) -> {
                // 完全阻止被免疫效果的应用
                // 通过不修改result来阻止效果应用
            });
        }
    }

    /**
     * 重写reactsWith来包含免疫关系
     */
    @Override
    public boolean reactsWith(StatusEffect effect){
        return super.reactsWith(effect) || immunities.contains(effect);
    }
    
    @Override
    public void setStats(){
        super.setStats();
        
        // 在状态显示中添加免疫信息
        if(immunities.size > 0){
            for(var immuneEffect : immunities.toSeq().sort()){
                stats.add(Stat.immunities, immuneEffect.emoji() + immuneEffect);
            }
        }
    }
}