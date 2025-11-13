package darkRegion.content;

import mindustry.game.Schematic;
import mindustry.game.Schematics;

public class DRLoadouts {
    public static Schematic
        basicSpaceStations,
        高温核心蓝图
    ;

    public static void load() {
        basicSpaceStations = Schematics.readBase64("bXNjaAF4nGNgZmBmYWDJS8xNZeB5uXrGsx0rny3Y8XR/MwN7cUlqYm5mCgNXcXJGam5iSWZyMQN3SmpxclFmQUlmfh4DAwNbTmJSak4xA1N0LCOD5LPtG5/Nmv5kZ/fTORt0UcxiYGAEISABAGCuK3I=");
        高温核心蓝图 = Schematics.readBase64("bXNjaAF4nGNgYWABorzE3FQGRkMGruT8vJLUvBLfxAIGpupaBu6U1OLkosyCksz8PAYGBracxKTUnGIGpuhYRgbJZ9s3Pps1/cnO7qdzNui+XD3j2Y6VzxbseLq/GaiSEYSABAB0uiDD");
    }
}