/// 原始代码来源未知，由 caliniya 获取
/// 注意：需补充原始代码的许可证信息
/// 当前版本经过重构和注释添加（AI 注释太好用了）

package darkRegion.type

import arc.scene.ui.layout.Table
import mindustry.content.Fx
import mindustry.entities.Effect
import mindustry.gen.Building
import mindustry.type.ItemStack
import mindustry.type.LiquidStack
import mindustry.world.Block
import mindustry.world.consumers.Consume
import mindustry.world.consumers.ConsumePower
import mindustry.world.meta.Stat
import mindustry.world.meta.StatUnit
import mindustry.world.meta.StatValues
import mindustry.world.meta.Stats

/**
 * 配方类 - 定义机器或结构的输入输出规则、生产效果和统计信息
 *
 * 用于配置完整的生产流程，包括资源消耗、产物输出、特效和电力管理等
 *
 * @property inputs 输入消耗数组，定义配方所需的各种资源
 * @property outputItems 输出物品数组，定义配方产出的物品及其数量
 * @property outputLiquids 输出液体数组，定义配方产出的液体及其数量
 * @property craftTime 制作时间（单位：帧），默认 60 帧（1 秒）
 * @property liquidOutputDirections 液体输出方向数组，-1 表示所有方向
 * @property craftEffect 制作完成时触发的特效
 * @property updateEffect 更新时触发的特效
 * @property updateEffectChance 更新特效触发概率，默认 0.04（4%）
 * @property warmupSpeed 预热速度，影响生产效率的渐变过程
 * @property powerProduction 电力生产量，大于 0 表示该配方发电
 * @property consPower 电力消耗配置，单独存储以便特殊处理
 */
class Formula {
    var inputs: Array<Consume>? = null
    var outputItems: Array<ItemStack>? = null
    var outputLiquids: Array<LiquidStack>? = null
    var craftTime: Float = 60f
    var liquidOutputDirections: IntArray = intArrayOf(-1)
    var craftEffect: Effect = Fx.none
    var updateEffect: Effect = Fx.none
    var updateEffectChance: Float = 0.04f
    var warmupSpeed: Float = 0.019f
    var powerProduction: Float = 0f

    var consPower: ConsumePower? = null

    /**
     * 设置输入消耗配置
     *
     * @param inputs 可变参数的消耗对象
     * @return 当前 Formula 实例，支持链式调用
     */
    fun setInputs(vararg inputs: Consume): Formula {
        this.inputs = arrayOf(*inputs)
        return this
    }

    /**
     * 设置输出物品配置
     *
     * @param items 可变参数的物品堆栈
     * @return 当前 Formula 实例，支持链式调用
     */
    fun setOutputItems(vararg items: ItemStack): Formula {
        outputItems = arrayOf(*items)
        return this
    }

    /**
     * 设置输出液体配置
     *
     * @param liquids 可变参数的液体堆栈
     * @return 当前 Formula 实例，支持链式调用
     */
    fun setOutputLiquids(vararg liquids: LiquidStack): Formula {
        outputLiquids = arrayOf(*liquids)
        return this
    }

    /**
     * 完整设置配方的输入输出配置
     *
     * @param `in` 输入消耗数组
     * @param items 输出物品数组
     * @param liquids 输出液体数组
     * @return 当前 Formula 实例，支持链式调用
     */
    fun set(`in`: Array<Consume>, items: Array<ItemStack>, liquids: Array<LiquidStack>): Formula {
        inputs = `in`
        outputItems = items
        outputLiquids = liquids
        return this
    }

    /**
     * 设置电力生产量
     *
     * @param value 电力生产值，大于 0 表示该配方会发电
     * @return 当前 Formula 实例，支持链式调用
     */
    fun setPowerProduction(value: Float): Formula {
        powerProduction = value
        return this
    }


    /**
     * 将配方配置应用到目标方块
     *
     * 设置方块的消耗项和电力相关属性
     *
     * @param block 目标方块实例
     */
    fun apply(block: Block) {
        inputs?.forEach {
            if (it !is ConsumePower) {
                it.apply(block)
            } else {
                consPower = it
            }
        }

        if (powerProduction > 0) {
            block.hasPower = true
            block.outputsPower = true
        }
    }

    /**
     * 更新建筑状态
     *
     * 处理所有输入消耗的更新逻辑
     *
     * @param build 目标建筑实例
     */
    fun update(build: Building) {
        inputs?.let {
            for (c in it) {
                c.update(build)
            }
        }
    }

    /**
     * 触发建筑效果
     *
     * 激活所有输入消耗的触发效果
     *
     * @param build 目标建筑实例
     */
    fun trigger(build: Building) {
        inputs?.let {
            for (c in it) {
                c.trigger(build)
            }
        }
    }

    /**
     * 显示配方统计信息
     *
     * 在游戏界面中展示配方的各项数据和属性
     *
     * @param stats 统计对象
     * @param block 相关方块实例
     */
    fun display(stats: Stats, block: Block) {
        stats.timePeriod = craftTime

        inputs?.forEach { it.display(stats) }

        if ((block.hasItems && block.itemCapacity > 0) || outputItems != null) {
            stats.add(Stat.productionTime, craftTime / 60f, StatUnit.seconds)
        }

        outputItems?.let {
            stats.add(Stat.output, StatValues.items(craftTime, *it))
        }

        outputLiquids?.let {
            stats.add(Stat.output, StatValues.liquids(1f, *it))
        }

        if (powerProduction > 0) {
            stats.add(Stat.basePowerGeneration, powerProduction * 60f, StatUnit.powerSecond)
        }
    }

    /**
     * 构建UI显示
     *
     * 在用户界面中展示配方的输入信息
     *
     * @param build 相关建筑实例
     * @param table UI 表格容器
     */
    fun build(build: Building, table: Table) {
        table.pane { t -> inputs?.forEach { it.build(build, t) } }
    }

    /**
     * 返回配方的字符串表示
     *
     * @return 包含配方完整信息的字符串
     */
    override fun toString(): String {
        return "Formula{ input=${inputs.contentToString()}, outputItems=${outputItems.contentToString()}, outputLiquids=${outputLiquids.contentToString()}, craftTime=$craftTime, powerProduction=$powerProduction }"
    }
}
