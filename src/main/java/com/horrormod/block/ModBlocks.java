package com.horrormod.block;

import com.horrormod.HorrorMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HorrorMod.MODID);

    public static final RegistryObject<Block> BLOOD_POOL = BLOCKS.register("blood_pool",
            () -> new BloodPoolBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noCollission()
                    .noOcclusion()
                    .strength(0.1f)
                    .sound(SoundType.SLIME_BLOCK)));

    public static final RegistryObject<Block> OTHERWORLD_STONE = BLOCKS.register("otherworld_stone",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(1.5f, 6.0f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> OTHERWORLD_GROUND = BLOCKS.register("otherworld_ground",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .strength(0.6f)
                    .sound(SoundType.NETHERRACK)));
}
