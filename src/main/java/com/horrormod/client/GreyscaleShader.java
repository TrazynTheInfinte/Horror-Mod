package com.horrormod.client;

import com.horrormod.HorrorMod;
import com.horrormod.dimension.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

// Applies the "everything looks washed-out and grey here" atmosphere of the Otherworld to the
// whole screen, GUI included -- vanilla's per-dimension post effects run before the GUI is drawn
// and never touch it, so this runs its own PostChain after RenderGuiEvent.Post instead, once the
// GUI has already been baked into the main render target.
@Mod.EventBusSubscriber(modid = HorrorMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class GreyscaleShader
{
    private static PostChain postChain;
    private static int lastWidth = -1;
    private static int lastHeight = -1;

    // The HUD (hotbar, crosshair, etc.) renders via RenderGuiEvent, but an open Screen
    // (inventory, journal, any container) renders separately, afterward -- so both are
    // hooked to guarantee whichever rendered last in a frame ends up desaturated. Running
    // this twice in one frame (HUD then screen) is harmless: greyscaling an already-grey
    // image is a no-op.
    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event)
    {
        apply(event.getPartialTick());
    }

    @SubscribeEvent
    public static void onScreenRenderPost(ScreenEvent.Render.Post event)
    {
        apply(1.0F);
    }

    private static void apply(float partialTick)
    {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null
                || minecraft.player.level().dimension() != ModDimensions.OTHERWORLD)
        {
            return;
        }

        PostChain chain = getOrLoadChain(minecraft);
        if (chain == null)
        {
            return;
        }

        int width = minecraft.getWindow().getWidth();
        int height = minecraft.getWindow().getHeight();
        if (width != lastWidth || height != lastHeight)
        {
            chain.resize(width, height);
            lastWidth = width;
            lastHeight = height;
        }

        chain.process(partialTick);
        minecraft.getMainRenderTarget().bindWrite(false);
    }

    private static PostChain getOrLoadChain(Minecraft minecraft)
    {
        if (postChain != null)
        {
            return postChain;
        }

        try
        {
            postChain = new PostChain(
                    minecraft.getTextureManager(),
                    minecraft.getResourceManager(),
                    minecraft.getMainRenderTarget(),
                    new ResourceLocation(HorrorMod.MODID, "greyscale"));
            lastWidth = minecraft.getWindow().getWidth();
            lastHeight = minecraft.getWindow().getHeight();
            postChain.resize(lastWidth, lastHeight);
        }
        catch (IOException e)
        {
            postChain = null;
        }

        return postChain;
    }
}
