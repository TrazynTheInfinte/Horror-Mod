package com.horrormod.journal;

import com.horrormod.HorrorMod;
import com.horrormod.network.HorrorModNetwork;
import com.horrormod.network.SyncJournalPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = HorrorMod.MODID)
public class JournalEvents
{
    private static final ResourceLocation CAPABILITY_ID = new ResourceLocation(HorrorMod.MODID, "journal_data");

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof Player)
        {
            event.addCapability(CAPABILITY_ID, new JournalDataProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event)
    {
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(JournalDataCapability.JOURNAL_DATA).ifPresent(oldData ->
                event.getEntity().getCapability(JournalDataCapability.JOURNAL_DATA).ifPresent(newData ->
                        newData.setDiscovered(oldData.getDiscovered())));
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer serverPlayer)
        {
            syncToClient(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        if (event.getEntity() instanceof ServerPlayer serverPlayer)
        {
            syncToClient(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer))
        {
            return;
        }

        String dimensionPath = event.getTo().location().getPath();
        serverPlayer.getCapability(JournalDataCapability.JOURNAL_DATA).ifPresent(data -> {
            for (JournalEntry entry : JournalEntries.ALL)
            {
                if (entry.getTriggerType() == JournalEntry.TriggerType.DIMENSION && entry.getId().equals(dimensionPath))
                {
                    data.discover(entry.getId());
                }
            }
        });

        syncToClient(serverPlayer);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide)
        {
            return;
        }
        if (event.player.tickCount % 20 != 0 || !(event.player instanceof ServerPlayer serverPlayer))
        {
            return;
        }

        serverPlayer.getCapability(JournalDataCapability.JOURNAL_DATA).ifPresent(data -> {
            boolean unlockedSomething = false;
            for (JournalEntry entry : JournalEntries.ALL)
            {
                if (entry.getTriggerType() != JournalEntry.TriggerType.ITEM || data.isDiscovered(entry.getId()))
                {
                    continue;
                }
                if (serverPlayer.getInventory().contains(new ItemStack(entry.getTriggerItem())))
                {
                    unlockedSomething |= data.discover(entry.getId());
                }
            }

            for (JournalEntry entry : JournalEntries.ALL)
            {
                if (entry.getTriggerType() != JournalEntry.TriggerType.STRUCTURE || data.isDiscovered(entry.getId()))
                {
                    continue;
                }
                ResourceKey<Structure> structureKey = ResourceKey.create(Registries.STRUCTURE,
                        new ResourceLocation(HorrorMod.MODID, entry.getId()));
                if (serverPlayer.serverLevel().structureManager()
                        .getStructureWithPieceAt(serverPlayer.blockPosition(), structureKey).isValid())
                {
                    unlockedSomething |= data.discover(entry.getId());
                }
            }

            for (JournalEntry entry : JournalEntries.ALL)
            {
                if (entry.getTriggerType() != JournalEntry.TriggerType.ENTITY || data.isDiscovered(entry.getId()))
                {
                    continue;
                }
                EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(HorrorMod.MODID, entry.getId()));
                if (entityType == null)
                {
                    continue;
                }
                AABB nearby = serverPlayer.getBoundingBox().inflate(16.0);
                if (!serverPlayer.level().getEntities(entityType, nearby, e -> true).isEmpty())
                {
                    unlockedSomething |= data.discover(entry.getId());
                }
            }

            if (unlockedSomething)
            {
                syncToClient(serverPlayer);
            }
        });
    }

    @SubscribeEvent
    public static void onMobEffectAdded(MobEffectEvent.Added event)
    {
        if (event.getEntity().level().isClientSide || !(event.getEntity() instanceof ServerPlayer serverPlayer))
        {
            return;
        }

        serverPlayer.getCapability(JournalDataCapability.JOURNAL_DATA).ifPresent(data -> {
            boolean unlockedSomething = false;
            for (JournalEntry entry : JournalEntries.ALL)
            {
                if (entry.getTriggerType() != JournalEntry.TriggerType.EFFECT || data.isDiscovered(entry.getId()))
                {
                    continue;
                }
                if (entry.getTriggerEffect() == event.getEffectInstance().getEffect())
                {
                    unlockedSomething |= data.discover(entry.getId());
                }
            }
            if (unlockedSomething)
            {
                syncToClient(serverPlayer);
            }
        });
    }

    private static void syncToClient(ServerPlayer player)
    {
        player.getCapability(JournalDataCapability.JOURNAL_DATA).ifPresent(data ->
                HorrorModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                        new SyncJournalPacket(data.getDiscovered())));
    }
}
