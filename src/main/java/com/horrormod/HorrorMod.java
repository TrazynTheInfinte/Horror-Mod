package com.horrormod;

import com.horrormod.block.ModBlocks;
import com.horrormod.effect.ModEffects;
import com.horrormod.item.ModItems;
import com.horrormod.network.HorrorModNetwork;
import com.horrormod.potion.ModBrewing;
import com.horrormod.potion.ModPotions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod(HorrorMod.MODID)
public class HorrorMod
{
    public static final String MODID = "horrormod";

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> HORROR_TAB = CREATIVE_MODE_TABS.register("horror_tab",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .title(Component.translatable("itemGroup.horrormod"))
                    .icon(() -> ModItems.BLOOD_POOL.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.BLOOD_POOL.get());
                        output.accept(ModItems.WORN_JOURNAL.get());
                        output.accept(ModItems.BLOOD_SOAKED_QUILL.get());
                        output.accept(ModItems.OTHERWORLD_STONE.get());
                        output.accept(ModItems.OTHERWORLD_GROUND.get());
                        output.accept(ModItems.RITUALISTIC_DAGGER.get());
                    })
                    .build());

    public HorrorMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
        ModPotions.POTIONS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        HorrorModNetwork.register();

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        ModBrewing.register(event);
    }
}
