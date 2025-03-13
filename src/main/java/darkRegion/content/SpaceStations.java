package darkRegion.content;

import darkRegion.space.SpaceStationGenerator;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.Vars;
import mindustry.content.Planets;

public class SpaceStations {
    public static Planet
            SpaceStationGalinia;
    
    public static void load() {
        SpaceStationGalinia = new Planet("space-station-galinia",Planets.sun,0.05f){{
            bloom = true;
            
            generator = new SpaceStationGenerator();
            mesh = new HexMesh(this, 0);
            sectors.add(new Sector(this, PlanetGrid.Ptile.empty));
            hasAtmosphere = false;
            updateLighting = false;
            drawOrbit = true;
            accessible = true;
            clipRadius = 2;
            orbitRadius = parent.radius + 1f;
            alwaysUnlocked = true;
            defaultEnv = Env.space;
            allowWaves = false;
            defaultCore = Vars.content.block("深暗之地-高温核心");
        }};
    }
}