package com.horrormod.potion;

import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModBrewing
{
    public static void register(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(new LacerationBrewingRecipe()));
    }
}
