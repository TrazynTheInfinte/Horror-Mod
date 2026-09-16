package com.horrormod.potion;

import com.horrormod.HorrorMod;
import com.horrormod.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions
{
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, HorrorMod.MODID);

    public static final RegistryObject<Potion> LACERATION = POTIONS.register("laceration",
            () -> new Potion("laceration", new MobEffectInstance(ModEffects.LACERATION.get(), 900)));
}
