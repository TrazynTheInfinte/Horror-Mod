package com.horrormod.client;

import java.util.HashSet;
import java.util.Set;

// Client-side cache of the local player's journal progress, kept in sync by SyncJournalPacket.
public class ClientJournalData
{
    private static Set<String> discovered = new HashSet<>();

    public static void setDiscovered(Set<String> ids)
    {
        discovered = new HashSet<>(ids);
    }

    public static boolean isDiscovered(String id)
    {
        return discovered.contains(id);
    }
}
