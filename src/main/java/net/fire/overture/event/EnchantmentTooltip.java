package net.fire.overture.event;

import net.fire.overture.OvertureMod;
import net.fire.overture.enchantment.EffectMemory;
import net.fire.overture.enchantment.Enchant;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = OvertureMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EnchantmentTooltip {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!EffectMemory.hasRoseEnchantment(stack)) {
            return;
        }
        MobEffect effect = EffectMemory.getEffect(stack);
        if (effect == null) {
            return;
        }

        List<Component> lines = event.getToolTip();
        int enchantmentLine = findEnchantmentLine(lines);
        int position = enchantmentLine < 0 ? lines.size() : enchantmentLine + 1;
        lines.add(position, Component.literal("  ").append(
                effect.getDisplayName().copy().withStyle(Style.EMPTY.withColor(effect.getColor()))));
    }

    private static int findEnchantmentLine(List<Component> pLines) {
        String jinx = Enchant.JINX.get().getFullname(1).getString();
        String satiation = Enchant.SATIATION.get().getFullname(1).getString();
        for (int i = 0; i < pLines.size(); i++) {
            String text = pLines.get(i).getString();
            if (text.equals(jinx) || text.equals(satiation)) {
                return i;
            }
        }
        return -1;
    }
}