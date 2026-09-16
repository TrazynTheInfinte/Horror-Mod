package com.horrormod.client;

import com.horrormod.HorrorMod;
import com.horrormod.entity.CultistEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class CultistRenderer extends MobRenderer<CultistEntity, CultistModel>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(HorrorMod.MODID, "textures/entity/cultist.png");

    public CultistRenderer(EntityRendererProvider.Context context)
    {
        super(context, new CultistModel(context.bakeLayer(ModClientLayers.CULTIST)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(CultistEntity entity)
    {
        return TEXTURE;
    }
}
