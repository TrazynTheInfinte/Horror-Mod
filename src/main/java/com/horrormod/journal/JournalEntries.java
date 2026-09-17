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

    public static final JournalEntry OTHERWORLD_STONE = JournalEntry.forItem("otherworld_stone", JournalCategory.ITEMS, ModItems.OTHERWORLD_STONE);

    public static final JournalEntry OTHERWORLD_GROUND = JournalEntry.forItem("otherworld_ground", JournalCategory.ITEMS, ModItems.OTHERWORLD_GROUND);

    // id must match the dimension's registry path (horrormod:otherworld) -- see JournalEvents.
    public static final JournalEntry OTHERWORLD_DIMENSION = JournalEntry.forDimension("otherworld", JournalCategory.DIMENSIONS,
            () -> new ItemStack(ModItems.OTHERWORLD_GROUND.get()));

    public static final JournalEntry DEAD_LOG = JournalEntry.forItem("dead_log", JournalCategory.ITEMS, ModItems.DEAD_LOG);

    public static final JournalEntry RITUALISTIC_DAGGER = JournalEntry.forItem("ritualistic_dagger", JournalCategory.ITEMS, ModItems.RITUALISTIC_DAGGER);

    // id must match the structure's registry path (horrormod:ritual_circle) -- see JournalEvents.
    public static final JournalEntry RITUAL_CIRCLE = JournalEntry.forStructure("ritual_circle", JournalCategory.STRUCTURES,
            () -> new ItemStack(Items.POLISHED_BLACKSTONE_BRICKS));

    public static final JournalEntry CULTIST_CROSSBOW = JournalEntry.forItem("cultist_crossbow", JournalCategory.ITEMS, ModItems.CULTIST_CROSSBOW);

    public static final JournalEntry CULTIST_AXE = JournalEntry.forItem("cultist_axe", JournalCategory.ITEMS, ModItems.CULTIST_AXE);

    // id must match the entity's registry path (horrormod:cultist) -- see JournalEvents.
    public static final JournalEntry CULTIST = JournalEntry.forEntity("cultist", JournalCategory.ENTITIES,
            () -> new ItemStack(ModItems.CULTIST_AXE.get()));

    public static final List<JournalEntry> ALL = List.of(BLOOD_POOL, LACERATION, BLOOD_SOAKED_QUILL, THE_OTHERWORLD, THE_RITUALISTS,
            OTHERWORLD_STONE, OTHERWORLD_GROUND, DEAD_LOG, OTHERWORLD_DIMENSION, RITUALISTIC_DAGGER, RITUAL_CIRCLE,
            CULTIST_CROSSBOW, CULTIST_AXE, CULTIST);

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
