package com.thethirdswan.tfc_food_compat.mixin;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import net.dries007.tfc.common.component.food.FoodCapability;
import net.dries007.tfc.common.component.food.IFood;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(BasinRecipe.class)
public class MixinCreateBasinRecipeCompat {
    @Unique
    private static ItemStack tfc_food_process_compat$input = ItemStack.EMPTY;

    @Inject(method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z", at = @At(value = "HEAD"), remap = false)
    private static void getItemInput(BasinBlockEntity basin, Recipe<?> recipe, boolean test, CallbackInfoReturnable<Boolean> cir) {
        for (int i = 0; i < basin.inputInventory.getSlots(); i++) {
            IFood food = FoodCapability.get(basin.inputInventory.getItem(i));
            if (food != null) {
                tfc_food_process_compat$input = basin.inputInventory.getItem(i);
                break;
            }
        }
    }
//    does the singular add needs to be modified too?
    @ModifyArg(method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), remap = false)
    private static Object modifyAdd(Object o) {
        if (tfc_food_process_compat$input.isEmpty()) return o;
        if (o instanceof ItemStack) {
            return ItemStackProvider.of((ItemStack) o, CopyFoodModifier.INSTANCE).getSingleStack(tfc_food_process_compat$input);
        }
        return o;
    }

    @ModifyArg(method = "apply(Lcom/simibubi/create/content/processing/basin/BasinBlockEntity;Lnet/minecraft/world/item/crafting/Recipe;Z)Z", at = @At(value = "INVOKE", target = "Ljava/util/List;addAll(Ljava/util/Collection;)Z"), remap = false)
    private static Collection<ItemStack> modifyAddAll(Collection<ItemStack> c) {
        List<ItemStack> newList = new ArrayList<>();
        if (tfc_food_process_compat$input.isEmpty()) return c;
        c.forEach(item -> newList.add(ItemStackProvider.of(item, CopyFoodModifier.INSTANCE).getSingleStack(tfc_food_process_compat$input)));
        return newList;
    }
}