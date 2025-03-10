const nodeRoot = TechTree.nodeRoot;
const node = TechTree.node;

const 加利尼亚 = require("加利尼亚");
const {行星对接端口} = require("区块");
const {高温核心} = require("核心");

// public static TechNode nodeRoot(String name, UnlockableContent content, boolean requireUnlock, Runnable children)
加利尼亚.techTree = nodeRoot("加利尼亚", 高温核心, true, () => {
    node(行星对接端口, Seq.with( new Objectives.SectorComplete(SectorPresets.planetaryTerminal), ), () => {
    
    });
    
   
});