package darkRegion.content;

import darkRegion.world.blocks.environment.*;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.world.Block;

public class DRBlocks {
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
        spaceStationFloorFixed = new ConnectFloor("space-station-floor-fixed"){{
            variants = 0;
            blendGroup = Blocks.empty;
            connects = Seq.with(spaceStationFloor.asFloor(), this);
        }};
        ((ConnectFloor)spaceStationFloor.asFloor()).connects.add(spaceStationFloorFixed.asFloor());

        highTemperatureCore = Vars.content.block("深暗之地-高温核心");
    }
}