/// 从我别的项目抄过来的，用来测试。尽管现在还没用上

package darkRegion.kt.content

import arc.util.Log
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.world.Block
import mindustry.world.blocks.production.Drill
import mindustry.world.meta.Env
import mindustry.content.Items as MindustryItems
import mindustry.content.Liquids as MindustryLiquids

class Blocks {
    companion object {
        lateinit var mechanicalDrillSmall: Block

        @JvmStatic
        fun load() {
            Log.debug("注册方块 mechanical-drill-small")
            mechanicalDrillSmall = Drill("mechanical-drill-small").apply {
                requirements(
                    Category.production,
                    ItemStack.with(MindustryItems.copper, 12)
                )
                tier = 2
                drillTime = 600f
                size = 1
                //mechanical drill doesn't work in space
                envEnabled = envEnabled xor Env.space

                consumeLiquid(MindustryLiquids.water, 0.05f).boost()
            }
        }
    }
}