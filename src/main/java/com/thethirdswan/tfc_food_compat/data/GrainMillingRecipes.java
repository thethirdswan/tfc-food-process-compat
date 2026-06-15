package com.thethirdswan.tfc_food_compat.data;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.foundation.data.recipe.MillingRecipeGen;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.thethirdswan.tfc_food_compat.TFCFoodCompat;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class GrainMillingRecipes extends ProcessingRecipeGen {
    public GrainMillingRecipes(DataGenerator gen) {
        super(gen);
        addGrainMillingRecipes();
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.MILLING;
    }

    static List<GeneratedRecipe> grainMillingRecipes = new ArrayList<>();
    void addGrainMillingRecipes() {
        for (TFCGrains grain : TFCGrains.values()) {
            grainMillingRecipes.add(create(ResourceLocation.fromNamespaceAndPath(TFCFoodCompat.MOD_ID, grain.getName()), b -> b.require(grain.getItem(grain.getName(), "grain")).duration(100).output(grain.getItem(grain.getName(), "flour"))));
        }
    }
}
