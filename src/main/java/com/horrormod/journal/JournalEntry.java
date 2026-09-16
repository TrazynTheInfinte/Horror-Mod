package com.horrormod.journal;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class JournalEntry
{
    public enum TriggerType { ITEM, EFFECT, MANUAL }

    private final String id;
    private final JournalCategory category;
    private final Supplier<ItemStack> icon;
    private final TriggerType triggerType;
    private final Supplier<Item> triggerItem;
    private final Supplier<MobEffect> triggerEffect;

    private JournalEntry(String id, JournalCategory category, Supplier<ItemStack> icon, TriggerType triggerType,
                          Supplier<Item> triggerItem, Supplier<MobEffect> triggerEffect)
    {
        this.id = id;
        this.category = category;
        this.icon = icon;
        this.triggerType = triggerType;
        this.triggerItem = triggerItem;
        this.triggerEffect = triggerEffect;
    }

    public static JournalEntry forItem(String id, JournalCategory category, Supplier<Item> item)
    {
        return new JournalEntry(id, category, () -> new ItemStack(item.get()), TriggerType.ITEM, item, null);
    }

    public static JournalEntry forEffect(String id, JournalCategory category, Supplier<MobEffect> effect, Supplier<ItemStack> icon)
    {
        return new JournalEntry(id, category, icon, TriggerType.EFFECT, null, effect);
    }

    // Discovered only by explicit game logic (e.g. an item's use effect), not by passive scanning.
    public static JournalEntry manual(String id, JournalCategory category, Supplier<ItemStack> icon)
    {
        return new JournalEntry(id, category, icon, TriggerType.MANUAL, null, null);
    }

    public String getId()
    {
        return id;
    }

    public JournalCategory getCategory()
    {
        return category;
    }

    public String getTitleKey()
    {
        return "journal.horrormod." + id + ".title";
    }

    public String getDescriptionKey()
    {
        return "journal.horrormod." + id + ".description";
    }

    public ItemStack getIcon()
    {
        return icon.get();
    }

    public TriggerType getTriggerType()
    {
        return triggerType;
    }

    public Item getTriggerItem()
    {
        return triggerItem == null ? null : triggerItem.get();
    }

    public MobEffect getTriggerEffect()
    {
        return triggerEffect == null ? null : triggerEffect.get();
    }
}
