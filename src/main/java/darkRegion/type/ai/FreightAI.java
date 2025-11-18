package darkRegion.type.ai;

import arc.struct.Seq;
import arc.util.Log;
import mindustry.entities.units.AIController;
import mindustry.gen.Building;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.production.GenericCrafter;

public class FreightAI <T extends GenericCrafter> extends AIController{
    
    public Seq<FactoryInfo<T>> factories = new Seq<>();
    public int targetTimer = 300; // 工厂寻找冷却
    
    private FactoryPair<T> currentTargetPair; // 当前选中的工厂对
    private boolean isBusy = false; // 是否忙碌状态
    private int busyTimer = 600; // 忙碌状态冷却
    private int scanDelayTimer = 300; // 扫描后延迟
    private boolean hasScanned = false; // 是否已完成扫描
    
    // 工厂对类，存储匹配的输入输出工厂
    public static class FactoryPair<T extends Block> {
        public FactoryInfo<T> outputFactory; // 输出工厂
        public FactoryInfo<T> inputFactory;  // 输入工厂
        
        public FactoryPair(FactoryInfo<T> outputFactory, FactoryInfo<T> inputFactory) {
            this.outputFactory = outputFactory;
            this.inputFactory = inputFactory;
        }
    }
    
    //抽象的工厂信息类
    public static class FactoryInfo<T extends Block> {
        public Tile position;
        public T factoryBlock;
        public Seq<String> requirements; // 需求物品列表
        public Seq<String> outputs; // 输出物品列表
        
        public FactoryInfo(Tile position, T factoryBlock, Seq<String> requirements, Seq<String> outputs) {
            this.position = position;
            this.factoryBlock = factoryBlock;
            this.requirements = requirements;
            this.outputs = outputs;
        }
    }
    
    @Override
    public void updateUnit() {
        super.updateUnit();
    }
    
    
    @Override
    public void updateTargeting() {
        // 定期扫描工厂
        if(timer.get(timerTarget,targetTimer)) {
            scanFriendlyFactories();
            hasScanned = true;
            scanDelayTimer = 0; // 重置延迟计时器
        }
        
        // 更新忙碌状态计时器
        if(isBusy) {
            busyTimer--;
            if(busyTimer <= 0) {
                isBusy = false;
                busyTimer = 600; // 重置为10秒
            }
        }
        
        // 更新扫描延迟计时器
        if(hasScanned) {
            scanDelayTimer++;
        }
        
        // 扫描后1秒且空闲时，随机选择工厂对
        if(hasScanned && scanDelayTimer >= 60 && !isBusy && factories.size >= 2) {
            selectRandomFactoryPair();
        }
    }
    
    /**
     * 扫描友方工厂并存储信息
     */
    protected void scanFriendlyFactories() {
        factories.clear();
        // 扫描所有友方建筑
        for(Building building : unit.team.data().buildings){
            if(building.block.category == Category.crafting && building.block.hasItems) {
                T factoryBlock = (T)building.block;
                Tile position = building.tile;
                Seq<Item> requiredItems = new Seq<>();
                Seq<Item> outputItems = new Seq<>();
                Seq<Item> items = new Seq<>();
                
                building.items.each((item, amount) -> {
                    if(amount > 0) {
                        items.add(item);
                    }
                    
                });
                
                //factories.add(new FactoryInfo<>(position, factoryBlock, requiredItems, outputItems));
            }
        }
    }
    
    /**
     * 随机选择匹配的工厂对
     */
    protected void selectRandomFactoryPair() {
        Seq<FactoryPair<T>> validPairs = new Seq<>();
        
        // 查找所有有效的工厂对（一个工厂的输出匹配另一个工厂的输入）
        for(int i = 0; i < factories.size; i++) {
            FactoryInfo<T> factory1 = factories.get(i);
            
            for(int j = i + 1; j < factories.size; j++) {
                FactoryInfo<T> factory2 = factories.get(j);
                
                // 检查工厂1的输出是否匹配工厂2的输入
                if(hasMatchingItems(factory1.outputs, factory2.requirements)) {
                    validPairs.add(new FactoryPair<>(factory1, factory2));
                }
                
                // 检查工厂2的输出是否匹配工厂1的输入
                if(hasMatchingItems(factory2.outputs, factory1.requirements)) {
                    validPairs.add(new FactoryPair<>(factory2, factory1));
                }
            }
        }
        
        // 随机选择一个有效的工厂对
        if(validPairs.size > 0) {
            currentTargetPair = validPairs.random();
            isBusy = true;
            busyTimer = 600; // 设置10秒忙碌状态
            hasScanned = false; // 重置扫描标志
            
            // 输出调试信息（可选）
            Log.debug("工厂对: " + 
                currentTargetPair.outputFactory.factoryBlock + " -> " + 
                currentTargetPair.inputFactory.factoryBlock);
        }
    }
    
    /**
     * 检查两个物品列表是否有匹配项
     */
    protected boolean hasMatchingItems(Seq<String> outputs, Seq<String> requirements) {
        for(String output : outputs) {
            for(String requirement : requirements) {
                if(output.equals(requirement)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void updateMovement() {
        if(currentTargetPair != null && currentTargetPair.outputFactory != null && isBusy) {
            // 直线飞往输出工厂
            moveTo(currentTargetPair.outputFactory.position,5);
        }
    }
    
    
    /**
     * 获取扫描到的工厂列表
     */
    public Seq<FactoryInfo<T>> getScannedFactories() {
        return factories;
    }
    
    /**
     * 获取当前选中的工厂对
     */
    public FactoryPair<T> getCurrentTargetPair() {
        return currentTargetPair;
    }
    
    /**
     * 获取当前是否忙碌
     */
    public boolean isBusy() {
        return isBusy;
    }
}