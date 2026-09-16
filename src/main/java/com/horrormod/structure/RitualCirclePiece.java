package com.horrormod.structure;

import com.horrormod.block.ModBlocks;
import com.horrormod.entity.CultistEntity;
import com.horrormod.entity.ModEntities;
import com.horrormod.entity.OtherworldlyCrystalEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

// Places a central obelisk plus a ring of smaller spires around it, with a
// handful of Blood Pools scattered on the ground within the ring. Everything
// sits flush on the highest ground point sampled across the footprint
// (RitualCircleStructure), with foundation blocks filling down to the real
// terrain under any column whose local ground is lower -- so the whole ring
// stays level and nothing is embedded in a hill or left floating.
public class RitualCirclePiece extends StructurePiece
{
    private final int obeliskHeight;
    private final int ringRadius;
    private final int[] spireHeights;
    private final int obeliskGroundY;
    private final int[] spireGroundY;

    public RitualCirclePiece(BlockPos center, int obeliskHeight, int ringRadius, int[] spireHeights,
                              int obeliskGroundY, int[] spireGroundY)
    {
        super(ModStructurePieces.RITUAL_CIRCLE.get(), 0, makeBoundingBox(center, obeliskHeight, ringRadius, obeliskGroundY, spireGroundY));
        this.obeliskHeight = obeliskHeight;
        this.ringRadius = ringRadius;
        this.spireHeights = spireHeights;
        this.obeliskGroundY = obeliskGroundY;
        this.spireGroundY = spireGroundY;
    }

    public RitualCirclePiece(CompoundTag tag)
    {
        super(ModStructurePieces.RITUAL_CIRCLE.get(), tag);
        this.obeliskHeight = tag.getInt("ObeliskHeight");
        this.ringRadius = tag.getInt("RingRadius");
        this.spireHeights = tag.getIntArray("SpireHeights");
        this.obeliskGroundY = tag.getInt("ObeliskGroundY");
        this.spireGroundY = tag.getIntArray("SpireGroundY");
    }

    private static BoundingBox makeBoundingBox(BlockPos center, int obeliskHeight, int ringRadius, int obeliskGroundY, int[] spireGroundY)
    {
        int margin = ringRadius + 2;
        int lowestGround = obeliskGroundY;
        for (int y : spireGroundY)
        {
            lowestGround = Math.min(lowestGround, y);
        }
        return new BoundingBox(center.getX() - margin, lowestGround - 1, center.getZ() - margin,
                center.getX() + margin, center.getY() + obeliskHeight + 1, center.getZ() + margin);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
    {
        tag.putInt("ObeliskHeight", obeliskHeight);
        tag.putInt("RingRadius", ringRadius);
        tag.put("SpireHeights", new IntArrayTag(spireHeights));
        tag.putInt("ObeliskGroundY", obeliskGroundY);
        tag.put("SpireGroundY", new IntArrayTag(spireGroundY));
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator,
                             RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos piecePos)
    {
        int centerX = (this.boundingBox.minX() + this.boundingBox.maxX()) / 2;
        int centerZ = (this.boundingBox.minZ() + this.boundingBox.maxZ()) / 2;
        int baseY = this.boundingBox.maxY() - obeliskHeight - 1;

        BlockState obeliskBlock = Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState();
        BlockState spireBlock = Blocks.CHISELED_DEEPSLATE.defaultBlockState();
        BlockState bloodPool = ModBlocks.BLOOD_POOL.get().defaultBlockState();

        // Obelisk foundation (fills any gap between its real ground and the shared base) + the obelisk itself.
        for (int dx = 0; dx < 2; dx++)
        {
            for (int dz = 0; dz < 2; dz++)
            {
                for (int y = obeliskGroundY; y < baseY; y++)
                {
                    placeBlock(level, obeliskBlock, centerX + dx, y, centerZ + dz, chunkBox);
                }
            }
        }
        for (int y = 0; y < obeliskHeight; y++)
        {
            for (int dx = 0; dx < 2; dx++)
            {
                for (int dz = 0; dz < 2; dz++)
                {
                    placeBlock(level, obeliskBlock, centerX + dx, baseY + y, centerZ + dz, chunkBox);
                }
            }
        }

        int spireCount = spireHeights.length;
        for (int i = 0; i < spireCount; i++)
        {
            double angle = 2 * Math.PI * i / spireCount;
            int sx = centerX + (int) Math.round(Math.cos(angle) * ringRadius);
            int sz = centerZ + (int) Math.round(Math.sin(angle) * ringRadius);

            for (int y = spireGroundY[i]; y < baseY; y++)
            {
                placeBlock(level, spireBlock, sx, y, sz, chunkBox);
            }

            int height = spireHeights[i];
            for (int y = 0; y < height; y++)
            {
                placeBlock(level, spireBlock, sx, baseY + y, sz, chunkBox);
            }
        }

        RandomSource decorationRandom = RandomSource.create(((long) centerX << 32) ^ centerZ);
        int poolCount = 3 + decorationRandom.nextInt(4);
        for (int i = 0; i < poolCount; i++)
        {
            double angle = decorationRandom.nextDouble() * Math.PI * 2;
            int r = 2 + decorationRandom.nextInt(Math.max(1, ringRadius - 2));
            int px = centerX + (int) Math.round(Math.cos(angle) * r);
            int pz = centerZ + (int) Math.round(Math.sin(angle) * r);
            placeBlock(level, bloodPool, px, baseY, pz, chunkBox);
        }

        BlockPos obeliskTop = new BlockPos(centerX, baseY + obeliskHeight, centerZ);
        if (chunkBox.isInside(obeliskTop))
        {
            OtherworldlyCrystalEntity crystal = new OtherworldlyCrystalEntity(ModEntities.OTHERWORLDLY_CRYSTAL.get(), level.getLevel());
            crystal.moveTo(centerX + 1.0, obeliskTop.getY() + 0.5, centerZ + 1.0, 0.0F, 0.0F);
            level.addFreshEntity(crystal);
        }

        // Fixed, one-time population -- nothing respawns here once cleared.
        // Spawned at the shared plaza height like the Blood Pools; on strongly
        // uneven interior ground (between the leveled obelisk/spire columns)
        // a Cultist can end up slightly embedded or floating, same tradeoff
        // the Blood Pool decoration already accepts.
        int cultistCount = 3 + decorationRandom.nextInt(6);
        for (int i = 0; i < cultistCount; i++)
        {
            double angle = decorationRandom.nextDouble() * Math.PI * 2;
            int r = 1 + decorationRandom.nextInt(Math.max(1, ringRadius));
            int cx = centerX + (int) Math.round(Math.cos(angle) * r);
            int cz = centerZ + (int) Math.round(Math.sin(angle) * r);

            if (chunkBox.isInside(new BlockPos(cx, baseY, cz)))
            {
                CultistEntity cultist = new CultistEntity(ModEntities.CULTIST.get(), level.getLevel());
                cultist.moveTo(cx + 0.5, baseY, cz + 0.5, decorationRandom.nextFloat() * 360.0F, 0.0F);
                cultist.equipStartingWeapon();
                cultist.setPersistenceRequired();
                level.addFreshEntity(cultist);
            }
        }
    }
}
