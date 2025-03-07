//by miner 20230607

var range = 32;

var bounceBulletType = extend(BasicBulletType, 3, 20, {
    lifetime: 180,
    
    pierceCap: 12,
    pierceDamageFactor: 0.9,
    
    trailLength: 24,
    trailChance: 0.5,
    trailInterval: 10,
    
    hitEntity(b, entity, health){
        this.super$hitEntity(b, entity, health);
        
        let {team, x, y, vel} = b;
        let target = null;
        if(entity instanceof Unit){
            target = Units.closestEnemy(team, x, y, range, unit => !b.hasCollided(unit.id));
        }else{
            target = Units.findEnemyTile(team, x, y, range, build => !b.hasCollided(build.id));
        }
        
        if(target != null){
            vel.setAngle(Angles.angle(x, y, target.x, target.y));
        }
    },
});

// 测试
Events.on(ClientLoadEvent, e => {
    Blocks.duo.ammoTypes.put(Items.copper, bounceBulletType);
});