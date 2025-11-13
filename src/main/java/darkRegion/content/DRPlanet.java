package darkRegion.content;

import darkRegion.content.*;
import darkRegion.map.*;
import darkRegion.world.meta.DREnv;
import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.Planets;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.graphics.g3d.PlanetGrid.*;
import mindustry.maps.planet.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class DRPlanet {
	public static Planet
        加利尼亚空间站,
        加利尼亚
    ;
    
    public static void load() {
        加利尼亚 = new Planet("加利尼亚" ,Planets.sun ,1f ,4){{
            generator = new GaliniaGenerator();
            alwaysUnlocked = true;
            meshLoader = () -> new HexMesh(this, 5);
            prebuildBase = true;
            defaultCore = JsonLoad.高温核心;
            cloudMeshLoader = () -> new MultiMesh(
                new HexSkyMesh(this, 2, 0.15f, 0.14f, 5, Color.valueOf("eba768").a(0.75f), 2, 0.42f, 1f, 0.43f),
                new HexSkyMesh(this, 3, 0.6f, 0.15f, 5, Color.valueOf("eea293").a(0.75f), 2, 0.42f, 1.2f, 0.45f)
            );
            defaultAttributes.set(Attribute.heat, 0.8f);
            ruleSetter = r -> {
                r.waveTeam = Team.malis;
                r.placeRangeCheck = false;
                r.showSpawns = true;
                r.fog = true;
                r.staticFog = true;
                r.lighting = false;
                r.coreDestroyClear = true;
                r.onlyDepositCore = true;
            };
            campaignRuleDefaults.fog = true;
            campaignRuleDefaults.showSpawns = true;
            campaignRuleDefaults.rtsAI = true;
        }};
        
        加利尼亚空间站 = new Planet("加利尼亚空间站",加利尼亚,0.01f){{
            bloom = false;//禁用光晕
            generator = new SpaceStationGenerator();
            mesh = new HexMesh(this, 0);
            sectors.add(new Sector(this, PlanetGrid.Ptile.empty));
            hasAtmosphere = false;
            updateLighting = true;
            drawOrbit = true;
            accessible = true;
            clipRadius = 4;
            camRadius = 2;
            launchCapacityMultiplier = 1;
            prebuildBase = true;
            orbitRadius = parent.radius + 0.5f;
            orbitTime = 6;
            alwaysUnlocked = true;
            defaultEnv = Env.space | DREnv.omurlo | DREnv.twin;
            allowWaves = false;
            defaultCore = JsonLoad.高温核心;
        }};
    }
}