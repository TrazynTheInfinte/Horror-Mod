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
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

// Right-click the ground (a block's top face) to spend the Blood Price
// (3 hearts) and tear open a Rift -- a two-block-long fissure lying flush
// on that surface, running away from the player in whichever horizontal
// direction they're facing. Reusable: the dagger itself is never consumed
// or damaged.
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
        Direction clickedFace = context.getClickedFace();

        if (clickedFace != Direction.UP || !level.getBlockState(clickedPos).isFaceSturdy(level, clickedPos, clickedFace))
        {
            return fail(player, "message.horrormod.rift_no_surface");
        }

        Direction facing = player != null ? player.getDirection() : Direction.NORTH;
        BlockPos nearPos = clickedPos.above();
        BlockPos farPos = nearPos.relative(facing);
        BlockPos farGroundPos = farPos.below();

        boolean farGroundSturdy = level.getBlockState(farGroundPos).isFaceSturdy(level, farGroundPos, Direction.UP);
        if (!level.getBlockState(nearPos).isAir() || !level.getBlockState(farPos).isAir() || !farGroundSturdy)
        {
            return fail(player, "message.horrormod.rift_no_space");
        }

        ServerLevel serverLevel = (ServerLevel) level;
        BlockState nearState = ModBlocks.RIFT_NEAR.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, facing);
        BlockState farState = ModBlocks.RIFT_FAR.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, facing);

        serverLevel.setBlockAndUpdate(nearPos, nearState);
        serverLevel.setBlockAndUpdate(farPos, farState);
        serverLevel.scheduleTick(nearPos, ModBlocks.RIFT_NEAR.get(), RIFT_LIFETIME_TICKS);
        serverLevel.scheduleTick(farPos, ModBlocks.RIFT_FAR.get(), RIFT_LIFETIME_TICKS);

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
