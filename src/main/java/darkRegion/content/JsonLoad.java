package darkRegion.content;

import mindustry.Vars;
import mindustry.type.Item;
import mindustry.world.Block;

//此文件负责将所有json内容引入java

public class JsonLoad {
    
    public static Block
    高温核心
    ;
    
    public static Item 
    锗
    ;
    
    public static void load(){
        高温核心 = Vars.content.block("深暗之地-高温核心");
        锗 = Vars.content.item("深暗之地-锗");
    }
}