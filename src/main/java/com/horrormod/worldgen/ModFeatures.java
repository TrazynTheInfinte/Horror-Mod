package com.horrormod.worldgen;

import com.horrormod.HorrorMod;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures
{
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, HorrorMod.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> DEAD_TREE = FEATURES.register("dead_tree",
            () -> new DeadTreeFeature(NoneFeatureConfiguration.CODEC));
}
