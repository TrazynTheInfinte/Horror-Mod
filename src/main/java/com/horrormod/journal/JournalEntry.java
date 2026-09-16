package com.horrormod.journal;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class JournalEntry
{
    public enum TriggerType { ITEM, EFFECT }

    private final String id;
    private final Supplier<ItemStack> icon;
    private final TriggerType triggerType;
    private final Supplier<Item> triggerItem;
    private final Supplier<MobEffect> triggerEffect;

    private JournalEntry(String id, Supplier<ItemStack> icon, TriggerType triggerType,
                          Supplier<Item> triggerItem, Supplier<MobEffect> triggerEffect)
    {
        this.id = id;
        this.icon = icon;
        this.triggerType = triggerType;
        this.triggerItem = triggerItem;
        this.triggerEffect = triggerEffect;
    }

    public static JournalEntry forItem(String id, Supplier<Item> item)
    {
        return new JournalEntry(id, () -> new ItemStack(item.get()), TriggerType.ITEM, item, null);
    }

    public static JournalEntry forEffect(String id, Supplier<MobEffect> effect, Supplier<ItemStack> icon)
    {
        return new JournalEntry(id, icon, TriggerType.EFFECT, null, effect);
    }

    public String getId()
    {
        return id;
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
