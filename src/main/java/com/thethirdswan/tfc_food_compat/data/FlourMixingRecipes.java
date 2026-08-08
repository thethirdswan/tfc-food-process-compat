package com.thethirdswan.tfc_food_compat.data;

import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class FlourMixingRecipes extends MixingRecipeGen {
    public FlourMixingRecipes(PackOutput output, String defaultNamespace) {
        super(output, defaultNamespace);
        addFlourMixingRecipe();
    }

    public Fluid YEAST_STARTER = ForgeRegistries.FLUIDS.getValue(ResourceLocation.fromNamespaceAndPath("firmalife", "yeast_starter"));
    public Fluid FRESH_WATER = ForgeRegistries.FLUIDS.getValue(ResourceLocation.fromNamespaceAndPath("minecraft", "water"));

    static List<GeneratedRecipe> flourMixingRecipes = new ArrayList<>();

    void addFlourMixingRecipe(){
        for (TFCGrains grain : TFCGrains.values()) {
            flourMixingRecipes.add(create(grain.getName(), b -> b.require(grain.getItem(grain.getName(), "flour")).require(YEAST_STARTER, 200).whenModLoaded("firmalife").output(grain.getDough(grain.getName()))));
            flourMixingRecipes.add(create(grain.getName() + "_flat", b -> b.require(grain.getItem(grain.getName(), "flour")).require(FRESH_WATER, 200). output(grain.getItem(grain.getName(), "dough"))));
        }
    }
}
