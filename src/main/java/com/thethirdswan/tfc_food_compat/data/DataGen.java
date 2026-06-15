package com.thethirdswan.tfc_food_compat.data;


import com.thethirdswan.tfc_food_compat.TFCFoodCompat;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static com.thethirdswan.tfc_food_compat.data.FoodProcessingRecipeProvider.registerCreateProcessing;

@Mod.EventBusSubscriber(modid = TFCFoodCompat.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper exFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) registerCreateProcessing(generator);
    }
}
