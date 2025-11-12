package darkRegion.kt.world.production

import arc.Core
import arc.func.Prov
import arc.graphics.g2d.Draw
import arc.math.Mathf
import arc.math.geom.Geometry
import arc.scene.ui.Button
import arc.scene.ui.layout.Table
import arc.struct.EnumSet
import arc.struct.Seq
import arc.util.Strings
import arc.util.Time
import arc.util.io.Reads
import arc.util.io.Writes
import darkRegion.kt.type.RecipeStack
import mindustry.Vars
import mindustry.gen.Building
import mindustry.gen.Iconc
import mindustry.gen.Sounds
import mindustry.graphics.Pal
import mindustry.type.ItemStack
import mindustry.type.Liquid
import mindustry.type.LiquidStack
import mindustry.ui.Bar
import mindustry.world.blocks.production.GenericCrafter
import mindustry.world.consumers.Consume
import mindustry.world.consumers.ConsumeLiquid
import mindustry.world.consumers.ConsumeLiquids
import mindustry.world.consumers.ConsumePower
import mindustry.world.meta.*
import kotlin.math.min


/**
 * 多合成工厂
 *
 * @property recipes 配方列表
 */
class MultipleRecipeCrafter(name: String) : GenericCrafter(name) {
    var recipes: RecipeStack = RecipeStack()
    var dumpExtraLiquid = true
    var ignoreLiquidFullness = false

    init {
        solid = true
        update = true
        hasItems = true
        drawArrow = false
        configurable = true
        ambientSound = Sounds.machine
        ambientSoundVolume = 0.03f
        flags = EnumSet.of(BlockFlag.factory)

        config(Int::class.java) { tile: MultipleRecipeCrafterBuild, it -> tile.currentRecipe = it }

        buildType = Prov(::MultipleRecipeCrafterBuild)
    }

    override fun init() {
        super.init()
        recipes.apply(this)
        if (hasPower && consumesPower) {
            val cs: ArrayList<ConsumePower> = ArrayList()
            recipes.forEach { f ->
                val p: ConsumePower? = f.consPower
                p?.let {
                    cs.add(it)
                }
            }
            val csa: Array<ConsumePower> = arrayOf()
            //  consPower =
            //     ConsumePowerMultiple(cs.toArray(csa))
        }
        hasConsumers = true
    }

    override fun setBars() {
        super.setBars()
        var added = false
        var outPower = false
        var consP = false
        val addedLiquids = Seq<Liquid>()
        recipes.forEach { recipe ->
            if (recipe.powerProduction > 0) outPower = true
            recipe.inputs?.let {
                for (cons in it) {
                    if (cons is ConsumePower) consP = true
                    if (cons is ConsumeLiquid) {
                        added = true
                        if (addedLiquids.contains(cons.liquid)) continue
                        addedLiquids.add(cons.liquid)
                        addLiquidBar(cons.liquid)
                    } else if (cons is ConsumeLiquids) {
                        added = true
                        for (stack in cons.liquids) {
                            if (addedLiquids.contains(stack.liquid)) continue
                            addedLiquids.add(stack.liquid)
                            addLiquidBar(stack.liquid)
                        }
                    }
                }
            }

            recipe.outputLiquids?.forEach liquids@{ out ->
                if (addedLiquids.contains(out.liquid)) return@liquids
                addedLiquids.add(out.liquid)
                addLiquidBar(out.liquid)
            }

            if (!added) {
                if (recipes.hasOutputLiquids()) {
                    addLiquidBar { build: Building -> build.liquids.current() }
                }
            }
        }
        if (outPower) {
            addBar(
                "outPower"
            ) { entity: MultipleRecipeCrafterBuild ->
                Bar(
                    {
                        Core.bundle.format(
                            "bar.poweroutput",
                            Strings.fixed(entity.powerProduction * 60 * entity.timeScale(), 1)
                        )
                    },
                    { Pal.powerBar }, { entity.efficiency })
            }
        }

        if (consPower != null) {
            addBar("power") { entity: MultipleRecipeCrafterBuild ->
                Bar({ "${Iconc.power} todo" }, { Pal.powerBar }, {
                    val power = entity.power.graph.getPowerProduced() + entity.power.graph.getBatteryStored() > 0f
                    if (Mathf.zero(consPower.requestedPower(entity)) && power) 1f else entity.power.status
                })
            }
        }

        addBar("productionProgress") { build: MultipleRecipeCrafterBuild ->
            Bar({ "生产进度" }, { Pal.ammo }) { build.progress }
        }
    }

    override fun outputsItems(): Boolean {
        return recipes.hasOutputItems()
    }

