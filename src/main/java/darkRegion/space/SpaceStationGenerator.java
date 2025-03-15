package darkRegion.space;

import arc.math.geom.Vec2;
import mindustry.game.Team;
import mindustry.graphics.g3d.PlanetParams;
import mindustry.maps.generators.BlankPlanetGenerator;
import mindustry.type.Sector;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;

import static mindustry.Vars.state;
import static mindustry.Vars.world;

import darkRegion.content.DRLoadouts;
import darkRegion.content.DRBlocks;
import darkRegion.util.WorldDef;

public class SpaceStationGenerator extends BlankPlanetGenerator {
    public Block core = DRBlocks.highTemperatureCore;
    
    @Override
    public void generate() {
        defaultLoadout = DRLoadouts.basicSpaceStations;
        int sx = width / 2, sy = height / 2;

        Floor background = mindustry.content.Blocks.empty.asFloor();
        Floor ground = DRBlocks.spaceStationFloorFixed.asFloor();

        tiles.eachTile(t -> t.setFloor(background));

        WorldDef.getAreaTile(new Vec2(sx, sy), core.size + 6, core.size + 6).each(t -> {
            t.setFloor(ground);
        });
        world.tile(sx + core.size / 2 + 3, sy + core.size / 2 + 3).setBlock(core, Team.sharded);

        state.rules.planetBackground = new PlanetParams() {{
            planet = sector.planet.parent;
            camPos.setZero();
            sector.planet.addParentOffset(camPos);
            camPos.scl(0.5f);
            zoom = 0.3f;
        }};

        state.rules.dragMultiplier = 0.4f; 
        state.rules.borderDarkness = false;
        state.rules.waves = false;
    }

    @Override
    public int getSectorSize(Sector sector) {
        return 600;
    }
}