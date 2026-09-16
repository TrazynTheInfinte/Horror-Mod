package com.horrormod.entity;

import com.horrormod.HorrorMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = HorrorMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HorrorMod.MODID);

    public static final RegistryObject<EntityType<OtherworldlyCrystalEntity>> OTHERWORLDLY_CRYSTAL = ENTITY_TYPES.register("otherworldly_crystal",
            () -> EntityType.Builder.<OtherworldlyCrystalEntity>of(OtherworldlyCrystalEntity::new, MobCategory.MISC)
                    .sized(2.4f, 3.6f)
                    .clientTrackingRange(10)
                    .build("otherworldly_crystal"));

    public static final RegistryObject<EntityType<CultistEntity>> CULTIST = ENTITY_TYPES.register("cultist",
            () -> EntityType.Builder.of(CultistEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.95f)
                    .clientTrackingRange(8)
                    .build("cultist"));

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event)
    {
        event.put(CULTIST.get(), CultistEntity.createAttributes().build());
    }
}
