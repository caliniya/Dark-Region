package darkRegion.content;

import mindustry.Vars;
import mindustry.type.*;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;

//此文件负责将所有json内容引入java
//除非确信某一个内容用不上，否则保留

public class JsonLoad {
    
    public static Block
    高温核心,
    低温液池
    ;
    
    public static Item 
    锗,铱,银,锗原矿
    ;
    
    public static Liquid 
    低温液
    ;
    
    public static Planet
    加利尼亚
    ;
    
    public static void load(){
        高温核心 = Vars.content.block("深暗之地-高温核心");
        锗 = Vars.content.item("深暗之地-锗");
        铱 = Vars.content.item("深暗之地-铱");
        银 = Vars.content.item("深暗之地-银");
        锗原矿 = Vars.content.item("深暗之地-锗原矿");
        
        低温液池 = Vars.content.block("深暗之地-低温液池");
        
        低温液 = Vars.content.liquid("深暗之地-低温液");
        
        加利尼亚 = Vars.content.planet("深暗之地-加利尼亚");
        
    }
}