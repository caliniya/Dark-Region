/** 
    js anuke只会加载main.js(当 scripts/ 只有一个js文件是会当成main.js加载)
    所以这里需要用require(<文件相对路径>(相对scripts/))来加载指定js文件
*/
//require("废弃/区块")
//require("废弃/区块2")
//require ("废弃科技树")
//弃用
require("星球/加利尼亚");
require("星球/核心")
require("重引/液体")//液体
require("特效/液体池效果")//动态效果
require("base/碎甲弹")
require("base/跳跃弹")
MapResizeDialog.minSize = 0
MapResizeDialog.maxSize = 500000

//require("炮塔/修复激光");