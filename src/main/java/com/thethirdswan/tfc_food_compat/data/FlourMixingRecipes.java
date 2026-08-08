package com.thethirdswan.tfc_food_compat.data;

import com.simibubi.create.api.data.recipe.DatagenMod;
import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.concurrent.CompletableFuture;

public class FlourMixingRecipes extends MixingRecipeGen {
    public FlourMixingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);
    }

    public Fluid YEAST_STARTER = BuiltInRegistries.FLUID.get(ResourceLocation.fromNamespaceAndPath("firmalife", "yeast_starter"));
    public Fluid FRESH_WATER = BuiltInRegistries.FLUID.get(ResourceLocation.fromNamespaceAndPath("minecraft", "water"));

    GeneratedRecipe
    RYE = create(TFCGrains.RYE.getName(), b -> b.require(TFCGrains.RYE.getItem(TFCGrains.RYE.getName(), "flour")).require(SizedFluidIngredient.of(YEAST_STARTER, 200)).whenModLoaded("firmalife").output(TFCGrains.RYE.getDough(TFCGrains.RYE.getName()))),
    BARLEY = create(TFCGrains.BARLEY.getName(), b -> b.require(TFCGrains.BARLEY.getItem(TFCGrains.BARLEY.getName(), "flour")).require(SizedFluidIngredient.of(YEAST_STARTER, 200)).whenModLoaded("firmalife").output(TFCGrains.BARLEY.getDough(TFCGrains.BARLEY.getName()))),
    OAT = create(TFCGrains.OAT.getName(), b -> b.require(TFCGrains.OAT.getItem(TFCGrains.OAT.getName(), "flour")).require(SizedFluidIngredient.of(YEAST_STARTER, 200)).whenModLoaded("firmalife").output(TFCGrains.OAT.getDough(TFCGrains.OAT.getName()))),
    RICE = create(TFCGrains.RICE.getName(), b -> b.require(TFCGrains.RICE.getItem(TFCGrains.RICE.getName(), "flour")).require(SizedFluidIngredient.of(YEAST_STARTER, 200)).whenModLoaded("firmalife").output(TFCGrains.RICE.getDough(TFCGrains.RICE.getName()))),
    MAIZE = create(TFCGrains.MAIZE.getName(), b -> b.require(TFCGrains.MAIZE.getItem(TFCGrains.MAIZE.getName(), "flour")).require(SizedFluidIngredient.of(YEAST_STARTER, 200)).whenModLoaded("firmalife").output(TFCGrains.MAIZE.getDough(TFCGrains.MAIZE.getName()))),
    WHEAT = create(TFCGrains.WHEAT.getName(), b -> b.require(TFCGrains.WHEAT.getItem(TFCGrains.WHEAT.getName(), "flour")).require(SizedFluidIngredient.of(YEAST_STARTER, 200)).whenModLoaded("firmalife").output(TFCGrains.WHEAT.getDough(TFCGrains.WHEAT.getName()))),

    FLAT_RYE = create(TFCGrains.RYE.getName() + "_flat", b -> b.require(TFCGrains.RYE.getItem(TFCGrains.RYE.getName(), "flour")).require(SizedFluidIngredient.of(FRESH_WATER, 200)).output(TFCGrains.RYE.getItem(TFCGrains.RYE.getName(), "dough"))),
    FLAT_BARLEY = create(TFCGrains.BARLEY.getName() + "_flat", b -> b.require(TFCGrains.BARLEY.getItem(TFCGrains.BARLEY.getName(), "flour")).require(SizedFluidIngredient.of(FRESH_WATER, 200)).output(TFCGrains.BARLEY.getItem(TFCGrains.BARLEY.getName(), "dough"))),
    FLAT_OAT = create(TFCGrains.OAT.getName() + "_flat", b -> b.require(TFCGrains.OAT.getItem(TFCGrains.OAT.getName(), "flour")).require(SizedFluidIngredient.of(FRESH_WATER, 200)).output(TFCGrains.OAT.getItem(TFCGrains.OAT.getName(), "dough"))),
    FLAT_RICE = create(TFCGrains.RICE.getName() + "_flat", b -> b.require(TFCGrains.RICE.getItem(TFCGrains.RICE.getName(), "flour")).require(SizedFluidIngredient.of(FRESH_WATER, 200)).output(TFCGrains.RICE.getItem(TFCGrains.RICE.getName(), "dough"))),
    FLAT_MAIZE = create(TFCGrains.MAIZE.getName() + "_flat", b -> b.require(TFCGrains.MAIZE.getItem(TFCGrains.MAIZE.getName(), "flour")).require(SizedFluidIngredient.of(FRESH_WATER, 200)).output(TFCGrains.MAIZE.getItem(TFCGrains.MAIZE.getName(), "dough"))),
    FLAT_WHEAT = create(TFCGrains.WHEAT.getName() + "_flat", b -> b.require(TFCGrains.WHEAT.getItem(TFCGrains.WHEAT.getName(), "flour")).require(SizedFluidIngredient.of(FRESH_WATER, 200)).output(TFCGrains.WHEAT.getItem(TFCGrains.WHEAT.getName(), "dough")));
}
