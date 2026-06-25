package com.thethirdswan.tfc_food_compat.mixin;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrushingWheelControllerBlockEntity.class)
public class MixinCrushingWheelsCompat {
    @Unique
    private ItemStack tfc_food_process_compat$input = ItemStack.EMPTY;

    @Inject(method = "intakeItem", at = @At(value = "HEAD"), remap = false)
    private void getInput(ItemEntity itemEntity, CallbackInfo ci) {
        tfc_food_process_compat$input = itemEntity.getItem().copy();
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), index = 0)
    private Entity onAddFreshEntity(Entity entity) {
        if (entity instanceof ItemEntity) {
            ItemStack itemStack = ((ItemEntity)entity).getItem();
            ((ItemEntity)entity).setItem(ItemStackProvider.of(itemStack, CopyFoodModifier.INSTANCE).getStack(tfc_food_process_compat$input));
            return entity;
        }
        return entity;
    }

}
