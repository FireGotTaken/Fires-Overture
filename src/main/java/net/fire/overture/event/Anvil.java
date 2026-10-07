package net.fire.overture.event;

import net.fire.overture.OvertureMod;
import net.fire.overture.enchantment.EffectMemory;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = OvertureMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Anvil {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack book = event.getRight();

        if (!book.is(Items.ENCHANTED_BOOK) || !EffectMemory.isActive(book) || EffectMemory.hasRoseEnchantment(left)) {
            return;
        }

        boolean creative = event.getPlayer().getAbilities().instabuild;
        Map<Enchantment, Integer> result = EnchantmentHelper.getEnchantments(left);
        int cost = 0;

        for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(book).entrySet()) {
            Enchantment enchantment = entry.getKey();
            int current = result.getOrDefault(enchantment, 0);
            int level = current == entry.getValue() ? current + 1 : Math.max(current, entry.getValue());

            boolean allowed = creative || left.is(Items.ENCHANTED_BOOK) || enchantment.canEnchant(left);
            for (Enchantment existing : result.keySet()) {
                if (existing != enchantment && !enchantment.isCompatibleWith(existing)) {
                    allowed = false;
                    cost++;
                }
            }

            if (allowed) {
                level = Math.min(level, enchantment.getMaxLevel());
                result.put(enchantment, level);
                cost += bookCost(enchantment) * level;
                if (left.getCount() > 1) {
                    cost = 40;
                }
            }
        }

        ItemStack output = left.copy();
        EnchantmentHelper.setEnchantments(result, output);
        if (!EffectMemory.hasRoseEnchantment(output) || !output.isBookEnchantable(book)) {
            return;
        }

        String name = event.getName();
        if (name != null && !Util.isBlank(name)) {
            if (!name.equals(left.getHoverName().getString())) {
                output.setHoverName(Component.literal(name));
                cost++;
            }
        } else if (left.hasCustomHoverName()) {
            output.resetHoverName();
            cost++;
        }

        cost += left.getBaseRepairCost() + book.getBaseRepairCost();
        if (cost >= 40 && !creative) {
            return;
        }

        output.setRepairCost(AnvilMenu.calculateIncreasedRepairCost(
                Math.max(left.getBaseRepairCost(), book.getBaseRepairCost())));
        EffectMemory.copy(book, output);

        event.setOutput(output);
        event.setCost(cost);
    }

    private static int bookCost(Enchantment pEnchantment) {
        return switch (pEnchantment.getRarity()) {
            case COMMON, UNCOMMON -> 1;
            case RARE -> 2;
            case VERY_RARE -> 4;
        };
    }
}