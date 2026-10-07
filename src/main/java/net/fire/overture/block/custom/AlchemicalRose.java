package net.fire.overture.block.custom;

import net.fire.overture.block.custom.blockentity.AlchemicalRoseBlockEntity;
import net.fire.overture.datagen.ModBlockEntities;
import net.fire.overture.event.EffectDurationTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class AlchemicalRose extends FlowerBlock implements EntityBlock {
    public AlchemicalRose(Supplier<MobEffect> effectSupplier, int pEffectDuration, Properties pProperties) {
        super(effectSupplier, pEffectDuration, pProperties);
    }

    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pLevel.isClientSide && pEntity instanceof LivingEntity livingEntity
                && pEntity.getType() != EntityType.FOX && pEntity.getType() != EntityType.BEE) {
            if (pEntity.xOld != pEntity.getX() || pEntity.zOld != pEntity.getZ()) {
                double d0 = Math.abs(pEntity.getX() - pEntity.xOld);
                double d1 = Math.abs(pEntity.getZ() - pEntity.zOld);
                if (d0 >= (double)0.003F || d1 >= (double)0.003F) {
                    pEntity.hurt(pLevel.damageSources().sweetBerryBush(), 1.0F);
                }
            }

            if (!(pLevel.getBlockEntity(pPos) instanceof AlchemicalRoseBlockEntity rose)) {
                return;
            }

            MobEffect remembered = rose.getEffect();
            if (remembered == null) {
                MobEffectInstance shortest = null;
                for (MobEffectInstance instance : livingEntity.getActiveEffects()) {
                    if (shortest == null || remainingTime(instance) < remainingTime(shortest)) {
                        shortest = instance;
                    }
                }

                if (shortest != null) {
                    rose.remember(shortest.getEffect(), shortest.getAmplifier(),
                            EffectDurationTracker.getOriginalDuration(livingEntity, shortest));
                }
            }

            else {
                livingEntity.addEffect(new MobEffectInstance(remembered, rose.getDuration(), rose.getAmplifier()));
            }
        }
    }

    private static int remainingTime(MobEffectInstance pInstance) {
        return pInstance.isInfiniteDuration() ? Integer.MAX_VALUE : pInstance.getDuration();
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AlchemicalRoseBlockEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (!pLevel.isClientSide || pBlockEntityType != ModBlockEntities.ALCHEMICAL_ROSE.get()) {
            return null;
        }
        return (level, pos, state, blockEntity) ->
                AlchemicalRoseBlockEntity.clientTick(level, pos, state, (AlchemicalRoseBlockEntity)blockEntity);
    }

    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        MobEffect effect = AlchemicalRoseBlockEntity.getEffectFromItem(pStack);
        if (effect != null) {
            pTooltip.add(effect.getDisplayName().copy().withStyle(Style.EMPTY.withColor(effect.getColor())));
        }
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return super.mayPlaceOn(pState, pLevel, pPos) || pState.is(Blocks.NETHERRACK) || pState.is(Blocks.SOUL_SAND) || pState.is(Blocks.SOUL_SOIL);
    }
}