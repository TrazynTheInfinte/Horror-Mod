package com.horrormod.command;

import com.horrormod.HorrorMod;
import com.horrormod.dimension.OtherworldTravel;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// Temporary debug command for testing the Otherworld alongside the
// Ritualistic Dagger's Rifts. Op-only.
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
                    OtherworldTravel.teleportToOpposite(player);
                    return 1;
                }));
    }
}
