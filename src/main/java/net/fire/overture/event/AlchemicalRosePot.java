package net.fire.overture.event;

import net.fire.overture.OvertureMod;
import net.fire.overture.block.ModBlocks;
import net.fire.overture.block.custom.blockentity.AlchemicalRoseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OvertureMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AlchemicalRosePot {

    @SubscribeEvent
    public static void onPotAlchemicalRose(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();

        if (player.isSecondaryUseActive()) {
            return;
        }
        if (!level.getBlockState(pos).is(Blocks.FLOWER_POT) || !stack.is(ModBlocks.ALCHEMICAL_ROSE.get().asItem())) {
            return;
        }

        if (!level.isClientSide) {
            level.setBlock(pos, ModBlocks.POTTED_ALCHEMICAL_ROSE.get().defaultBlockState(), 3);
            if (level.getBlockEntity(pos) instanceof AlchemicalRoseBlockEntity rose) {
                rose.loadFromItem(stack);
            }
            player.awardStat(Stats.POT_FLOWER);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
    }
}