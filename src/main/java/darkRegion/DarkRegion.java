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
        JsonLoad.load();//此内容第一个加载以防止同名错误
        DRStatusEffects.load();
        DRLoadouts.load();
        DRPlanet.load();
        DRBlocks.load();
        DRUnits.load();
        DRItems.load();
    }
}