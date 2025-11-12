package darkRegion;

import darkRegion.content.*;

import arc.util.Log;
import mindustry.mod.Mod;



public class DarkRegion extends Mod {
    public DarkRegion() {
        
    }

    @Override
    public void loadContent() {
        JsonLoad.load();
        DRBlocks.load();
        DRUnits.load();
        DRItems.load();
        JsonLoad.load();
        DRPlanet.load();
    }
}