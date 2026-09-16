package com.horrormod.network;

import com.horrormod.client.ClientJournalData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

// Server -> client: the full set of discovered journal entry ids for this player.
public class SyncJournalPacket
{
    private final Set<String> discovered;

    public SyncJournalPacket(Set<String> discovered)
    {
        this.discovered = discovered;
    }

    public static void encode(SyncJournalPacket msg, FriendlyByteBuf buf)
    {
        buf.writeCollection(msg.discovered, FriendlyByteBuf::writeUtf);
    }

    public static SyncJournalPacket decode(FriendlyByteBuf buf)
    {
        return new SyncJournalPacket(buf.readCollection(HashSet::new, FriendlyByteBuf::readUtf));
    }

    public static void handle(SyncJournalPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> ClientJournalData.setDiscovered(msg.discovered));
        ctx.get().setPacketHandled(true);
    }
}
