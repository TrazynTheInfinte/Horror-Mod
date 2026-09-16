package com.horrormod.client;

import net.minecraft.client.Minecraft;

// Kept in its own class so the client-only Minecraft/Screen references stay
// out of WornJournalItem, which is loaded on the dedicated server too.
public class ClientJournalScreenOpener
{
    public static void open()
    {
        Minecraft.getInstance().setScreen(new JournalScreen());
    }
}
