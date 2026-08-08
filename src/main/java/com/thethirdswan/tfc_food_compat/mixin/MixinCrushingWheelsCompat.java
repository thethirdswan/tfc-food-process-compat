package com.thethirdswan.tfc_food_compat.mixin;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingInventory;
import com.thethirdswan.tfc_food_compat.TFCFoodCompat;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrushingWheelControllerBlockEntity.class)
public class MixinCrushingWheelsCompat {
    @Shadow
    public ProcessingInventory inventory;
    @Unique
    private ItemStack tfc_food_process_compat$input = ItemStack.EMPTY;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/kinetics/crusher/CrushingWheelControllerBlockEntity;applyRecipe()V"), remap = false)
    private void getInput(CallbackInfo ci) {
        tfc_food_process_compat$input = this.inventory.getStackInSlot(0);
    }

    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), index = 0, remap = false)
    private Entity onAddFreshEntity(Entity entity) {
        if (entity instanceof ItemEntity) {
            ItemStack itemStack = ((ItemEntity)entity).getItem();
            ((ItemEntity)entity).setItem(ItemStackProvider.of(itemStack, CopyFoodModifier.INSTANCE).getSingleStack(tfc_food_process_compat$input));
            return entity;
        }
        return entity;
    }

}