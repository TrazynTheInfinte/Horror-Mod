package com.horrormod.journal;

public enum JournalCategory
{
    ITEMS,
    EFFECTS,
    ENTITIES,
    BIOMES,
    DIMENSIONS,
    STRUCTURES,
    LORE;

    public String getTitleKey()
    {
        return "journal.horrormod.category." + name().toLowerCase();
    }
}
