package net.fire.overture.datagen;

import net.fire.overture.OvertureMod;
import net.fire.overture.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, OvertureMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ModBlocks.ALCHEMICAL_ROSE.get(), models().cross(blockTexture(ModBlocks.ALCHEMICAL_ROSE.get()).getPath(),
                blockTexture(ModBlocks.ALCHEMICAL_ROSE.get())).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.POTTED_ALCHEMICAL_ROSE.get(), models().singleTexture("potted_alchemical_rose", new ResourceLocation("flower_pot_cross"), "plant",
                blockTexture(ModBlocks.ALCHEMICAL_ROSE.get())).renderType("cutout"));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}