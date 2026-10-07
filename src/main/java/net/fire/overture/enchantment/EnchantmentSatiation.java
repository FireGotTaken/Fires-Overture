package net.fire.overture.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentSatiation extends Enchantment {
    protected EnchantmentSatiation() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR,
                new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
    }

    public boolean isTreasureOnly() {
        return true;
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

    public boolean canEnchant(ItemStack pStack) {
        return pStack.getItem() instanceof ArmorItem;
    }
}
