package com.horrormod.journal;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.HashSet;
import java.util.Set;

public class JournalData
{
    private final Set<String> discovered = new HashSet<>();

    public boolean isDiscovered(String id)
    {
        return discovered.contains(id);
    }

    // Returns true if this actually discovered something new.
    public boolean discover(String id)
    {
        return discovered.add(id);
    }

    public Set<String> getDiscovered()
    {
        return discovered;
    }

    public void setDiscovered(Set<String> ids)
    {
        discovered.clear();
        discovered.addAll(ids);
    }

    public CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (String id : discovered)
        {
            list.add(StringTag.valueOf(id));
        }
        tag.put("discovered", list);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag)
    {
        discovered.clear();
        ListTag list = tag.getList("discovered", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++)
        {
            discovered.add(list.getString(i));
        }
    }
}
