package net.fire.overture.item;

import net.fire.overture.OvertureMod;
import net.fire.overture.block.ModBlocks;
import net.fire.overture.enchantment.Enchant;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OvertureMod.MODID);

    public static final RegistryObject<CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("mod_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.ALCHEMICAL_ROSE.get()))
                    .title(Component.translatable("creativetab.mod_tab"))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(ModBlocks.ALCHEMICAL_ROSE.get());
                        pOutput.accept(EnchantedBookItem.createForEnchantment(
                                new EnchantmentInstance(Enchant.JINX.get(), 1)));
                        pOutput.accept(EnchantedBookItem.createForEnchantment(
                                new EnchantmentInstance(Enchant.SATIATION.get(), 1)));


                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}