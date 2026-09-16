package com.horrormod.network;

import com.horrormod.HorrorMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class HorrorModNetwork
{
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(HorrorMod.MODID, "main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    public static void register()
    {
        CHANNEL.registerMessage(0, SyncJournalPacket.class,
                SyncJournalPacket::encode, SyncJournalPacket::decode, SyncJournalPacket::handle);
    }
}
