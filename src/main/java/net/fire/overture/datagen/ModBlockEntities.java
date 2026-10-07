package net.fire.overture.datagen;

import net.fire.overture.OvertureMod;
import net.fire.overture.block.ModBlocks;
import net.fire.overture.block.custom.blockentity.AlchemicalRoseBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, OvertureMod.MODID);

    public static final RegistryObject<BlockEntityType<AlchemicalRoseBlockEntity>> ALCHEMICAL_ROSE =
            BLOCK_ENTITIES.register("alchemical_rose", () ->
                    BlockEntityType.Builder.of(AlchemicalRoseBlockEntity::new,
                            ModBlocks.ALCHEMICAL_ROSE.get(),
                            ModBlocks.POTTED_ALCHEMICAL_ROSE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
