package com.horrormod.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

// Purely decorative marker placed atop a Ritual Circle's obelisk. Floating
// and rotation are handled entirely client-side by the renderer using
// tickCount -- the entity itself never moves and has no gravity, collision,
// or interactions. Invulnerable and unpickable so nothing can remove it.
public class OtherworldlyCrystalEntity extends Entity
{
    public OtherworldlyCrystalEntity(EntityType<?> type, Level level)
    {
        super(type, level);
        this.noPhysics = true;
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    @Override
    protected void defineSynchedData()
    {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag)
    {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag)
    {
    }

    @Override
    public boolean isPickable()
    {
        return false;
    }

    @Override
    public boolean isPushable()
    {
        return false;
    }

    @Override
    public boolean isAttackable()
    {
        return false;
    }
}
