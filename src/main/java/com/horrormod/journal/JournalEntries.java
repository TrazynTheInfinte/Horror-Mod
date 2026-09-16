package com.horrormod.journal;

import com.horrormod.effect.ModEffects;
import com.horrormod.item.ModItems;
import com.horrormod.potion.ModPotions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;

import java.util.ArrayList;
import java.util.List;

// The full list of everything the journal can teach the player about.
// Add an entry here whenever a new discoverable feature is added to the mod.
public class JournalEntries
{
    public static final JournalEntry BLOOD_POOL = JournalEntry.forItem("blood_pool", JournalCategory.ITEMS, ModItems.BLOOD_POOL);

    public static final JournalEntry LACERATION = JournalEntry.forEffect("laceration", JournalCategory.EFFECTS, ModEffects.LACERATION,
            () -> PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), ModPotions.LACERATION.get()));

    public static final JournalEntry BLOOD_SOAKED_QUILL = JournalEntry.forItem("blood_soaked_quill", JournalCategory.ITEMS, ModItems.BLOOD_SOAKED_QUILL);

    public static final JournalEntry THE_OTHERWORLD = JournalEntry.manual("the_otherworld", JournalCategory.LORE,
            () -> new ItemStack(ModItems.BLOOD_SOAKED_QUILL.get()));

    public static final JournalEntry THE_RITUALISTS = JournalEntry.manual("the_ritualists", JournalCategory.LORE,
            () -> new ItemStack(ModItems.BLOOD_SOAKED_QUILL.get()));

    public static final List<JournalEntry> ALL = List.of(BLOOD_POOL, LACERATION, BLOOD_SOAKED_QUILL, THE_OTHERWORLD, THE_RITUALISTS);

    public static JournalEntry byId(String id)
    {
        for (JournalEntry entry : ALL)
        {
            if (entry.getId().equals(id))
            {
                return entry;
            }
        }
        return null;
    }

    public static List<JournalEntry> byCategory(JournalCategory category)
    {
        List<JournalEntry> result = new ArrayList<>();
        for (JournalEntry entry : ALL)
        {
            if (entry.getCategory() == category)
            {
                result.add(entry);
            }
        }
        return result;
    }
}
