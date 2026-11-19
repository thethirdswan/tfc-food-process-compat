package com.thethirdswan.tfc_food_compat.mixin;

import net.dries007.tfc.common.component.food.FoodCapability;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

import java.util.Collection;
import java.util.List;

@Mixin(CookingPotBlockEntity.class)
public class MixinCookingPotCompat {
    @Shadow(remap = false)
    @Final
    private ItemStackHandler inventory;

    @ModifyArg(method = "processCooking", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/items/ItemStackHandler;setStackInSlot(ILnet/minecraft/world/item/ItemStack;)V"), index = 1, remap = false)
    private ItemStack setStackInSlot(ItemStack stack) {
        Collection<ItemStack> inputs = new java.util.ArrayList<>(List.of());
        for (int i = 0; i < 6; i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                inputs.add(this.inventory.getStackInSlot(i));
            }
        }
        return FoodCapability.updateFoodFromAllPrevious(inputs, stack);
    }
}
