package darkRegion.game;

import arc.math.geom.*;
import arc.util.*;
import mindustry.core.GameState.*;
import mindustry.ctype.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.net.*;
import mindustry.net.Packets.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.storage.CoreBlock.*;

public class DREvent {
	
    public static class STAlaunchEvent {
        
        public final Sector sector, from;
        public final Schematic loadout;
        
        public STAlaunchEvent(Sector sector, Sector from, Schematic loadout){
            this.sector = sector;
            this.from = from;
            this.loadout = loadout;
        }
    }
    
    public static class LaunchItemToSTAEvent{
        public final ItemStack stack;

        public LaunchItemToSTAEvent(ItemStack stack){
            this.stack = stack;
        }
    }
    
}