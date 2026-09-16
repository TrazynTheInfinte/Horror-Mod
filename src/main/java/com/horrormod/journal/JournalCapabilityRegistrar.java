package com.horrormod.journal;

import com.horrormod.HorrorMod;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HorrorMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JournalCapabilityRegistrar
{
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(JournalData.class);
    }
}
