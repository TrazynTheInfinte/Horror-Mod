package com.horrormod.item;

import com.horrormod.HorrorMod;
import com.horrormod.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HorrorMod.MODID);

    public static final RegistryObject<Item> BLOOD_POOL = ITEMS.register("blood_pool",
            () -> new BlockItem(ModBlocks.BLOOD_POOL.get(), new Item.Properties()));

    public static final RegistryObject<Item> WORN_JOURNAL = ITEMS.register("worn_journal",
            () -> new WornJournalItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BLOOD_SOAKED_QUILL = ITEMS.register("blood_soaked_quill",
            () -> new BloodSoakedQuillItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> OTHERWORLD_STONE = ITEMS.register("otherworld_stone",
            () -> new BlockItem(ModBlocks.OTHERWORLD_STONE.get(), new Item.Properties()));

    public static final RegistryObject<Item> OTHERWORLD_GROUND = ITEMS.register("otherworld_ground",
            () -> new BlockItem(ModBlocks.OTHERWORLD_GROUND.get(), new Item.Properties()));

    public static final RegistryObject<Item> RITUALISTIC_DAGGER = ITEMS.register("ritualistic_dagger",
            () -> new RitualisticDaggerItem(new Item.Properties().stacksTo(1)));

    // Not obtainable -- exists only to hold the 3D model the Otherworldly Crystal entity renders with.
    public static final RegistryObject<Item> OTHERWORLDLY_CRYSTAL_DISPLAY = ITEMS.register("otherworldly_crystal_display",
            () -> new Item(new Item.Properties()));
}
