package darkRegion.entities.ability;

import arc.struct.ObjectMap;
import arc.math.*;
import arc.graphics.g2d.*;
import arc.graphics.*;
import arc.util.*;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.entities.*;


public class shield_suppression extends Ability {
	public float reload = 60f;//冷却时间
    public float radius = 120f;//效果半径
    public float suppressionTime = 60f;//抑制时间
    
    float timer;//计时器
    
    private ObjectMap<Unit, Float> originalShields = new ObjectMap<>();//存储单位及其护盾值
    private ObjectMap<Unit, Float> suppressionEndTimes = new ObjectMap<>();//存储单位抑制结束时间
    
    @Override
    public void update(Unit unit){
        if((timer += Time.delta) > reload) {
            Units.nearbyEnemies(unit.team,unit.x,unit.y,radius,other ->{
            if(other.shield > 0) {
                originalShields.put(other,other.shield);
            	suppression(other);
                suppressionEndTimes.put(other, Time.time + suppressionTime);
            }
        });
        timer = 0f;
    }
        suppressionEndTimes.each((other, endTime) -> {
            if(Time.time >= endTime){
                // 恢复护盾
                other.shield = originalShields.get(other);
                // 移除记录
                originalShields.remove(other);
                suppressionEndTimes.remove(other);
            }
        });
 }
    
    public void suppression(Unit unit){//接收单位然后抑制
        if(unit != null && unit.abilities != null && unit.type != null) {
            if(unit.shield > 0) {
                unit.shield = 0;
            }
        }
    }

}
