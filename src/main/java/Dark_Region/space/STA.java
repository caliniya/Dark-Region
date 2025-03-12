package Dark_Region.space;

import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.graphics.g3d.PlanetGrid.*;
import mindustry.maps.planet.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import mindustry.Vars;

import Dark_Region.space.STAGenerator;

public class STA {
    public static Planet
    STA_GA;
    
    public static void load() {
        
        STA_GA = new Planet("STA-GA",Vars.content.planet("深暗之地-加利尼亚"),0.001f){{
            bloom = true;
            
            generator = new STAGenerator();
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