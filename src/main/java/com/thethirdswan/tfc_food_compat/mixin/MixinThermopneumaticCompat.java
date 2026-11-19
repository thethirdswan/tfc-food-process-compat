package com.thethirdswan.tfc_food_compat.mixin;

import me.desht.pneumaticcraft.common.block.entity.processing.ThermoPlantBlockEntity;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ThermoPlantBlockEntity.class)
public abstract class MixinThermopneumaticCompat {
    @Shadow(remap = false)
    public abstract IItemHandler getInputItemHandler();

    @ModifyArg(method = "runOneCycle", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/items/ItemStackHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"), remap = false)
    private ItemStack insertItem(ItemStack stack) {
        ItemStack input = this.getInputItemHandler().getStackInSlot(0);
        return ItemStackProvider.of(stack, CopyFoodModifier.INSTANCE).getSingleStack(input);
    }
}
