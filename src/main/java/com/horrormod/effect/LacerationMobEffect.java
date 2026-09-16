package com.horrormod.effect;

import com.horrormod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

// Functionally identical to vanilla Poison (same damage, same tick cadence,
// won't drop an entity below 1 health), but also drops a blood pool at the
// entity's feet each time it ticks.
public class LacerationMobEffect extends MobEffect
{
    public LacerationMobEffect()
    {
        super(MobEffectCategory.HARMFUL, 0x8B0000);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier)
    {
        if (entity.getHealth() > 1.0F)
        {
            entity.hurt(entity.damageSources().magic(), 1.0F);
        }

        Level level = entity.level();
        if (!level.isClientSide)
        {
            BlockPos pos = entity.blockPosition();
            BlockState bloodPool = ModBlocks.BLOOD_POOL.get().defaultBlockState();
            if (level.getBlockState(pos).isAir() && bloodPool.canSurvive(level, pos))
            {
                level.setBlockAndUpdate(pos, bloodPool);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier)
    {
        int i = 25 >> amplifier;
        return i > 0 ? duration % i == 0 : true;
    }
}
