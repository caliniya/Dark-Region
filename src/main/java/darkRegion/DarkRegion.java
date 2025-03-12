package darkRegion;

import darkRegion.content.Blocks;
import darkRegion.content.SpaceStations;
import arc.util.Log;
import mindustry.mod.Mod;

public class DarkRegion extends Mod {
    public DarkRegion() {
        //Log.level = Log.LogLevel.debug;
    }

    @Override
    public void loadContent() {
        Log.debug("Loading Blocks...");
        Blocks.load();
        Log.debug("Loading SpaceStations...");
        SpaceStations.load();
    }
}