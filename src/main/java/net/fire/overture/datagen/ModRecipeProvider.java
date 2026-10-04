package net.fire.overture.datagen;

import net.fire.overture.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.ALCHEMICAL_ROSE.get())
                .requires(Items.WITHER_ROSE)
                .requires(Items.BLAZE_ROD)
                .requires(Items.NETHER_WART)
                .unlockedBy(getHasName(Items.BLAZE_ROD), has(Items.BLAZE_ROD))
                .unlockedBy(getHasName(Items.NETHER_WART), has(Items.NETHER_WART))
                .unlockedBy(getHasName(Items.WITHER_ROSE), has(Items.WITHER_ROSE))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLACK_DYE)
                .requires(ModBlocks.ALCHEMICAL_ROSE.get())
                .group("black_dye")
                .unlockedBy(getHasName(ModBlocks.ALCHEMICAL_ROSE.get()), has(ModBlocks.ALCHEMICAL_ROSE.get()))
                .save(pWriter);

    }
}