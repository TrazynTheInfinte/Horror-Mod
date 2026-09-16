package com.horrormod.effect;

import com.horrormod.HorrorMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects
{
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, HorrorMod.MODID);

    public static final RegistryObject<MobEffect> LACERATION = MOB_EFFECTS.register("laceration", LacerationMobEffect::new);
}
