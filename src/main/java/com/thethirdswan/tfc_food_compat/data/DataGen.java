package com.thethirdswan.tfc_food_compat.data;


import com.thethirdswan.tfc_food_compat.TFCFoodCompat;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

import static com.thethirdswan.tfc_food_compat.data.FoodProcessingRecipeProvider.registerCreateProcessing;

@Mod.EventBusSubscriber(modid = TFCFoodCompat.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> dataGenFuture = event.getLookupProvider();

        ExistingFileHelper exFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) registerCreateProcessing(generator, output);
    }
}
