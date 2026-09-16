package com.horrormod.client;

import com.horrormod.journal.JournalEntries;
import com.horrormod.journal.JournalEntry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.Component;

public class JournalScreen extends Screen
{
    private static final int IMAGE_WIDTH = 192;
    private static final int IMAGE_HEIGHT = 192;

    private int currentPage = 0;
    private PageButton forwardButton;
    private PageButton backButton;

    public JournalScreen()
    {
        super(Component.translatable("gui.horrormod.journal.title"));
    }

    @Override
    protected void init()
    {
        int left = (this.width - IMAGE_WIDTH) / 2;
        int top = (this.height - IMAGE_HEIGHT) / 2;

        this.forwardButton = this.addRenderableWidget(new PageButton(left + 116, top + 159, true, button -> pageForward(), true));
        this.backButton = this.addRenderableWidget(new PageButton(left + 43, top + 159, false, button -> pageBack(), true));

        updateButtons();
    }

    private void pageForward()
    {
        if (currentPage < JournalEntries.ALL.size() - 1)
        {
            currentPage++;
            updateButtons();
        }
    }

    private void pageBack()
    {
        if (currentPage > 0)
        {
            currentPage--;
            updateButtons();
        }
    }

    private void updateButtons()
    {
        this.backButton.visible = currentPage > 0;
        this.forwardButton.visible = currentPage < JournalEntries.ALL.size() - 1;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        this.renderBackground(guiGraphics);

        int left = (this.width - IMAGE_WIDTH) / 2;
        int top = (this.height - IMAGE_HEIGHT) / 2;

        guiGraphics.blit(BookViewScreen.BOOK_LOCATION, left, top, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        JournalEntry entry = JournalEntries.ALL.get(currentPage);
        boolean discovered = ClientJournalData.isDiscovered(entry.getId());

        int textLeft = left + 36;
        int textTop = top + 30;

        if (discovered)
        {
            guiGraphics.renderItem(entry.getIcon(), textLeft, textTop);
            guiGraphics.drawWordWrap(this.font, Component.translatable(entry.getTitleKey()), textLeft + 20, textTop + 4, 94, 0x3F3F3F);
            guiGraphics.drawWordWrap(this.font, Component.translatable(entry.getDescriptionKey()), textLeft, textTop + 24, 114, 0x3F3F3F);
        }
        else
        {
            guiGraphics.drawWordWrap(this.font, Component.translatable("journal.horrormod.undiscovered.title"), textLeft, textTop, 114, 0x3F3F3F);
            guiGraphics.drawWordWrap(this.font, Component.translatable("journal.horrormod.undiscovered.description"), textLeft, textTop + 24, 114, 0x777777);
        }

        guiGraphics.drawCenteredString(this.font, (currentPage + 1) + " / " + JournalEntries.ALL.size(), left + IMAGE_WIDTH / 2, top + 174, 0x3F3F3F);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
