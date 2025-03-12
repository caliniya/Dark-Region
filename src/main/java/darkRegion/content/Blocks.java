package darkRegion.content;

import darkRegion.world.blocks.environment.*;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.world.Block;

public class Blocks {
    public static Block
    //STA
    spaceStationFloor, spaceStationFloorFixed,
    //core
    highTemperatureCore;


    public static void load() {
        spaceStationFloor = new ConnectFloor("space-station-floor") {{
            blendGroup = mindustry.content.Blocks.empty;
            connects = Seq.with(this);
        }};

        highTemperatureCore = Vars.content.block("深暗之地-高温核心");
    }
}