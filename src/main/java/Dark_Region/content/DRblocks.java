package Dark_Region.content;

import Dark_Region.world.blocks.environment.*;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.world.Block;

public class DRblocks {
    public static Block
        //STA
        spaceStationFloor,spaceStationFloorFixed,
        //core
        hightemperaturecore;
    
    
    
    
    public static void load(){
        
        spaceStationFloor = new ConnectFloor("space-station-floor"){{
            variants = 0;
            blendGroup = Blocks.empty;
            connects = Seq.with(this);
        }};
        
        hightemperaturecore = Vars.content.block("深暗之地-高温核心");
        
    };
	
}