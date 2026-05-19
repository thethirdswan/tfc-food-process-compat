package com.thethirdswan.tfc_food_compat.data;

import com.simibubi.create.api.data.recipe.MillingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class GrainMillingRecipes extends MillingRecipeGen {
    public GrainMillingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);
    }

    GeneratedRecipe
            RYE = create(TFCGrains.RYE.getName(), b -> b.require(TFCGrains.RYE.getItem(TFCGrains.RYE.getName(), "grain")).duration(100).output(TFCGrains.RYE.getItem(TFCGrains.RYE.getName(), "flour"))),
            BARLEY = create(TFCGrains.BARLEY.getName(), b -> b.require(TFCGrains.BARLEY.getItem(TFCGrains.BARLEY.getName(), "grain")).duration(100).output(TFCGrains.BARLEY.getItem(TFCGrains.BARLEY.getName(), "flour"))),
            OAT = create(TFCGrains.OAT.getName(), b -> b.require(TFCGrains.OAT.getItem(TFCGrains.OAT.getName(), "grain")).duration(100).output(TFCGrains.OAT.getItem(TFCGrains.OAT.getName(), "flour"))),
            RICE = create(TFCGrains.RICE.getName(), b -> b.require(TFCGrains.RICE.getItem(TFCGrains.RICE.getName(), "grain")).duration(100).output(TFCGrains.RICE.getItem(TFCGrains.RICE.getName(), "flour"))),
            MAIZE = create(TFCGrains.MAIZE.getName(), b -> b.require(TFCGrains.MAIZE.getItem(TFCGrains.MAIZE.getName(), "grain")).duration(100).output(TFCGrains.MAIZE.getItem(TFCGrains.MAIZE.getName(), "flour"))),
            WHEAT = create(TFCGrains.WHEAT.getName(), b -> b.require(TFCGrains.WHEAT.getItem(TFCGrains.WHEAT.getName(), "grain")).duration(100).output(TFCGrains.WHEAT.getItem(TFCGrains.WHEAT.getName(), "flour")));
}
