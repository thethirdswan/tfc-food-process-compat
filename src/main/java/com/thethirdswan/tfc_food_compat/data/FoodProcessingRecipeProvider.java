package com.thethirdswan.tfc_food_compat.data;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.thethirdswan.tfc_food_compat.TFCFoodCompat;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class FoodProcessingRecipeProvider extends RecipeProvider {
    public FoodProcessingRecipeProvider(PackOutput output) {
        super(output);
    }

    static final List<ProcessingRecipeGen> GENERATORS = new ArrayList<>();

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
    }


    public static void registerCreateProcessing(DataGenerator gen, PackOutput output) {
        GENERATORS.add(new FlourMixingRecipes(output, TFCFoodCompat.MODID));
        GENERATORS.add(new GrainMillingRecipes(output, TFCFoodCompat.MODID));
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
