package caliniya;

import arc.Events;
import arc.util.Log;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class DarkRegion extends Mod {
    public DarkRegion() {
        //Log.level = Log.LogLevel.debug;
    }

    @Override
    public void loadContent() {
        Events.on(EventType.ClientLoadEvent.class, it -> {
            Log.info("喵喵喵");
        });
    }
}