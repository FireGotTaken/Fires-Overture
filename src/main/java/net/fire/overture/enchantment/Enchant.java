package net.fire.overture.enchantment;

import net.fire.overture.OvertureMod;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Enchant {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, OvertureMod.MODID);

    public static final RegistryObject<Enchantment> JINX =
            ENCHANTMENTS.register("jinx", EnchantmentJinx::new);

    public static final RegistryObject<Enchantment> SATIATION =
            ENCHANTMENTS.register("satiation", EnchantmentSatiation::new);

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}
