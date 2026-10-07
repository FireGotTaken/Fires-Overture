package net.fire.overture.block.custom;

import net.fire.overture.block.custom.blockentity.AlchemicalRoseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PottedAlchemicalRose extends FlowerPotBlock implements EntityBlock {

    public PottedAlchemicalRose(@Nullable Supplier<FlowerPotBlock> pEmptyPot, Supplier<? extends Block> pContent, BlockBehaviour.Properties pProperties) {
        super(pEmptyPot, pContent, pProperties);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AlchemicalRoseBlockEntity(pPos, pState);
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack held = pPlayer.getItemInHand(pHand);

        if (held.getItem() instanceof BlockItem blockItem
                && this.getEmptyPot().getFullPotsView().containsKey(ForgeRegistries.BLOCKS.getKey(blockItem.getBlock()))) {
            return InteractionResult.CONSUME;
        }

        if (!pLevel.isClientSide) {
            ItemStack rose = pLevel.getBlockEntity(pPos) instanceof AlchemicalRoseBlockEntity blockEntity
                    ? blockEntity.createRoseItem()
                    : new ItemStack(this.getContent());

            if (held.isEmpty()) {
                pPlayer.setItemInHand(pHand, rose);
            } else if (!pPlayer.addItem(rose)) {
                pPlayer.drop(rose, false);
            }

            pLevel.setBlock(pPos, this.getEmptyPot().defaultBlockState(), 3);
            pLevel.gameEvent(pPlayer, GameEvent.BLOCK_CHANGE, pPos);
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }
}