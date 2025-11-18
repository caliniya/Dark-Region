package darkRegion.content;

import arc.graphics.Color;
import darkRegion.entities.ability.*;
import darkRegion.type.ai.FreightAI;
import mindustry.content.Fx;
import mindustry.type.UnitType;
import mindustry.ai.types.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.entities.bullet.*;
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
    
    public static UnitType 
    小型工厂货运无人机, aa
    ;
    public static void load(){
        小型工厂货运无人机 = new UnitType("小型工厂货运无人机"){{
            constructor = UnitEntity::create;    
            flying = true;
            boostMultiplier = 5f;
            speed = 5f;
            hitSize = 8f;
            health = 2000f;
            buildSpeed = 2f;
            armor = 5f;
            
            abilities.add(new ShieldSuppression());
            ammoType = new PowerAmmoType(1000);
            
            controller = u -> new FreightAI();
            playerControllable = false;

            weapons.add(new Weapon("heal-weapon"){{
                top = false;
                shootY = 2f;
                reload = 24f;
                x = 4.5f;
                alternate = false;
                ejectEffect = Fx.none;
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
        aa = new UnitType("aa"){{
            constructor = UnitEntity::create;    
            flying = true;
            boostMultiplier = 5f;
            speed = 5f;
            hitSize = 8f;
            health = 2000f;
            buildSpeed = 2f;
            armor = 5f;
            
            abilities.add(new StatusFieldAbility(DRStatusEffects.压制 , 10 , 5 ,200));
            ammoType = new PowerAmmoType(1000);

            weapons.add(new Weapon("heal-weapon"){{
                top = false;
                shootY = 2f;
                reload = 24f;
                x = 4.5f;
                alternate = false;
                ejectEffect = Fx.none;
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