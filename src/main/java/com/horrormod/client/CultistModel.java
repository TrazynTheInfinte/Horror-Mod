package com.horrormod.client;

import com.horrormod.entity.CultistEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

// A robed humanoid figure authored directly for this mod -- head, a
// full-length robe that hides the legs entirely, and two arms. Not a reuse
// of vanilla's HumanoidModel skeleton/proportions.
public class CultistModel extends EntityModel<CultistEntity> implements ArmedModel
{
    private final ModelPart head;
    private final ModelPart robe;
    private final ModelPart rightArm;
    private final ModelPart leftArm;

    public CultistModel(ModelPart root)
    {
        this.head = root.getChild("head");
        this.robe = root.getChild("robe");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4, -8, -4, 8, 8, 8),
                PartPose.offset(0, 0, 0));

        root.addOrReplaceChild("robe",
                CubeListBuilder.create().texOffs(0, 16).addBox(-5, 0, -3, 10, 22, 6),
                PartPose.offset(0, 0, 0));

        root.addOrReplaceChild("right_arm",
                CubeListBuilder.create().texOffs(32, 0).addBox(-2, -2, -2, 4, 20, 4),
                PartPose.offset(-6, 2, 0));

        root.addOrReplaceChild("left_arm",
                CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-2, -2, -2, 4, 20, 4),
                PartPose.offset(6, 2, 0));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(CultistEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);

        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F) * 0.6F * limbSwingAmount;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.6F * limbSwingAmount;

        if (this.attackTime > 0.0F)
        {
            this.rightArm.xRot = -1.5F + 0.5F * Mth.sin(this.attackTime * (float) Math.PI);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                                float red, float green, float blue, float alpha)
    {
        this.head.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.robe.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.rightArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.leftArm.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack poseStack)
    {
        ModelPart handPart = arm == HumanoidArm.RIGHT ? this.rightArm : this.leftArm;
        handPart.translateAndRotate(poseStack);
    }
}
