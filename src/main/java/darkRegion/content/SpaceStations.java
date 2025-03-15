package darkRegion.content;

import darkRegion.world.meta.DREnv;
import darkRegion.space.SpaceStationGenerator;
import darkRegion.world.meta.DREnv;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.Vars;
import mindustry.content.Planets;

public class SpaceStations {
    public static Planet
            SpaceStationGalinia;
    
    public static void load() {
        SpaceStationGalinia = new Planet("space-station-galinia",Vars.content.planet("深暗之地-加利尼亚"),0.05f){{
            bloom = true;
            
            generator = new SpaceStationGenerator();
            mesh = new HexMesh(this, 0);
            sectors.add(new Sector(this, PlanetGrid.Ptile.empty));
            hasAtmosphere = false;
            updateLighting = false;
            drawOrbit = true;
            accessible = true;
            clipRadius = 4;
            orbitRadius = parent.radius + 1f;
            alwaysUnlocked = true;
            defaultEnv = Env.space | DREnv.omurlo | DREnv.twin;
            allowWaves = false;
            defaultCore = Vars.content.block("深暗之地-高温核心");
        }};
    }
}