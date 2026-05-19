package com.thethirdswan.tfc_food_compat.data;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.thethirdswan.tfc_food_compat.TFCFoodCompat;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    static final List<ProcessingRecipeGen<?, ?, ?>> GENERATORS = new ArrayList<>();

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
    }

    public static void registerCreateProcessing(DataGenerator gen, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        GENERATORS.add(new FlourMixingRecipes(output, registries, TFCFoodCompat.MODID));
        GENERATORS.add(new GrainMillingRecipes(output, registries, TFCFoodCompat.MODID));
        gen.addProvider(true, new DataProvider() {
            @Override
            public CompletableFuture<?> run(CachedOutput output) {
                return CompletableFuture.allOf(GENERATORS.stream().map(gen -> gen.run(output)).toArray(CompletableFuture[]::new));
            }

            @Override
            public String getName() {
                return "TFC Food Compat's Create Recipes";
            }
        });
    }
}
