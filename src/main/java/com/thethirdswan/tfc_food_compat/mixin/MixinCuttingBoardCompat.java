package com.thethirdswan.tfc_food_compat.mixin;

import com.thethirdswan.tfc_food_compat.TFCFoodCompat;
import net.dries007.tfc.common.recipes.outputs.CopyFoodModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
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
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipeInput;
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
    @Final
    private ItemStackHandler inventory;

    @Shadow(remap = false)
    public abstract ItemStack getStoredItem();

    @Shadow(remap = false)
    protected abstract Optional<RecipeHolder<CuttingBoardRecipe>> getMatchingRecipe(ItemStack toolStack, @Nullable Player player);

    @Shadow(remap = false)
    @Final
    private RecipeManager.CachedCheck<CuttingBoardRecipeInput, CuttingBoardRecipe> quickCheck;

    @Shadow(remap = false)
    public abstract void spawnCuttingParticles(ServerLevel level, BlockPos pos, ItemStack stack);

    @Shadow(remap = false)
    public abstract void playProcessingSound(@Nullable SoundEvent sound, ItemStack tool, ItemStack boardItem);

//    TODO figure out why did the result gets multiplied by the total items on the cutting board
    @Inject(method = "processStoredItemUsingTool", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V"), remap = false, cancellable = true)
    private void onProcessStoredItem(ItemStack toolStack, Player player, CallbackInfoReturnable<Boolean> cir) {
        Optional<RecipeHolder<CuttingBoardRecipe>> matchingRecipe = this.getMatchingRecipe(toolStack, player);
        matchingRecipe.ifPresent((recipe) -> {
            for(ItemStack resultStack : recipe.value().rollResults(this.level.random, EnchantmentHelper.getTagEnchantmentLevel(this.level.holder(Enchantments.FORTUNE).get(), toolStack), new RecipeWrapper(this.inventory))) {
                Direction direction = this.getBlockState().getValue(CuttingBoardBlock.FACING).getCounterClockWise();
                ItemUtils.spawnItemEntity(level, ItemStackProvider.of(resultStack.copy(), CopyFoodModifier.INSTANCE).getStack(getStoredItem()),
                        worldPosition.getX() + 0.5 + (direction.getStepX() * 0.2), worldPosition.getY() + 0.2, worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.2),
                        direction.getStepX() * 0.2F, 0.0F, direction.getStepZ() * 0.2F);
            }

            if (!this.level.isClientSide) {
                toolStack.hurtAndBreak(1, (ServerLevel)this.level, (ServerPlayer) player, (item) -> {
                });
                if (player != null) {
                    player.awardStat(Stats.ITEM_USED.get(toolStack.getItem()));
                }
            }

            Level patt0$temp = this.level;
            if (patt0$temp instanceof ServerLevel serverLevel) {
                this.spawnCuttingParticles(serverLevel, this.getBlockPos(), this.getStoredItem());
            }

            this.playProcessingSound(recipe.value().getSoundEvent().orElse(null), toolStack, this.getStoredItem());
            this.inventory.extractItem(0, 1, false);
            if (player instanceof ServerPlayer) {
                ModAdvancements.USE_CUTTING_BOARD.get().trigger((ServerPlayer)player);
                if (!this.getStoredItem().isEmpty()) {
                    player.displayClientMessage(TextUtils.block("cutting_board.remaining_items", this.getStoredItem().getCount()), true);
                } else {
                    player.displayClientMessage(Component.empty(), true);
                }
            }

        });

        TFCFoodCompat.LOGGER.info("cutting board compat called");
        cir.setReturnValue(matchingRecipe.isPresent());
    }
}
