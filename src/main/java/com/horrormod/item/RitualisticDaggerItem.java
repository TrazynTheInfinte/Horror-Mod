package com.horrormod.item;

import com.horrormod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

// Right-click a solid block face to spend the Blood Price (3 hearts) and open
// a Rift -- two stacked blocks in the clear space in front of that face.
// Reusable: the dagger itself is never consumed or damaged.
public class RitualisticDaggerItem extends Item
{
    private static final int RIFT_LIFETIME_TICKS = 600; // 30 seconds
    private static final float BLOOD_PRICE = 6.0F; // 3 hearts

    public RitualisticDaggerItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Level level = context.getLevel();
        if (level.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }

        Player player = context.getPlayer();
        BlockPos clickedPos = context.getClickedPos();
        Direction face = context.getClickedFace();

        if (!level.getBlockState(clickedPos).isFaceSturdy(level, clickedPos, face))
        {
            return fail(player, "message.horrormod.rift_no_surface");
        }

        BlockPos bottomPos = clickedPos.relative(face);
        BlockPos topPos = bottomPos.above();

        if (!level.getBlockState(bottomPos).isAir() || !level.getBlockState(topPos).isAir())
        {
            return fail(player, "message.horrormod.rift_no_space");
        }

        ServerLevel serverLevel = (ServerLevel) level;
        serverLevel.setBlockAndUpdate(bottomPos, ModBlocks.RIFT_BOTTOM.get().defaultBlockState());
        serverLevel.setBlockAndUpdate(topPos, ModBlocks.RIFT_TOP.get().defaultBlockState());
        serverLevel.scheduleTick(bottomPos, ModBlocks.RIFT_BOTTOM.get(), RIFT_LIFETIME_TICKS);
        serverLevel.scheduleTick(topPos, ModBlocks.RIFT_TOP.get(), RIFT_LIFETIME_TICKS);

        if (player != null)
        {
            player.hurt(player.damageSources().magic(), BLOOD_PRICE);
        }

        return InteractionResult.CONSUME;
    }

    private InteractionResult fail(Player player, String messageKey)
    {
        if (player != null)
        {
            player.displayClientMessage(Component.translatable(messageKey), true);
        }
        return InteractionResult.FAIL;
    }
}
