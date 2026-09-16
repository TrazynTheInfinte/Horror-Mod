package com.horrormod.client;

import com.horrormod.journal.JournalCategory;
import com.horrormod.journal.JournalEntries;
import com.horrormod.journal.JournalEntry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JournalScreen extends Screen
{
    private static final int IMAGE_WIDTH = 192;
    private static final int IMAGE_HEIGHT = 192;

    private JournalCategory selectedCategory;
    private int currentPage = 0;

    private PageButton forwardButton;
    private PageButton backButton;
    private final List<Button> categoryButtons = new ArrayList<>();

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

        categoryButtons.clear();
        int buttonY = top + 20;
        for (JournalCategory category : JournalCategory.values())
        {
            Button button = this.addRenderableWidget(Button.builder(Component.translatable(category.getTitleKey()),
                            b -> selectCategory(category))
                    .bounds(left + 36, buttonY, 114, 14)
                    .build());
            categoryButtons.add(button);
            buttonY += 16;
        }

        refreshWidgets();
    }

    private void selectCategory(JournalCategory category)
    {
        this.selectedCategory = category;
        this.currentPage = 0;
        refreshWidgets();
    }

    private List<JournalEntry> currentEntries()
    {
        return selectedCategory == null ? Collections.emptyList() : JournalEntries.byCategory(selectedCategory);
    }

    private void pageForward()
    {
        if (currentPage < currentEntries().size() - 1)
        {
            currentPage++;
            refreshWidgets();
        }
    }

    private void pageBack()
    {
        if (currentPage > 0)
        {
            currentPage--;
        }
        else
        {
            selectedCategory = null;
        }
        refreshWidgets();
    }

    private void refreshWidgets()
    {
        boolean inIndex = selectedCategory == null;
        for (Button button : categoryButtons)
        {
            button.visible = inIndex;
        }
        this.backButton.visible = !inIndex;
        this.forwardButton.visible = !inIndex && currentPage < currentEntries().size() - 1;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        this.renderBackground(guiGraphics);

        int left = (this.width - IMAGE_WIDTH) / 2;
        int top = (this.height - IMAGE_HEIGHT) / 2;

        guiGraphics.blit(BookViewScreen.BOOK_LOCATION, left, top, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        if (selectedCategory == null)
        {
            guiGraphics.drawCenteredString(this.font, Component.translatable("journal.horrormod.contents"),
                    left + IMAGE_WIDTH / 2, top + 10, 0x3F3F3F);
        }
        else
        {
            renderCategoryPage(guiGraphics, left, top);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderCategoryPage(GuiGraphics guiGraphics, int left, int top)
    {
        int textLeft = left + 36;
        int textTop = top + 30;

        guiGraphics.drawCenteredString(this.font, Component.translatable(selectedCategory.getTitleKey()),
                left + IMAGE_WIDTH / 2, top + 10, 0x3F3F3F);

        List<JournalEntry> entries = currentEntries();
        if (entries.isEmpty())
        {
            guiGraphics.drawWordWrap(this.font, Component.translatable("journal.horrormod.empty_category"), textLeft, textTop, 114, 0x777777);
            return;
        }

        JournalEntry entry = entries.get(currentPage);
        boolean discovered = ClientJournalData.isDiscovered(entry.getId());

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

        guiGraphics.drawCenteredString(this.font, (currentPage + 1) + " / " + entries.size(), left + IMAGE_WIDTH / 2, top + 174, 0x3F3F3F);
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }
}
