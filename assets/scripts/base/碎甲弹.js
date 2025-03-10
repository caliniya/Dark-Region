var ArmorReductionBulletType = extend(BasicBulletType, {//继承自基础类型子弹
		hitEntity(b, entity, health) {
			this.super$hitEntity(b, entity, health);
			entity.armor -= ard;//削弱命中目标的装甲
		},
		speed: speed,
		damage: damage,
		lifetime: lifetime,
		pierceCap: 2,
		pierceBuilding: true,
		ammoMultiplier: 1
	});
}
bulletTypes.ArmorReductionBulletType = ArmorReductionBulletType//，注册子弹类型