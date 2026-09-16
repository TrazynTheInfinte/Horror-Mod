package com.horrormod.potion;

import com.horrormod.item.ModItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;

// Awkward Potion (in any of the potion/splash/lingering containers) + Blood Pool -> Laceration,
// preserving whichever container the input potion was in.
public class LacerationBrewingRecipe implements IBrewingRecipe
{
    @Override
    public boolean isInput(ItemStack stack)
    {
        return (stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION))
                && PotionUtils.getPotion(stack) == Potions.AWKWARD;
    }

    @Override
    public boolean isIngredient(ItemStack stack)
    {
        return stack.is(ModItems.BLOOD_POOL.get());
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient)
    {
        if (!isInput(input) || !isIngredient(ingredient))
        {
            return ItemStack.EMPTY;
        }
        return PotionUtils.setPotion(new ItemStack(input.getItem()), ModPotions.LACERATION.get());
    }
}
