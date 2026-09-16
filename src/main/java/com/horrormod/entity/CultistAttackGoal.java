package com.horrormod.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;

import java.util.EnumSet;

// One goal covering both Cultist variants, checking isRanged() live on every
// tick rather than being locked to whichever variant was picked at goal-list
// construction time -- so it can never end up mismatched with the equipped
// weapon (e.g. after a chunk unload/reload cycle re-runs registerGoals()).
//
// The ranged case fires a plain arrow directly rather than going through
// vanilla's CrossbowItem charge/fire state machine, which proved unreliable
// for this entity in testing (charges accumulated in the crossbow's
// ChargedProjectiles NBT across repeated cycles but were never released).
// The crossbow stays equipped purely for visuals.
public class CultistAttackGoal extends Goal
{
    private static final double RANGED_ATTACK_RANGE_SQR = 15.0 * 15.0;
    private static final double MELEE_REACH_SQR = 4.0;
    private static final int RANGED_COOLDOWN_TICKS = 40;
    private static final int MELEE_COOLDOWN_TICKS = 20;

    private final CultistEntity cultist;
    private int cooldown;

    public CultistAttackGoal(CultistEntity cultist)
    {
        this.cultist = cultist;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse()
    {
        LivingEntity target = cultist.getTarget();
        if (target == null || !target.isAlive())
        {
            return false;
        }
        double rangeSqr = cultist.isRanged() ? RANGED_ATTACK_RANGE_SQR : MELEE_REACH_SQR * 16.0;
        return cultist.distanceToSqr(target) <= rangeSqr;
    }

    @Override
    public boolean canContinueToUse()
    {
        return canUse();
    }

    @Override
    public boolean requiresUpdateEveryTick()
    {
        return true;
    }

    @Override
    public void start()
    {
        cooldown = 0;
    }

    @Override
    public void tick()
    {
        LivingEntity target = cultist.getTarget();
        if (target == null)
        {
            return;
        }

        cultist.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (cultist.isRanged())
        {
            tickRanged(target);
        }
        else
        {
            tickMelee(target);
        }
    }

    private void tickRanged(LivingEntity target)
    {
        double distanceSqr = cultist.distanceToSqr(target);
        cultist.getNavigation().moveTo(target, distanceSqr < 25.0 ? 0.3 : 0.8);

        if (cooldown > 0)
        {
            cooldown--;
            return;
        }
        if (!cultist.getSensing().hasLineOfSight(target) || !(cultist.level() instanceof ServerLevel))
        {
            return;
        }

        AbstractArrow arrow = new Arrow(cultist.level(), cultist);
        double dx = target.getX() - cultist.getX();
        double dy = target.getY(0.3333) - arrow.getY();
        double dz = target.getZ() - cultist.getZ();
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        arrow.shoot(dx, dy + horizontalDistance * 0.2, dz, 1.6F, 4.0F);
        arrow.setBaseDamage(4.0);

        cultist.level().addFreshEntity(arrow);
        cultist.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F);
        cooldown = RANGED_COOLDOWN_TICKS;
    }

    private void tickMelee(LivingEntity target)
    {
        cultist.getNavigation().moveTo(target, 1.0);

        if (cooldown > 0)
        {
            cooldown--;
            return;
        }
        if (cultist.distanceToSqr(target) > MELEE_REACH_SQR)
        {
            return;
        }

        cultist.doHurtTarget(target);
        cooldown = MELEE_COOLDOWN_TICKS;
    }
}
