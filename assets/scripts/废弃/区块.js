const lib = require("base/lib");
const 加利尼亚 = require("加利尼亚");

// 创建主线区块
/**
    看源码SectorPreset.java:
        public SectorPreset(String name, Planet planet, int sector)
    可以知道SectorPreset的一个构造函数包含以下参数:
        name: 主线区块的name;
            这里的name决定主线区块地图的文件名字
            地图文件见<maps/>
        planet: 所在星球;
        sector: 所在区块id(这个的查看可以用我的mod<显示星球区块id>);
*/
const 行星对接端口 = new SectorPreset("行星对接端口", 加利尼亚, 1);
行星对接端口.description = "从此区块开始进攻次行星，周围未发现敌人基地进攻";
行星对接端口.difficulty = 2;
行星对接端口.alwaysUnlocked = false;
行星对接端口.addStartingItems = true;
行星对接端口.captureWave = 1;
行星对接端口.localizedName = "行星对接端口";
exports.行星对接端口 = 行星对接端口;
lib.addToResearch(行星对接端口, {
	parent: "高温核心",
	objectives: Seq.with(
		new Objectives.SectorComplete(SectorPresets.planetaryTerminal))
});




function exportPreset(name, sectorPreset){
    module.exports[name] = sectorPreset;
}