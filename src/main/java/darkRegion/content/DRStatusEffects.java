package darkRegion.content;

import darkRegion.type.DRStatusEffect;
import mindustry.content.StatusEffects;
import mindustry.type.StatusEffect;

public class DRStatusEffects {
    
    public static StatusEffect 
    压制
    ;
    
    public static void load(){
        压制 = new DRStatusEffect("压制"){{
            immuneTo(StatusEffects.boss);
            init(()->{
                
            });
        }};
    }
    
}