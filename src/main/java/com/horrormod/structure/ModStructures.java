package com.horrormod.structure;

import com.horrormod.HorrorMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures
{
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, HorrorMod.MODID);

    public static final RegistryObject<StructureType<RitualCircleStructure>> RITUAL_CIRCLE = STRUCTURE_TYPES.register("ritual_circle",
            () -> () -> RitualCircleStructure.CODEC);
}
