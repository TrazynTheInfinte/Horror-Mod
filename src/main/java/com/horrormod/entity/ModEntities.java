package com.horrormod.entity;

import com.horrormod.HorrorMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HorrorMod.MODID);

    public static final RegistryObject<EntityType<OtherworldlyCrystalEntity>> OTHERWORLDLY_CRYSTAL = ENTITY_TYPES.register("otherworldly_crystal",
            () -> EntityType.Builder.<OtherworldlyCrystalEntity>of(OtherworldlyCrystalEntity::new, MobCategory.MISC)
                    .sized(2.4f, 3.6f)
                    .clientTrackingRange(10)
                    .build("otherworldly_crystal"));
}
