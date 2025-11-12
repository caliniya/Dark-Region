package darkRegion.kt.type

import arc.struct.Seq
import mindustry.gen.Building
import mindustry.world.Block

/**
 * 配方堆栈类 - 用于管理和操作多个配方
 *
 * @see Seq
 */
class RecipeStack : Seq<Recipe>() {
    /**
     * 使用 Lambda 表达式添加配方
     *
     * @param function 配置配方的 Lambda 表达式
     * @return 当前 RecipeStack 实例，支持链式调用
     */
    fun add(function: Recipe.() -> Unit): RecipeStack {
        val recipe = Recipe()
        function(recipe)
        add(recipe)
        return this
    }

    /**
     * 批量添加配方到堆栈
     *
     * @param recipes 可变参数的配方实例
     * @return 当前 RecipeStack 实例，支持链式调用
     */
    fun addRecipe(vararg recipes: Recipe): RecipeStack {
        recipes.forEach(this::add)
        return this
    }

    /**
     * 检查指定索引的配方是否包含物品输出
     *
     * @param index 配方在堆栈中的索引位置
     * @return 如果配方有物品输出返回 true，否则返回 false
     */
    fun hasOutputItems(index: Int): Boolean = get(index).outputItems != null

    /**
     * 检查堆栈中是否存在任何包含物品输出的配方
     *
     * @return 如果存在有物品输出的配方返回 true，否则返回 false
     */
    fun hasOutputItems(): Boolean = any { it.outputItems != null }

    /**
     * 检查堆栈中是否存在任何包含液体输出的配方
     *
     * @return 如果存在有液体输出的配方返回 true，否则返回 false
     */
    fun hasOutputLiquids(): Boolean = any { it.outputLiquids != null }

    /**
     * 触发所有配方的建筑效果
     *
     * @param build 目标建筑实例
     */
    fun trigger(build: Building) = forEach { it.trigger(build) }

    /**
     * 将所有配方配置应用到指定方块
     *
     * @param block 目标方块实例
     */
    fun apply(block: Block) = forEach { it.apply(block) }
}