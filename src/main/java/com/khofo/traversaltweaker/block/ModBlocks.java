package com.khofo.traversaltweaker.block;

import com.khofo.traversaltweaker.item.ModCreativeModeTab;
import com.khofo.traversaltweaker.item.ModItems;
import com.khofo.traversaltweaker.traversaltweaker;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;


public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, traversaltweaker.MOD_ID);

    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block){
        return BLOCKS.register(name,block);
    }
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T>block, CreativeModeTab tab){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),new Item.Properties().tab(tab)));
    }
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }



    public static final RegistryObject<Block> CITRINE_BLOCK = registerBlock("citrine_block", () -> new Block(BlockBehaviour.Properties.of(Material.GRASS)
            .strength(9f).requiresCorrectToolForDrops()), ModCreativeModeTab.TRAVERSAL_TWEAKER);

    public static final  RegistryObject<Block> TEST_BLOCK = registerBlock("test_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
            .strength(5f).requiresCorrectToolForDrops()) , ModCreativeModeTab.TRAVERSAL_TWEAKER);

    public static final RegistryObject<Block> TREE_TAP = registerBlock("tree_tap", () -> new TreeTapBlock(BlockBehaviour.Properties.of(Material.GRASS)
            .strength(3f).requiresCorrectToolForDrops().sound(SoundType.CHAIN)), ModCreativeModeTab.TRAVERSAL_TWEAKER);

}
