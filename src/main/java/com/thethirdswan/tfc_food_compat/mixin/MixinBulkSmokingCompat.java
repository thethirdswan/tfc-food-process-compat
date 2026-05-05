package com.thethirdswan.tfc_food_compat.mixin;

import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(RecipeApplier.class)
public class MixinBulkSmokingCompat {
    @Inject(method = "applyRecipeOn(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/crafting/Recipe;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Recipe;getResultItem()Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    private static void onApplyRecipe(ItemStack stackIn, Recipe<?> recipe, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> modifiedStacks;
        ItemStack output = recipe.getResultItem();
        output = ItemStackProvider.of(output, CopyFoodModifier.INSTANCE).getStack(stackIn);
        modifiedStacks = ItemHelper.multipliedOutput(stackIn, output);
        cir.setReturnValue(modifiedStacks);
    }
}
