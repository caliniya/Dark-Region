package darkRegion.content;

import arc.scene.style.BaseDrawable;
import darkRegion.world.blocks.payload.VelocityPayloadConveyor;
import darkRegion.world.blocks.environment.*;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.world.Block;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawRegion;

public class DRBlocks {
    public static Block
    //STA
    spaceStationFloor, spaceStationFloorFixed,
    
    //载荷轨道
    载荷驾驶轨道,payloadAccelerator
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
        
        /*
        载荷驾驶轨道 = new VelocityPayloadConveyor("bhhjjj"){{
            
            
        }};
        */
        
        payloadAccelerator = new VelocityPayloadConveyor("payload-accelerator"){{
            conductivePower = true;
            consumePower(1f);
            size = 3;
            force = 128f;
            rotate = true;
            acceptsPayload = true;
            outputsPayload = true;
            hasShadow = false;
            drawer = new DrawMulti(
                    new DrawRegion("-shadow"){{
                        buildingRotate = true;
                    }}
                    //new DrawAllRotate()
                    );
        }};
    }
}