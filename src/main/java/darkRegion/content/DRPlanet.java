package darkRegion.content;

import darkRegion.content.*;
import darkRegion.world.meta.DREnv;
import darkRegion.space.SpaceStationGenerator;
import darkRegion.world.meta.DREnv;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.Vars;
import mindustry.content.Planets;

public class DRPlanet {
	public static Planet
        SpaceStationGalinia;
    
    public static void load() {
        SpaceStationGalinia = new Planet("加利尼亚空间站",JsonLoad.加利尼亚,0.01f){{
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