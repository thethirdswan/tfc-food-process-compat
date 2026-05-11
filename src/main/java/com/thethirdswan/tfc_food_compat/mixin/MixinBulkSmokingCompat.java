package com.thethirdswan.tfc_food_compat.mixin;

import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.thethirdswan.tfc_food_compat.TFCFoodCompat;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(RecipeApplier.class)
public class MixinBulkSmokingCompat {
//    TODO figure out why items being cooked are getting multiplied
    @Inject(method = "applyRecipeOn(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/crafting/Recipe;Z)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/Recipe;getResultItem(Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"), remap = false, cancellable = true)
    private static void onApplyRecipe(Level level, ItemStack stackIn, Recipe<?> recipe, boolean returnProcessingRemainder, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> modifiedStacks;
        ItemStack output = recipe.getResultItem(level.registryAccess()).copy();
        output = ItemStackProvider.of(output, CopyFoodModifier.INSTANCE).getStack(stackIn);
        modifiedStacks = ItemHelper.multipliedOutput(stackIn, output);
        TFCFoodCompat.LOGGER.info("bulk smoking compat called");
        TFCFoodCompat.LOGGER.info("how many items are there: {}", modifiedStacks.getFirst().getCount());
        cir.setReturnValue(modifiedStacks);
    }
}
