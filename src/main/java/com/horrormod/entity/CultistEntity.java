package com.horrormod.entity;

import com.horrormod.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// The Ritualists, alive and present -- not descendants, not a separate group.
// Neutral: ignores players until one of them is attacked, at which point
// every Cultist at the site turns hostile to the attacker (HurtByTargetGoal's
// alertOthers). Variant (crossbow vs axe) is decided once, randomly, the
// moment goals are registered, since that's the only point construction-time
// state is guaranteed to be ready; the weapon itself is equipped afterward
// by whoever spawns the entity, matching whichever variant this rolled.
//
// Both variants share one CultistAttackGoal that checks isRanged() live
// rather than being locked to a variant at goal-registration time; the
// ranged case fires arrows directly rather than through vanilla's
// CrossbowItem charge/fire mechanic -- see that class for why.
public class CultistEntity extends Monster
{
    private static final EntityDataAccessor<Boolean> DATA_IS_RANGED = SynchedEntityData.defineId(CultistEntity.class, EntityDataSerializers.BOOLEAN);

    public CultistEntity(EntityType<? extends CultistEntity> type, Level level)
    {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE, 13.0)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_RANGED, false);
    }

    @Override
    protected void registerGoals()
    {
        // registerGoals() runs during construction, before any saved NBT is
        // applied, so this roll is only meaningful for a genuinely brand-new
        // Cultist. If this entity is being reloaded from disk instead,
        // readAdditionalSaveData() below overwrites it with the persisted
        // value afterward -- CultistAttackGoal reads isRanged() live on every
        // tick rather than caching a choice, so that correction is enough to
        // keep AI behavior and the equipped weapon (which persists via
        // standard equipment NBT regardless) from ever disagreeing.
        this.entityData.set(DATA_IS_RANGED, this.random.nextBoolean());

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CultistAttackGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        // setAlertOthers takes classes to EXCLUDE from the alert, not include --
        // no-args alerts every other entity of this same class within range.
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsRanged", this.entityData.get(DATA_IS_RANGED));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);
        if (tag.contains("IsRanged"))
        {
            this.entityData.set(DATA_IS_RANGED, tag.getBoolean("IsRanged"));
        }
    }

    public boolean isRanged()
    {
        return this.entityData.get(DATA_IS_RANGED);
    }

    // Called once, right after spawning, by whatever placed this Cultist --
    // gives it the weapon matching whichever variant registerGoals() rolled.
    public void equipStartingWeapon()
    {
        ItemStack weapon = isRanged()
                ? new ItemStack(ModItems.CULTIST_CROSSBOW.get())
                : new ItemStack(ModItems.CULTIST_AXE.get());
        this.setItemSlot(EquipmentSlot.MAINHAND, weapon);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.15F);
    }
}
