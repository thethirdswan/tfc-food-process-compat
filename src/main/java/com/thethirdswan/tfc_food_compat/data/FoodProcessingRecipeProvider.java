package com.thethirdswan.tfc_food_compat.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.RecipeProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FoodProcessingRecipeProvider extends RecipeProvider {
    public FoodProcessingRecipeProvider(DataGenerator gen) {
        super(gen);
    }

    static final List<com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen> GENERATORS = new ArrayList<>();

    public static void registerCreateProcessing(DataGenerator gen) {
        GENERATORS.add(new FlourMixingRecipes(gen));
        GENERATORS.add(new GrainMillingRecipes(gen));
        gen.addProvider(new DataProvider() {
            @Override
            public void run(HashCache cache) throws IOException {
                GENERATORS.forEach(gen -> {
                    try {
                        gen.run(cache);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public String getName() {
                return "TFC Food Compat's Create Recipes";
            }
        });
    }
}
