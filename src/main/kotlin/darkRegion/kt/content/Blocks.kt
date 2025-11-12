/// 从我别的项目抄过来的，用来测试。尽管现在还没用上

package darkRegion.kt.content

import arc.struct.Seq
import arc.util.Log
import darkRegion.kt.world.production.MultipleRecipeCrafter
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.type.LiquidStack
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
            mechanicalDrillSmall = MultipleRecipeCrafter("mechanical-drill-small").apply {
                requirements(
                    Category.production,
                    ItemStack.with(MindustryItems.copper, 12)
                )
                size = 2
                //mechanical drill doesn't work in space
                envEnabled = envEnabled xor Env.space

                recipes = RecipeStack().add {
                    outputItems = ItemStack.with(MindustryItems.copper, 1)
                    outputLiquids = LiquidStack.with(MindustryLiquids.water, 1)
                }.add {
                    outputItems = ItemStack.with(MindustryItems.sand, 1)
                    outputLiquids = LiquidStack.with(MindustryLiquids.oil, 1)
                }
            }
        }
    }
}