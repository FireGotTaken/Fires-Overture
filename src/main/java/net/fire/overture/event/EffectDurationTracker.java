package net.fire.overture.event;

import net.fire.overture.OvertureMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = OvertureMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EffectDurationTracker {
    private static final String KEY = "OriginalDuration";

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) {
            return;
        }

        MobEffectInstance added = event.getEffectInstance();
        MobEffectInstance old = event.getOldEffectInstance();
        if (old != null && !willReplace(added, old)) {
            return;
        }

        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(added.getEffect());
        if (id == null) {
            return;
        }

        CompoundTag durations = entity.getPersistentData().getCompound(KEY);
        durations.putInt(id.toString(), added.getDuration());
        entity.getPersistentData().put(KEY, durations);
    }

    private static boolean willReplace(MobEffectInstance pAdded, MobEffectInstance pOld) {
        if (pAdded.getAmplifier() > pOld.getAmplifier()) {
            return true;
        }
        if (pAdded.getAmplifier() < pOld.getAmplifier() || pOld.isInfiniteDuration()) {
            return false;
        }
        return pAdded.isInfiniteDuration() || pAdded.getDuration() > pOld.getDuration();
    }

    public static int getOriginalDuration(LivingEntity pEntity, MobEffectInstance pInstance) {
        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(pInstance.getEffect());
        CompoundTag durations = pEntity.getPersistentData().getCompound(KEY);
        if (id != null && durations.contains(id.toString())) {
            return durations.getInt(id.toString());
        }
        return pInstance.getDuration();
    }
}