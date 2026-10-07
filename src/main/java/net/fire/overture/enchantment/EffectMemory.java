package net.fire.overture.enchantment;

import net.fire.overture.block.custom.blockentity.AlchemicalRoseBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;

public class EffectMemory {
    public static final String TAG = "memory";

    public static boolean hasRoseEnchantment(ItemStack pStack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(pStack);
        return enchantments.containsKey(Enchant.JINX.get())
                || enchantments.containsKey(Enchant.SATIATION.get());
    }

    @Nullable
    public static MobEffect getEffect(ItemStack pStack) {
        CompoundTag tag = pStack.getTagElement(TAG);
        if (tag == null || !tag.contains(AlchemicalRoseBlockEntity.EFFECT_KEY)) {
            return null;
        }
        ResourceLocation key = ResourceLocation.tryParse(tag.getString(AlchemicalRoseBlockEntity.EFFECT_KEY));
        return key == null ? null : ForgeRegistries.MOB_EFFECTS.getValue(key);
    }

    public static int getAmplifier(ItemStack pStack) {
        CompoundTag tag = pStack.getTagElement(TAG);
        return tag == null ? 0 : tag.getInt(AlchemicalRoseBlockEntity.AMPLIFIER_KEY);
    }

    public static int getDuration(ItemStack pStack) {
        CompoundTag tag = pStack.getTagElement(TAG);
        return tag == null ? 0 : tag.getInt(AlchemicalRoseBlockEntity.DURATION_KEY);
    }

    public static void copyFromRose(AlchemicalRoseBlockEntity pRose, ItemStack pStack) {
        pRose.writeEffect(pStack.getOrCreateTagElement(TAG));
    }

    public static boolean isActive(ItemStack pStack) {
        return hasRoseEnchantment(pStack) && getEffect(pStack) != null;
    }

    public static void copy(ItemStack pFrom, ItemStack pTo) {
        CompoundTag tag = pFrom.getTagElement(TAG);
        if (tag != null) {
            pTo.getOrCreateTag().put(TAG, tag.copy());
        }
    }
}