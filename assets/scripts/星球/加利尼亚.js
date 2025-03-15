// 导入所需的基础库和核心模块
const {
  高温核心
} = require("星球/核心");


//颜色
const colorSrc = Color.valueOf("B8B8B8FF");
const colorDst = Color.valueOf("EAEAEAFF");
const baseColor = Color.white.cpy();
const iceBlueColor = Color.valueOf("4ABAFF");
const blackSandColor = Color.valueOf("777777");
const polarIceBlueColor = Color.valueOf("80C8E0");

/**
* 创建一个名为"加利尼亚"的星球模组
* @param {String} name - 星球名称
* @param {Planet} parent - 母星对象 (可为null)
* @param {float} radius - 星球半径（单位: 个赛普罗）
* @param {int} sectorSize - 区块大小
*/
var 加利尼亚 = extend(Planet, "加利尼亚", Planets.sun, 1, 4, {

  // 区块生成器
  generator: extend(SerpuloPlanetGenerator, {
    // 默认核心蓝图
    defaultLoadout: Schematics.readBase64("bXNjaAF4nGNgZmBmYWDJS8xNZeB5uXrGsx0rny3Y8XR/MwN7cUlqYm5mCgNXcXJGam5iSWZyMQN3SmpxclFmQUlmfh4DAwNbTmJSak4xA1N0LCOD5LPtG5/Nmv5kZ/fTORt0UcxiYGAEISABAGCuK3I="),

    /**
    * 生成区块
    * @param {Sector} sector - 区块对象
    * @param {int} x - 区块 x 坐标
    * @param {int} y - 区块 y 坐标
    */
    /**
    * 获取区块位置的颜色
    * @param {Vec3} position - 区块位置
    * @returns {Color} - 返回区块位置的颜色
    */
    getColor(position) {
      var vec = position.sub(加利尼亚.position);
      var height = vec.len() - 加利尼亚.radius;
      var latitude = Math.abs(vec.angle(Vec3.Y) - 90);

      // 如果海拔高度较高，则返回基础颜色
      if (height > 1.03) return baseColor;

      // 根据区块位置随机替换为冰蓝色
      if (latitude > 10)
        if (Mathf.chance(0.3)) return iceBlueColor;

      // 在赤道地区添加黑沙的颜色
      if (latitude < 13 && Mathf.chance(0.1)) return blackSandColor;
      if (latitude < 10 && Mathf.chance(0.2)) return blackSandColor;
      if (latitude < 7 && Mathf.chance(0.4)) return blackSandColor;
      if (latitude < 3 && Mathf.chance(0.7)) return blackSandColor;

      // 在两极地区，大约纬度70以上的地区，添加明显的冰蓝色
      if (latitude > 70) return polarIceBlueColor;

      // 使用基础颜色
      return baseColor
    },


  }),

  // 星球属性配置
  launchCapacityMultiplier: 1, // 发射时核心携带资源倍率
  sectorSeed: 15, // 区块生成种子
  allowWaves: true, // 允许生成区块波次
  allowWaveSimulation: true, // 允许后台自动挂波次
  allowSectorInvasion: true, // 允许区块被敌人进攻
  allowLaunchSchematics: false, // 允许使用核心蓝图
  enemyBuildSpeedMultiplier: 1, // 敌人建筑倍率
  enemyCoreSpawnReplace: true, // 敌人最后的核心被摧毁后是否要生成一个出怪点
  allowLaunchLoadout: true, // 允许发射时携带物资
  clearSectorOnLose: false, // 区块输了是否重置区块
  tidalLock: false, // 是否潮汐锁定
  prebuildBase: true, // 是否需要着陆建筑特效
  orbitRadius : 20,//轨道半径

  // 区块地图加载时的规则设置
  ruleSetter: r => {
    r.waveTeam = Team.crux; // 区块的敌对队伍
    r.placeRangeCheck = false; // 建筑范围检查
    r.showSpawns = true; // 显示出怪点
    r.fog = true; // 显示迷雾
  },

  // 星球外观配置
  iconColor: Color.valueOf("7F7F7FFF"), // 略带灰色的图标颜色
  atmosphereColor: Color.valueOf("19192500"), // 透明的蓝色大气层
  atmosphereRadIn: 0.04, // 内部辐射
  atmosphereRadOut: 0.3, // 外部辐射
  updateLighting: true, // 启用昼夜交替的光照效果
  lightSrcFrom: -0.1, // 光照起始位置
  lightSrcTo: 0.2, // 光照终止位置
  lightDstFrom: 0.8, // 光照距离起始位置
  lightDstTo: 1, // 光照距离终止位置

  startSector: 99, // 开始区块
  alwaysUnlocked: true, // 是否默认解锁
  landCloudColor: Object.assign(Pal.spore.cpy(),
    {
      a: 0.5
    }), // 着陆时的云烟颜色
  defaultCore: 高温核心, // 默认核心

  /**
  * 初始化星球
  */
  init() {
    this.super$init(); // 调用父类的初始化方法

    // 渲染星球表面地形的MeshLoader
    this.meshLoader = () => new HexMesh(this,
      7);

    // 渲染星球云层的MeshLoader
    this.cloudMeshLoader = () => new MultiMesh(
      new HexSkyMesh(this, 5, 0.3, 0.17, 8, Object.assign(new Color().set(colorSrc).mul(0.9), {
        a: 0.6
      }), 9, 0.1, 0.9, 0.38),
      new HexSkyMesh(this, 15, 0.6, 0.16, 9, Object.assign(new Color().set(colorSrc), {
        a: 0.9
      }), 6, 0, 1, 0.28),
      new HexSkyMesh(this, 10, 0.6, 0.16, 2, Object.assign(Color.white.cpy().lerp(colorDst, 0.9), {
        a: 0.6
      }), 8, 0.1, 1, 0.28),
      new HexSkyMesh(this, 15, 0.6, 0.15, 7, Object.assign(Color.white.cpy().lerp(colorDst, 0.9), {
        a: 0.6
      }), 3, 0.1, 1, 0.28),
      new HexSkyMesh(this, 15, 0.6, 0.14, 3, Object.assign(new Color().set(colorSrc), {
        a: 0.9
      }), 4, 0.1, 1, 0.28),
      new HexSkyMesh(this, 15, 0.8, 0.14, 5, Object.assign(new Color().set(colorSrc), {
        a: 0.9
      }), 8, 0.1, 1, 0.28),
      new HexSkyMesh(this, 15, 0.4, 0.15, 5, Object.assign(new Color().set(colorSrc), {
        a: 0.9
      }), 6, 0, 1, 0.21),
      new HexSkyMesh(this, 10, 0.6, 0.16, 5, Object.assign(new Color().set(colorSrc), {
        a: 0.8
      }), 4, 0, 1, 0.28),
      new HexSkyMesh(this, 25, 0.6, 0.15, 4, Object.assign(new Color().set(colorSrc), {
        a: 0.9
      }), 6, 0, 1, 0.28),
      new HexSkyMesh(this, 20, 0.6, 0.14, 5, Object.assign(new Color().set(colorSrc), {
        a: 0.8
      }), 8, 0, 1, 0.28),
    );

    // 隐藏在星球上的物品
    this.hiddenItems.addAll(
      Items.lead,
      Items.copper,
      Items.titanium,
      Items.phaseFabric,
      Items.pyratite,
      Items.blastCompound,
      Items.beryllium,
      Items.tungsten,
      Items.oxide,
      Items.carbide,
      Items.metaglass,
      Items.graphite,
      Items.titanium,
      Items.thorium,
      Items.scrap,
      Items.silicon,
      Items.plastanium,
      Items.surgeAlloy,
    );

    // 着陆该星球时解锁的物品
    this.unlockedOnLand.addAll(
      // 解锁的物品
    );
  },
});

module.exports = 加利尼亚;