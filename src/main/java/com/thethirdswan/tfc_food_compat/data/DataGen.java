package com.thethirdswan.tfc_food_compat.data;


import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static com.thethirdswan.tfc_food_compat.data.RecipeProvider.registerCreateProcessing;

public class DataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> dataGenFuture = event.getLookupProvider();

        ExistingFileHelper exFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeServer(), new RecipeProvider(output, dataGenFuture));
        if (event.includeServer()) registerCreateProcessing(generator, output, dataGenFuture);
    }
}
