package com.horrormod.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

// Shared by the /otherworld debug command and the Rift block: sends the
// player to whichever of the Overworld/Otherworld they aren't currently in,
// landing on top of the terrain at the same X/Z.
public class OtherworldTravel
{
    public static void teleportToOpposite(ServerPlayer player)
    {
        ServerLevel currentLevel = player.serverLevel();
        ResourceKey<Level> targetKey = currentLevel.dimension() == ModDimensions.OTHERWORLD
                ? Level.OVERWORLD
                : ModDimensions.OTHERWORLD;

        ServerLevel targetLevel = player.getServer().getLevel(targetKey);
        if (targetLevel == null)
        {
            player.sendSystemMessage(Component.literal("That dimension isn't loaded."));
            return;
        }

        int x = (int) Math.floor(player.getX());
        int z = (int) Math.floor(player.getZ());

        // Force the destination chunk to fully generate before reading its terrain --
        // the cached heightmap isn't reliably populated yet on a chunk's first visit,
        // which was landing players near minY instead of on the surface.
        targetLevel.getChunk(x >> 4, z >> 4);

        int y = findSurfaceY(targetLevel, x, z);

        player.teleportTo(targetLevel, x + 0.5, y, z + 0.5, player.getYRot(), player.getXRot());
    }

    private static int findSurfaceY(ServerLevel level, int x, int z)
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, level.getMaxBuildHeight() - 1, z);
        while (pos.getY() > level.getMinBuildHeight())
        {
            if (!level.getBlockState(pos).isAir())
            {
                return pos.getY() + 1;
            }
            pos.move(Direction.DOWN);
        }
        return level.getMinBuildHeight() + 1;
    }
}