    inner class MultipleRecipeCrafterBuild : GenericCrafterBuild() {
        var progress: Float = 0f
        var totalProgress: Float = 0f
        var warmup: Float = 0f
        var currentRecipe = -1
        var recipe = recipes[currentRecipe]
        var outputItems: Array<ItemStack>? = recipe.outputItems
        var outputLiquids: Array<LiquidStack>? = recipe.outputLiquids
        var powerProductionTimer: Float = 0f
        var consPower: ConsumePower? = null

        override fun draw() {
            super.draw()
            outputItems?.let {
                //drawItemSelection(it[0].item)
                return
            }
            outputLiquids?.let {
                //drawItemSelection(it[0].liquid)
            }
        }

        override fun shouldConsume(): Boolean {
            return currentRecipe != -1 && super.shouldConsume()
        }

        override fun config(): Any {
            return currentRecipe
        }

        override fun updateConsumption() {
            if (cheating()) {
                potentialEfficiency = if (enabled && productionValid()) 1.0f else 0.0f
                optionalEfficiency = if (shouldConsume()) potentialEfficiency else 0.0f
                efficiency = optionalEfficiency
                //shouldConsumePower = true
                updateEfficiencyMultiplier()
                return
            }
            if (!enabled) {
                optionalEfficiency = 0.0f
                efficiency = optionalEfficiency
                potentialEfficiency = efficiency
                //shouldConsumePower = false
                return
            }
            val update = shouldConsume() && productionValid()
            var minEfficiency = 1.0f
            optionalEfficiency = 1.0f
            efficiency = optionalEfficiency
            //shouldConsumePower = true
            val nonOptionalConsumers = Seq(recipe.inputs).select { consume ->
                !consume.optional && !consume.ignore()
            }.toArray<Consume>(Consume::class.java)
            val optionalConsumers = Seq(recipe.inputs).select { consume ->
                consume.optional && !consume.ignore()
            }.toArray<Consume>(Consume::class.java)

            for (cons in nonOptionalConsumers) {
                val result = cons.efficiency(this)
                if (cons !== consPower && result <= 1.0E-7f) {
                    //shouldConsumePower = false
                }
                minEfficiency = min(minEfficiency, result)
            }
            for (cons in optionalConsumers) {
                optionalEfficiency = min(optionalEfficiency, cons.efficiency(this))
            }
            efficiency = minEfficiency
            optionalEfficiency = min(optionalEfficiency, minEfficiency)
            potentialEfficiency = efficiency
            if (!update) {
                optionalEfficiency = 0.0f
                efficiency = optionalEfficiency
            }
            updateEfficiencyMultiplier()
            if (update && efficiency > 0) {
                for (cons in recipe.inputs!!) {
                    cons.update(this)
                }
            }
        }

        override fun displayConsumption(table: Table) {
            super.displayConsumption(table)
            recipe.build(this, table)
        }

        override fun updateTile() {
            recipe = recipes[currentRecipe]
            outputItems = recipe.outputItems
            outputLiquids = recipe.outputLiquids
            consPower = recipe.consPower
            if (efficiency > 0) {
                progress += getProgressIncrease(recipe.craftTime)
                warmup = Mathf.approachDelta(warmup, warmupTarget(), recipe.warmupSpeed)
                //continuously output based on efficiency
                if (outputLiquids != null) {
                    val inc = getProgressIncrease(1f)
                    for (output in outputLiquids!!) {
                        handleLiquid(
                            this, output.liquid,
                            min((output.amount * inc).toDouble(), (liquidCapacity - liquids[output.liquid]).toDouble())
                                .toFloat()
                        )
                    }
                }

                if (wasVisible && Mathf.chanceDelta(recipe.updateEffectChance.toDouble())) {
                    recipe.updateEffect.at(x + Mathf.range(size * 4f), y + Mathf.range(size * 4))
                }
            } else {
                warmup = Mathf.approachDelta(warmup, 0f, recipe.warmupSpeed)
            }
            totalProgress += warmup * Time.delta

            if (progress >= 1f) {
                craft()
            }

            dumpOutputs()
        }

        override fun drawSelect() {
            super.drawSelect()
            if (outputLiquids != null) {
                outputLiquids!!.indices.forEach {
                    val dir = if (recipe.liquidOutputDirections.size > it) recipe.liquidOutputDirections[it] else -1

                    if (dir != -1) {
                        Draw.rect(
                            outputLiquids!![it].liquid.fullIcon,
                            x + Geometry.d4x(dir + rotation) * (size * Vars.tilesize / 2f + 4),
                            y + Geometry.d4y(dir + rotation) * (size * Vars.tilesize / 2f + 4), 8f, 8f
                        )
                    }
                }
            }
        }

        override fun getPowerProduction(): Float =
            if (powerProductionTimer > 0f) recipe.powerProduction * efficiency else 0f

        override fun craft() {
            recipe.trigger(this)
            outputItems?.forEach { repeat(it.amount - 1) { _ -> offload(it.item) } }
            if (wasVisible) recipe.craftEffect.at(x, y)
            progress %= 1f
            powerProductionTimer += recipe.craftTime / efficiency + 1f
        }

        override fun dumpOutputs() {
            if (outputItems != null && timer(timerDump, dumpTime / timeScale)) {
                for (output in outputItems!!) {
                    dump(output.item)
                }
            }
            outputLiquids?.apply {
                indices.forEach {
                    val dir = if (recipe.liquidOutputDirections.size > it) recipe.liquidOutputDirections[it] else -1
                    dumpLiquid(this[it].liquid, 2f, dir)
                }
            }

        }

        override fun buildConfiguration(table: Table) {
            super.buildConfiguration(table)
            table.pane {
                for ((index, form) in recipes.withIndex()) {
                    it.button({ button ->
                        button.update {
                            button.isChecked = index == currentRecipe
                        }
                    }, Button.ButtonStyle()) {
                        currentRecipe = index
                        Vars.control.input.config.hideConfig()
                    }.height(100f).margin(10f).pad(3f).row()
                }
            }
        }

        override fun write(write: Writes) {
            super.write(write)
            write.f(progress)
            write.f(warmup)
            write.b(currentRecipe)
        }

        override fun read(read: Reads, revision: Byte) {
            super.read(read, revision)
            progress = read.f()
            warmup = read.f()
            currentRecipe = read.b().toInt()
        }
    }
}