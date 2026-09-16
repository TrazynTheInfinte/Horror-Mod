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
}
