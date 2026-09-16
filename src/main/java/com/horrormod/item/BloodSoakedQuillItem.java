package com.horrormod.item;

import com.horrormod.journal.JournalCategory;
import com.horrormod.journal.JournalData;
import com.horrormod.journal.JournalDataCapability;
import com.horrormod.journal.JournalEntries;
import com.horrormod.journal.JournalEntry;
import com.horrormod.network.HorrorModNetwork;
import com.horrormod.network.SyncJournalPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

// Writing with the quill costs blood (2 damage) and reveals the next
// undiscovered Lore journal entry, in order. Once every lore entry is
// discovered, using it again costs nothing and just says so.
public class BloodSoakedQuillItem extends Item
{
    public BloodSoakedQuillItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer)
        {
            serverPlayer.getCapability(JournalDataCapability.JOURNAL_DATA).ifPresent(data -> reveal(serverPlayer, data));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private void reveal(ServerPlayer player, JournalData data)
    {
        JournalEntry next = null;
        for (JournalEntry entry : JournalEntries.byCategory(JournalCategory.LORE))
        {
            if (!data.isDiscovered(entry.getId()))
            {
                next = entry;
                break;
            }
        }

        if (next == null)
        {
            player.displayClientMessage(Component.translatable("message.horrormod.quill_silent"), true);
            return;
        }

        data.discover(next.getId());
        player.hurt(player.damageSources().magic(), 2.0F);
        player.displayClientMessage(Component.translatable("message.horrormod.quill_reveal"), true);

        HorrorModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncJournalPacket(data.getDiscovered()));
    }
}
