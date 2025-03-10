//require("效果/压制")

const powerProduction = 300; // 电量产生速率

const 高温核心 = Object.assign(extend(CoreBlock, "高温核心", {
    // 核心基础统计信息
    setStats() {
        this.super$setStats();
        this.stats.add(Stat.basePowerGeneration, 60 * powerProduction, StatUnit.powerSecond);
    },
    // 设置核心的可放置和可破坏规则
    canBreak(tile) {
        // 如果只有一个核心，则不能破坏
        // return Vars.state.teams.get(tile.team()).getCount(this) > 1;
        return true; //必须全部抄掉
    },
  
    // 电力相关属性
    hasPower: true,
    consumesPower: false,
    outputsPower: true
}), {
    buildType() {
        return extend(CoreBlock.CoreBuild, 高温核心, {
            getPowerProduction() {
                return powerProduction;
            }
        });
    }
});

// 定义updateTile方法，它将在每一帧被调用
//高温核心.updateTile = function(tile) {
//    this.super$updateTile(tile);
    
    // 设置效果的半径
//    const effectRadius = 100;
    
    // 遍历半径内的所有单位
//    Units.nearby(tile.team, tile.x, tile.y, effectRadius, unit => {
        // 应用"压制"效果
//        unit.apply(statusEffects.压制, statusEffects.压制.duration);
 //   });
//};

exports.高温核心 = 高温核心;