package darkRegion.content;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.units.UnitAssembler.*;

import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.*;

public class DRFX {
    //@SuppressWarnings("")
	public static final Effect
    
    none = new Effect(0, 0f, e -> {}),
    
    aaa = new Effect(10 , 10 ,date -> {
        
    }),
    
    unitJumpTrail = new Effect(20f, 40f, e -> {
        float fin = e.fin();
        
        Draw.color(Color.valueOf("a3d4ff"), Pal.accent, fin);
        Draw.alpha(0.6f * (1f - fin));
        
        // 拖尾光效
        Lines.stroke(4f * (1f - fin));
        Lines.line(e.x, e.y, e.x, e.y);
        
        // 轨迹上的粒子
        for(int i = 0; i < 3; i++){
            float progress = Mathf.randomSeedRange(e.id + i, 0.3f) + 0.5f;
            float x = Mathf.lerp(e.x, e.x,progress);
            float y = Mathf.lerp(e.y, e.y,progress);
            
            Fill.circle(x, y, 1.5f * (1f - fin));
        }
        
        Draw.reset();
    }),
    
    unitJumpComplete = new Effect(60f, 80f, e -> {
        float fin = e.fin();
        
        if(fin < 0.5f){
            // 前半段：传送开始效果
            float progress = fin / 0.5f;
            float radius = 25f * Interp.pow2Out.apply(progress);
            
            Draw.color(Pal.accent, Pal.accent, progress);
            Draw.alpha(0.8f * (1f - progress));
            
            Lines.stroke(4f * (1f - progress));
            Lines.circle(e.x, e.y, radius);
            
            // 旋转能量环
            for(int i = 0; i < 3; i++){
                float angle = e.rotation + i * 120f + progress * 1080f;
                float len = radius * 0.7f;
                float x = e.x + Angles.trnsx(angle, len);
                float y = e.y + Angles.trnsy(angle, len);
                
                Fill.circle(x, y, 2f * (1f - progress));
            }
        }else{
            // 后半段：传送结束效果  
            float progress = (fin - 0.5f) / 0.5f;
            float radius = 25f * Interp.bounceOut.apply(progress);
            
            Draw.color(Pal.accent, Pal.accent, progress);
            Draw.alpha(0.8f * progress);
            
            Fill.circle(e.x, e.y, radius * 0.6f);
            
            Lines.stroke(3f * progress);
            Lines.circle(e.x, e.y, radius);
            
            // 冲击波
            if(progress > 0.3f){
                float waveProgress = (progress - 0.3f) / 0.7f;
                float waveRadius = radius * (1f + waveProgress * 2f);
                Lines.stroke(2f * (1f - waveProgress));
                Lines.circle(e.x, e.y, waveRadius);
            }
        }
        
        Draw.reset();
    })
    
    ;
    
}