package com.thethirdswan.tfc_food_compat.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public enum TFCGrains {
    RYE("rye"),
    BARLEY("barley"),
    RICE("rice"),
    MAIZE("maize"),
    OAT("oat"),
    WHEAT("wheat");

    public final String name;

    TFCGrains(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Item getItem(String itemName, String itemType) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("tfc", "food/" + itemName + "_" + itemType));
    }

    public Item getDough(String itemName) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("firmalife", "food/" + itemName + "_dough"));
    }
}
