package com.horrormod.journal;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class JournalDataCapability
{
    public static final Capability<JournalData> JOURNAL_DATA = CapabilityManager.get(new CapabilityToken<>() {});
}
