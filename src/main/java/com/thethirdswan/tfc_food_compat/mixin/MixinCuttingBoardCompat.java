package com.thethirdswan.tfc_food_compat.mixin;

import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModAdvancements;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.Optional;

@Mixin(CuttingBoardBlockEntity.class)
public abstract class MixinCuttingBoardCompat extends SyncedBlockEntity {
    public MixinCuttingBoardCompat(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Shadow(remap = false)
    protected abstract Optional<CuttingBoardRecipe> getMatchingRecipe(RecipeWrapper recipeWrapper, ItemStack toolStack, @Nullable Player player);

    @Shadow(remap = false)
    @Final
    private ItemStackHandler inventory;

    @Shadow(remap = false)
    public abstract void playProcessingSound(String soundEventID, ItemStack tool, ItemStack boardItem);

    @Shadow(remap = false)
    public abstract ItemStack getStoredItem();

    @Inject(method = "processStoredItemUsingTool", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"), remap = false, cancellable = true)
    private void onProcessStoredItem(ItemStack toolStack, Player player, CallbackInfoReturnable<Boolean> cir) {
        Optional<CuttingBoardRecipe> matchingRecipe = getMatchingRecipe(new RecipeWrapper(this.inventory), toolStack, player);

        matchingRecipe.ifPresent((recipe) -> {
            for (ItemStack resultStack : recipe.rollResults(this.level.random, EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, toolStack), new RecipeWrapper(this.inventory))) {
                Direction direction = getBlockState().getValue(CuttingBoardBlock.FACING).getCounterClockWise();
                ItemUtils.spawnItemEntity(level, ItemStackProvider.of(resultStack.copy(), CopyFoodModifier.INSTANCE).getStack(getStoredItem()),
                        worldPosition.getX() + 0.5 + (direction.getStepX() * 0.2), worldPosition.getY() + 0.2, worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.2),
                        direction.getStepX() * 0.2F, 0.0F, direction.getStepZ() * 0.2F);
            }
            if (player != null) {
                toolStack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            } else {
                if (toolStack.hurt(1, level.random, null)) {
                    toolStack.setCount(0);
                }
            }
            playProcessingSound(recipe.getSoundEventID(), toolStack, getStoredItem());
            this.inventory.extractItem(0, 1, false);
            if (player instanceof ServerPlayer) {
                ModAdvancements.CUTTING_BOARD.trigger((ServerPlayer)player);
                if (!this.getStoredItem().isEmpty()) {
                    player.displayClientMessage(TextUtils.block("cutting_board.remaining_items", new Object[]{this.getStoredItem().getCount()}), true);
                } else {
                    player.displayClientMessage(Component.empty(), true);
                }
            }
        });

        cir.setReturnValue(matchingRecipe.isPresent());
    }
}
