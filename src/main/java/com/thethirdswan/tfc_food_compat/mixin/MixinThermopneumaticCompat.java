package com.thethirdswan.tfc_food_compat.mixin;

import me.desht.pneumaticcraft.common.block.entity.ThermopneumaticProcessingPlantBlockEntity;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ThermopneumaticProcessingPlantBlockEntity.class)
public abstract class MixinThermopneumaticCompat {
    @Shadow(remap = false)
    public abstract IItemHandler getPrimaryInventory();

    @ModifyArg(method = "runOneCycle", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"), index = 1, remap = false)
    private ItemStack onInsertItem(ItemStack stack) {
        ItemStack input = getPrimaryInventory().getStackInSlot(0);
        return ItemStackProvider.of(stack, CopyFoodModifier.INSTANCE).getSingleStack(input);
    }
}