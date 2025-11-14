package darkRegion;

import darkRegion.content.*;
import darkRegion.kt.content.Blocks;
import darkRegion.kt.world.production.MultipleRecipeCrafter;
import mindustry.mod.Mod;

public class DarkRegion extends Mod {
    
    public DarkRegion() {
        //Log.level = Log.LogLevel.debug;
    }

    @Override
    public void loadContent() {
        DRLoadouts.load();
        JsonLoad.load();
        DRBlocks.load();
        DRUnits.load();
        DRItems.load();
        DRPlanet.load();
        DRStatusEffects.load();
    }
}