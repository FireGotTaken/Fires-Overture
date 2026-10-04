package net.fire.overture.block;

import net.fire.overture.OvertureMod;
import net.fire.overture.item.ModItems;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, OvertureMod.MODID);

    public static final RegistryObject<Block> ALCHEMICAL_ROSE = registerBlock("alchemical_rose",
            () -> new FlowerBlock(() -> MobEffects.WITHER, 8,
            BlockBehaviour.Properties.copy(Blocks.CORNFLOWER).noOcclusion().noCollission()));

    public static final RegistryObject<Block> POTTED_ALCHEMICAL_ROSE = BLOCKS.register("potted_alchemical_rose",
            () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), ModBlocks.ALCHEMICAL_ROSE,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_CORNFLOWER).noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}