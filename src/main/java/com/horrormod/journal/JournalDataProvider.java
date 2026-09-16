package com.horrormod.journal;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JournalDataProvider implements ICapabilitySerializable<CompoundTag>
{
    private final JournalData data = new JournalData();
    private final LazyOptional<JournalData> optional = LazyOptional.of(() -> data);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        return cap == JournalDataCapability.JOURNAL_DATA ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        return data.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag tag)
    {
        data.deserializeNBT(tag);
    }
}
