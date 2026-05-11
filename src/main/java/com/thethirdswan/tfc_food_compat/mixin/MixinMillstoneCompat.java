package com.thethirdswan.tfc_food_compat.mixin;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import com.thethirdswan.tfc_food_compat.TFCFoodCompat;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MillstoneBlockEntity.class)
public class MixinMillstoneCompat extends KineticBlockEntity {
    @Shadow(remap = false)
    private MillingRecipe lastRecipe;

    @Shadow(remap = false)
    public ItemStackHandler outputInv;

    @Shadow(remap = false)
    public ItemStackHandler inputInv;

    public MixinMillstoneCompat(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Unique
    private ItemStack tfc_food_process_compat$input;

    @Inject(method = "process", at = @At(value = "HEAD"), remap = false)
    private void getInput(CallbackInfo ci) {
        tfc_food_process_compat$input = inputInv.getStackInSlot(0);
        TFCFoodCompat.LOGGER.info("what is the input of millstone? {}", tfc_food_process_compat$input);
    }

//    TODO figure out where to inject properly
//    @Inject(method = "process", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/millstone/MillingRecipe;rollResults(Lnet/minecraft/util/RandomSource;)Ljava/util/List;"), remap = false)
//    private void onRollResults(CallbackInfo ci) {
////        ItemHandlerHelper.insertItemStacked()
//        lastRecipe.rollResults(level.random).forEach(itemStack -> {ItemHandlerHelper.insertItemStacked(outputInv, ItemStackProvider.of(itemStack, CopyFoodModifier.INSTANCE).getStack(tfc_food_process_compat$input), false);});
//        TFCFoodCompat.LOGGER.info("rolling results for {}", lastRecipe);
//    }

    @Inject(method = "process", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"), remap = false)
    private void onCraftingRemainingItems(CallbackInfo ci) {
        TFCFoodCompat.LOGGER.info("what's the 'getCraftingRemainingItems' of millstone? {}", inputInv.getStackInSlot(0).getCraftingRemainingItem());
        if (!inputInv.getStackInSlot(0).getCraftingRemainingItem().isEmpty()) {
            TFCFoodCompat.LOGGER.info("crafting remaining item is not empty");
            ItemHandlerHelper.insertItemStacked(outputInv, ItemStackProvider.of(lastRecipe.getResultItem(level.registryAccess()), CopyFoodModifier.INSTANCE).getStack(tfc_food_process_compat$input), false);
        }

        TFCFoodCompat.LOGGER.info("crafting remaining recipe called");
    }
}
