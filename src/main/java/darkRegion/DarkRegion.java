package darkRegion;

import darkRegion.content.*;
import darkRegion.entities.ability.shield_suppression;

import arc.util.Log;
import mindustry.mod.Mod;



public class DarkRegion extends Mod {
    public DarkRegion() {
        //Log.level = Log.LogLevel.debug;
    }

    @Override
    public void loadContent() {
        Log.debug("Loading Blocks...");
        DRBlocks.load();
        Log.debug("Loading SpaceStations...");
        SpaceStations.load();
        DRUnits.load();
    }
}