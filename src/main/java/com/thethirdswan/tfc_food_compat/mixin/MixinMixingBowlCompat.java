package com.thethirdswan.tfc_food_compat.mixin;

import com.eerussianguy.firmalife.FirmaLife;
import com.eerussianguy.firmalife.common.blockentities.MixingBowlBlockEntity;
import com.eerussianguy.firmalife.common.recipes.MixingBowlRecipe;
import com.thethirdswan.tfc_food_compat.TFCFoodCompat;
import net.dries007.tfc.common.blockentities.TickableInventoryBlockEntity;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(MixingBowlBlockEntity.class)
public abstract class MixinMixingBowlCompat extends TickableInventoryBlockEntity<MixingBowlBlockEntity.MixingBowlInventory> {
    public MixinMixingBowlCompat(BlockEntityType<?> type, BlockPos pos, BlockState state, InventoryFactory<MixingBowlBlockEntity.MixingBowlInventory> inventory, Component defaultName) {
        super(type, pos, state, inventory, FirmaLife.MOD_ID);
    }

    @Shadow(remap = false)
    public abstract @Nullable MixingBowlRecipe getRecipe();

    @Shadow(remap = false)
    @Final
    public static int SLOTS;

    @Unique
    private List<ItemStack> htfc_subsidiaries$input =  new ArrayList<ItemStack>();

    @Inject(method = "finishMixing", at = @At(value = "HEAD"), remap = false)
    private void onGetOriginalIngredient(CallbackInfo ci) {
        for (int i = 0; i < SLOTS; i++) {
            htfc_subsidiaries$input.add(inventory.getStackInSlot(i));
        }
    }

    @Inject(method = "finishMixing", at = @At(value = "INVOKE", target = "Lcom/eerussianguy/firmalife/common/recipes/MixingBowlRecipe;getFluidIngredient()Ljava/util/Optional;"), remap = false)
    private void onFinishMixing(CallbackInfo ci) {
        for (int i = 0; i < SLOTS; i++) {
            inventory.setStackInSlot(i, htfc_subsidiaries$input.get(i));
        }
        ItemStack output = getRecipe().assemble(inventory, level.registryAccess());
        int count = output.getCount();
        for (int i = 0; i < SLOTS; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
        for (int i = 0; i < SLOTS; i++)
        {
            if (count > 0)
            {
                inventory.setStackInSlot(i,
                        ItemStackProvider.of(output, CopyFoodModifier.INSTANCE).getStack(htfc_subsidiaries$input.get(i)).copyWithCount(1)
                );
                count--;
            }
            else
            {
                break;
            }
        }
        TFCFoodCompat.LOGGER.info("finishMixing called");
        htfc_subsidiaries$input.clear();
    }
}
