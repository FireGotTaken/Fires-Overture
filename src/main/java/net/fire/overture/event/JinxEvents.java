package net.fire.overture.event;

import net.fire.overture.OvertureMod;
import net.fire.overture.enchantment.EffectMemory;
import net.fire.overture.enchantment.Enchant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OvertureMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class JinxEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getAmount() <= 0) {
            return;
        }

        DamageSource source = event.getSource();
        ItemStack weapon = getWeapon(source);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchant.JINX.get(), weapon) <= 0) {
            return;
        }

        MobEffect effect = EffectMemory.getEffect(weapon);
        if (effect == null) {
            return;
        }

        event.getEntity().addEffect(new MobEffectInstance(effect,
                EffectMemory.getDuration(weapon), EffectMemory.getAmplifier(weapon)), source.getEntity());
    }

    private static ItemStack getWeapon(DamageSource pSource) {
        if (isMeleeHit(pSource) && pSource.getEntity() instanceof LivingEntity attacker) {
            return attacker.getMainHandItem();
        }
        if (pSource.is(DamageTypes.TRIDENT) && pSource.getDirectEntity() instanceof ThrownTrident trident) {
            return ItemStack.of(trident.saveWithoutId(new CompoundTag()).getCompound("Trident"));
        }
        return ItemStack.EMPTY;
    }

    private static boolean isMeleeHit(DamageSource pSource) {
        return pSource.is(DamageTypes.PLAYER_ATTACK)
                || pSource.is(DamageTypes.MOB_ATTACK)
                || pSource.is(DamageTypes.MOB_ATTACK_NO_AGGRO);
    }
}