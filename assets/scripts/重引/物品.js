let modName = modName; // 保存global的modName
let init = false;

let seq = new Seq();

// js执行时机比json创建内容早 需要等待内容初始化
let parser = Reflect.get(Vars.mods, "parser");
let postreads = Reflect.get(parser, "postreads");
let 压制 = Reflect.get(Vars.mods, "压制");
/** 比方说我json里有个 "铅.json"
* 我想在引用这个物品时叫他 "lead"
* 那么我就这样写: findAndExportItem("lead", "铅");
*    const myItems = require("MyItems");
*    myItems.lead即为我创建的 "铅"
*/
postreads.add(run(() => {
    findAndExportItem("铱", "铱");
    
    init = true;
    
    seq.each(func => func());
    seq.clear();
}));

// 初始化后执行
/** 在用到这些json物品时 务必等待初始化
* 还是比如我的 "铅.json"
" 我想在某个方块建造消耗它 那么需要这样写
* const myItems = require("MyItems");
* myItems.afterInit(() => {
*     block.requirements(ItemStack.with(myItems.lead, 150));
* });
*/
exports.afterInit = function(func){
    if(init){
        func();
    }else{
        seq.add(func);
    }
}

// 通过item的name 寻找json中创建的物品
function findMyItem(itemName){
    return Vars.content.item(modName + "-" + itemName);
}

// 寻找并导出模块
function findAndExportItem(exportName, itemName){
    exports[exportName] = findMyItem(itemName);
}