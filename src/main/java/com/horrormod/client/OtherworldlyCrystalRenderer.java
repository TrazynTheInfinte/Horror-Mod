package com.horrormod.client;

import com.horrormod.item.ModItems;
import com.horrormod.entity.OtherworldlyCrystalEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

// Purely a floating/rotating visual -- the animation is computed here from
// tickCount rather than any real entity movement, matching how vanilla
// animates its own decorative entities.
public class OtherworldlyCrystalRenderer extends EntityRenderer<OtherworldlyCrystalEntity>
{
    private static final ResourceLocation DUMMY_TEXTURE = new ResourceLocation("minecraft", "textures/atlas/blocks.png");

    public OtherworldlyCrystalRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public void render(OtherworldlyCrystalEntity entity, float yaw, float partialTicks, PoseStack poseStack,
                        MultiBufferSource buffer, int packedLight)
    {
        float age = entity.tickCount + partialTicks;
        float bob = Mth.sin(age / 20.0F) * 0.1F + 0.3F;
        float rotation = age * 2.0F;

        poseStack.pushPose();
        poseStack.translate(0.0, bob, 0.0);
        poseStack.scale(1.5F, 1.5F, 1.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        ItemStack displayStack = new ItemStack(ModItems.OTHERWORLDLY_CRYSTAL_DISPLAY.get());
        Minecraft.getInstance().getItemRenderer().renderStatic(displayStack, ItemDisplayContext.FIXED,
                packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), 0);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(OtherworldlyCrystalEntity entity)
    {
        return DUMMY_TEXTURE;
    }
}
