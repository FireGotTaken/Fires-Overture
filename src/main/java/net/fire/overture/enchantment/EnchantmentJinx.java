package net.fire.overture.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentJinx extends Enchantment {
    protected EnchantmentJinx() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof SwordItem
                || pStack.getItem() instanceof AxeItem
                || pStack.getItem() instanceof TridentItem;
    }

    public boolean canApplyAtEnchantingTable(ItemStack pStack) {
        return false;
    }

    public int getMinCost(int pLevel) {
        return 25;
    }

    public int getMaxCost(int pLevel) {
        return 50;
    }
}
