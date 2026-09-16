package com.horrormod.command;

import com.horrormod.HorrorMod;
import com.horrormod.dimension.ModDimensions;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// Temporary debug command for testing the Otherworld before a real travel
// mechanism (a rift, per the lore) is built. Op-only.
@Mod.EventBusSubscriber(modid = HorrorMod.MODID)
public class OtherworldCommand
{
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event)
    {
        register(event.getDispatcher());
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("otherworld")
                .requires(source -> source.hasPermission(2))
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    teleport(player);
                    return 1;
                }));
    }

    private static void teleport(ServerPlayer player)
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

        double x = player.getX();
        double z = player.getZ();
        int y = targetLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, (int) x, (int) z) + 1;

        player.teleportTo(targetLevel, x, y, z, player.getYRot(), player.getXRot());
    }
}
