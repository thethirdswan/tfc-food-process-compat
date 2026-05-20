package com.thethirdswan.tfc_food_compat.data;

import com.simibubi.create.api.data.recipe.MillingRecipeGen;
import net.minecraft.data.PackOutput;

import java.util.ArrayList;
import java.util.List;

public class GrainMillingRecipes extends MillingRecipeGen {
    public GrainMillingRecipes(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
        addGrainMillingRecipes();
    }

    static List<GeneratedRecipe> grainMillingRecipes = new ArrayList<>();
    void addGrainMillingRecipes() {
        for (TFCGrains grain : TFCGrains.values()) {
            grainMillingRecipes.add(create(grain.getName(), b -> b.require(grain.getItem(grain.getName(), "grain")).duration(100).output(grain.getItem(grain.getName(), "flour"))));
        }
    }
}
