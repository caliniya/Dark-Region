package darkRegion.map;

import darkRegion.content.DRLoadouts;
import darkRegion.content.JsonLoad;
import static mindustry.Vars.*;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;
import mindustry.ai.*;
import mindustry.ai.BaseRegistry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.maps.generators.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

public class GaliniaGenerator extends PlanetGenerator{
    Color c1 = Color.valueOf("5057a6"), c2 = Color.valueOf("272766");
    public Block core = JsonLoad.高温核心;
    Block[][] arr = {
    {Blocks.ice, Blocks.iceSnow, Blocks.iceSnow, Blocks.ice, Blocks.ice}
    };

    {
        baseSeed = 1;
    }
    
    @Override
    public int getSectorSize(Sector sector) {
        return 200 + super.getSectorSize(sector);
    }
    

    @Override
    public float getHeight(Vec3 position){
        return 0;
    }

    @Override
    public void getColor(Vec3 position, Color out){
        float depth = Simplex.noise3d(seed, 2, 0.56, 1.7f, position.x, position.y, position.z) / 2f;
        out.set(c1).lerp(c2, Mathf.clamp(Mathf.round(depth, 0.15f))).a(1f - 0.2f).toFloatBits();
    }

    @Override
    public float getSizeScl(){
        return 2000;
    }

    @Override
    public void addWeather(Sector sector, Rules rules){
        //no weather... yet
    }

    @Override
    public void genTile(Vec3 position, TileGen tile){
        // 使用六边形网格坐标来决定地形
        tile.floor = getHexagonalBlock(position);
        
        // 保持原有的装饰逻辑
        if(tile.floor == Blocks.redmat && rand.chance(0.1)){
            tile.block = Blocks.iceWall;
        }

        if(tile.floor == Blocks.bluemat && rand.chance(0.03)){
            tile.block = Blocks.iceWall;
        }

        if(tile.floor == Blocks.bluemat && rand.chance(0.002)){
            tile.block = Blocks.iceWall;
        }
    }

    @Override
    protected void generate(){
        int sx = width / 2, sy = height / 2;
        pass((x, y) -> {
            float max = 0;
            for(Point2 p : Geometry.d8){
                max = Math.max(max, world.getDarkness(x + p.x, y + p.y));
            }
            if(max > 0){
                block = floor.asFloor().wall;
            }

            if(noise(x, y, 40f, 1f) > 0.9){
                
            }
        });
        defaultLoadout = DRLoadouts.高温核心蓝图;
        //world.tile(sx + core.size / 2 + 3, sy + core.size / 2 + 3).setBlock(core, Team.sharded);
        Schematics.placeLaunchLoadout(width / 2, height / 2);
    }

    float rawHeight(Vec3 position){
        return Simplex.noise3d(seed, 8, 0.7f, 1f, position.x, position.y, position.z);
    }

    // 新的六边形区块生成方法
    Block getHexagonalBlock(Vec3 position){
        // 将3D坐标转换为六边形网格坐标
        HexGrid hexGrid = getHexGrid(position);
        
        // 使用六边形坐标作为噪声输入，而不是原始3D坐标
        float hexNoise = Simplex.noise3d(seed, 8, 0.7f, 1f, hexGrid.x * 0.1f, hexGrid.y * 0.1f, position.z);
        float tempNoise = Simplex.noise3d(seed, 8, 0.6, 1f/2f, hexGrid.x * 0.1f, hexGrid.y * 0.1f + 99f, position.z);
        
        hexNoise *= 1.2f;
        hexNoise = Mathf.clamp(hexNoise);
        
        // 增强六边形效果 - 让边界更明显
        float edgeFactor = getHexEdgeFactor(hexGrid);
        hexNoise = Mathf.lerp(hexNoise, edgeFactor, 0.3f);
        
        return arr[Mathf.clamp((int)(tempNoise * arr.length), 0, arr[0].length - 1)][Mathf.clamp((int)(hexNoise * arr[0].length), 0, arr[0].length - 1)];
    }
    
    // 计算六边形网格坐标
    HexGrid getHexGrid(Vec3 position){
        // 六边形网格大小
        float hexSize = 1f;
        
        // 将3D坐标转换为2D平面坐标
        float x = position.x / hexSize;
        float y = position.y / hexSize;
        
        // 六边形网格坐标计算
        float q = (x * Mathf.sqrt3 / 3f - y / 3f);
        float r = y * 2f / 3f;
        
        // 立方体坐标
        float cubeX = q;
        float cubeZ = r;
        float cubeY = -cubeX - cubeZ;
        
        // 四舍五入到最近的六边形中心
        float rx = Math.round(cubeX);
        float ry = Math.round(cubeY);
        float rz = Math.round(cubeZ);
        
        float x_diff = Math.abs(rx - cubeX);
        float y_diff = Math.abs(ry - cubeY);
        float z_diff = Math.abs(rz - cubeZ);
        
        if(x_diff > y_diff && x_diff > z_diff){
            rx = -ry - rz;
        }else if(y_diff > z_diff){
            ry = -rx - rz;
        }else{
            rz = -rx - ry;
        }
        
        return new HexGrid(rx, rz);
    }
    
    // 计算六边形边缘因子（用于增强六边形视觉效果）
    float getHexEdgeFactor(HexGrid hex){
        // 简单的距离计算来创建六边形边界
        float centerDist = Math.abs(hex.x) + Math.abs(hex.y) + Math.abs(-hex.x - hex.y);
        return Mathf.clamp(centerDist * 0.1f);
    }
    
    // 六边形网格坐标辅助类
    class HexGrid{
        float x, y;
        
        HexGrid(float x, float y){
            this.x = x;
            this.y = y;
        }
    }

    Block getBlock(Vec3 position){
        float height = rawHeight(position);
        Tmp.v31.set(position);
        position = Tmp.v33.set(position).scl(2f);
        float temp = Simplex.noise3d(seed, 8, 0.6, 1f/2f, position.x, position.y + 99f, position.z);
        height *= 1.2f;
        height = Mathf.clamp(height);

        return arr[Mathf.clamp((int)(temp * arr.length), 0, arr[0].length - 1)][Mathf.clamp((int)(height * arr[0].length), 0, arr[0].length - 1)];
    }
}