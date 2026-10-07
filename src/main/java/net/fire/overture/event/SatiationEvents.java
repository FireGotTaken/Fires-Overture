package net.fire.overture.event;

import net.fire.overture.OvertureMod;
import net.fire.overture.enchantment.EffectMemory;
import net.fire.overture.enchantment.Enchant;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OvertureMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SatiationEvents {
    private static final int CHECK_INTERVAL = 10;
    private static final int EFFECT_DURATION = 1520;
    private static final int REFRESH_BELOW = 1;

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide || entity.tickCount % CHECK_INTERVAL != 0) {
            return;
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) {
                continue;
            }

            ItemStack armor = entity.getItemBySlot(slot);
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchant.SATIATION.get(), armor) <= 0) {
                continue;
            }

            MobEffect effect = EffectMemory.getEffect(armor);
            if (effect == null) {
                continue;
            }

            int amplifier = EffectMemory.getAmplifier(armor);
            MobEffectInstance current = entity.getEffect(effect);
            if (current == null || (!current.isInfiniteDuration() && current.getDuration() <= REFRESH_BELOW)) {
                entity.addEffect(new MobEffectInstance(effect, EFFECT_DURATION, amplifier, false, true));
            }
        }
    }
}