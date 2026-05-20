package com.thethirdswan.tfc_food_compat.mixin;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
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

    @Unique
    private ItemStack tfc_food_process_compat$input;

    public MixinMillstoneCompat(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Inject(method = "process", at = @At(value = "HEAD"), remap = false)
    private void getInput(CallbackInfo ci) {
        if (!inputInv.getStackInSlot(0).isEmpty()) {
            tfc_food_process_compat$input = inputInv.getStackInSlot(0).copy();
        }
    }

    @Inject(method = "process", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/millstone/MillingRecipe;rollResults()Ljava/util/List;"), remap = false, cancellable = true)
    private void onProcess(CallbackInfo ci) {
        ItemStack craftingRemainingItem = inputInv.getStackInSlot(0).getCraftingRemainingItem();
        lastRecipe.rollResults().forEach(item -> ItemHandlerHelper.insertItemStacked(outputInv, ItemStackProvider.of(item, CopyFoodModifier.INSTANCE).getStack(tfc_food_process_compat$input), false));

        if (!craftingRemainingItem.isEmpty()) ItemHandlerHelper.insertItemStacked(outputInv, ItemStackProvider.of(craftingRemainingItem, CopyFoodModifier.INSTANCE).getStack(tfc_food_process_compat$input), false);

        this.award(AllAdvancements.MILLSTONE);
        this.sendData();
        this.setChanged();
        ci.cancel();
    }
}
