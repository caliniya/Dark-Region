package darkRegion.content;

import darkRegion.entities.ability.shield_suppression;
import mindustry.type.UnitType;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.effect.*;
import mindustry.entities.part.*;
import mindustry.entities.pattern.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.type.ammo.*;
import mindustry.type.unit.*;
import mindustry.type.weapons.*;
import mindustry.world.meta.*;
import darkRegion.entities.ability.*;

import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.*; 

public class DRUnits {
    
    public static UnitType a1;
    
    public static void load(){
        a1 = new UnitType("aa"){{
            constructor = UnitEntity::create;    
            canBoost = true;
            boostMultiplier = 1.5f;
            speed = 0.55f;
            hitSize = 8f;
            health = 120f;
            buildSpeed = 0.8f;
            armor = 1f;
            abilities.add(new shield_suppression());
            ammoType = new PowerAmmoType(1000);

            weapons.add(new Weapon("heal-weapon"){{
                top = false;
                shootY = 2f;
                reload = 24f;
                x = 4.5f;
                alternate = false;
                //ejectEffect = Fx.none;
                recoil = 2f;
                shootSound = Sounds.lasershoot;

                bullet = new LaserBoltBulletType(5.2f, 13){{
                    lifetime = 30f;
                    healPercent = 5f;
                    collidesTeam = true;
                    backColor = Pal.heal;
                    frontColor = Color.white;
                }};
            }});
        }};
    }
	
}