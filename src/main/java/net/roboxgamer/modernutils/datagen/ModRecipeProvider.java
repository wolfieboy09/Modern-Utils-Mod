package net.roboxgamer.modernutils.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.roboxgamer.modernutils.ModernUtilsMod;
import net.roboxgamer.modernutils.block.ModBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
  public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
    super(pOutput, pRegistries);
  }
  
  @Override
  protected void buildRecipes(@NotNull RecipeOutput pRecipeOutput) {
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.MECHANICAL_CRAFTER_BLOCK.get())
        .requires(Items.CRAFTING_TABLE)
        .requires(Items.STONE)
        .unlockedBy("has_crafting_table", has(Items.CRAFTING_TABLE))
        .save(pRecipeOutput);
    
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.MINI_CHEST_BLOCK.get(),9)
        .requires(Items.CHEST)
        .unlockedBy("has_log",has(ItemTags.LOGS))
        .save(pRecipeOutput);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.MECHANICAL_FURNACE_BLOCK.get())
        .requires(Items.FURNACE)
        .requires(Items.CHEST)
        .requires(Items.HOPPER)
        .unlockedBy("has_furnace", has(Items.FURNACE))
        .save(pRecipeOutput);
  }
  
  protected static void oreSmelting(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
    oreCooking(pRecipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
               pExperience, pCookingTIme, pGroup, "_from_smelting");
  }
  
  protected static void oreBlasting(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
    oreCooking(pRecipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
               pExperience, pCookingTime, pGroup, "_from_blasting");
  }
  
  protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput pRecipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
    for (ItemLike itemlike : pIngredients) {
      SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime,
                                         pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike),
                                                                                               has(itemlike)).save(
          pRecipeOutput, ModernUtilsMod.MODID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
    }
  }
}