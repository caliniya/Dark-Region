package darkRegion.content;

import darkRegion.world.blocks.environment.*;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.world.Block;

public class DRBlocks {
    public static Block
    //STA
    spaceStationFloor, spaceStationFloorFixed
    ;


    public static void load() {
        spaceStationFloor = new ConnectFloor("空间站地板") {{
            blendGroup = mindustry.content.Blocks.empty;
            connects = Seq.with(this);
        }};
        spaceStationFloorFixed = new ConnectFloor("空间站地板fixed"){{
            variants = 0;
            blendGroup = Blocks.empty;
            connects = Seq.with(spaceStationFloor.asFloor(), this);
        }};
        ((ConnectFloor)spaceStationFloor.asFloor()).connects.add(spaceStationFloorFixed.asFloor());
    }
}