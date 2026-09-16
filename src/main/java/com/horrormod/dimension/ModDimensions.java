package com.horrormod.dimension;

import com.horrormod.HorrorMod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ModDimensions
{
    public static final ResourceKey<Level> OTHERWORLD = ResourceKey.create(
            net.minecraft.core.registries.Registries.DIMENSION, new ResourceLocation(HorrorMod.MODID, "otherworld"));
}
