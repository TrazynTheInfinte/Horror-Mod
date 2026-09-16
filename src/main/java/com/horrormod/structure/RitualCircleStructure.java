package com.horrormod.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class RitualCircleStructure extends Structure
{
    public static final Codec<RitualCircleStructure> CODEC = simpleCodec(RitualCircleStructure::new);

    public RitualCircleStructure(StructureSettings settings)
    {
        super(settings);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context)
    {
        ChunkPos chunkPos = context.chunkPos();
        int centerX = chunkPos.getMiddleBlockX();
        int centerZ = chunkPos.getMiddleBlockZ();

        ChunkGenerator generator = context.chunkGenerator();
        LevelHeightAccessor heightAccessor = context.heightAccessor();
        RandomState randomState = context.randomState();

        int obeliskGroundY = generator.getFirstOccupiedHeight(centerX, centerZ, Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);

        Holder<Biome> biome = context.biomeSource().getNoiseBiome(
                QuartPos.fromBlock(centerX), QuartPos.fromBlock(obeliskGroundY), QuartPos.fromBlock(centerZ), randomState.sampler());
        if (biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_DEEP_OCEAN))
        {
            return Optional.empty();
        }

        RandomSource random = context.random();
        int obeliskHeight = 9 + random.nextInt(4);
        int ringRadius = 5 + random.nextInt(3);
        int spireCount = 6 + random.nextInt(3);

        int[] spireHeights = new int[spireCount];
        int[] spireX = new int[spireCount];
        int[] spireZ = new int[spireCount];
        for (int i = 0; i < spireCount; i++)
        {
            spireHeights[i] = 3 + random.nextInt(3);
            double angle = 2 * Math.PI * i / spireCount;
            spireX[i] = centerX + (int) Math.round(Math.cos(angle) * ringRadius);
            spireZ[i] = centerZ + (int) Math.round(Math.sin(angle) * ringRadius);
        }

        int[] spireGroundY = new int[spireCount];
        int sharedBaseY = obeliskGroundY;
        for (int i = 0; i < spireCount; i++)
        {
            spireGroundY[i] = generator.getFirstOccupiedHeight(spireX[i], spireZ[i], Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, randomState);
            sharedBaseY = Math.max(sharedBaseY, spireGroundY[i]);
        }

        BlockPos center = new BlockPos(centerX, sharedBaseY, centerZ);

        return Optional.of(new GenerationStub(center, piecesBuilder ->
                piecesBuilder.addPiece(new RitualCirclePiece(center, obeliskHeight, ringRadius, spireHeights, obeliskGroundY, spireGroundY))));
    }

    @Override
    public StructureType<?> type()
    {
        return ModStructures.RITUAL_CIRCLE.get();
    }
}
