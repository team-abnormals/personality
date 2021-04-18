package com.teamabnormals.personality.client.render.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.projectile.FishingBobberEntity;

public class FishingBobberModel extends EntityModel<FishingBobberEntity> {
    private final ModelRenderer bobber;

    public FishingBobberModel() {
        this.textureWidth = 16;
        this.textureHeight = 16;

        bobber = new ModelRenderer(this);
        bobber.setRotationPoint(0.0F, 24.0F, 0.0F);
        bobber.setTextureOffset(0, 0).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, false);

        ModelRenderer topper = new ModelRenderer(this);
        topper.setRotationPoint(0.0F, 0.0F, 0.0F);
        bobber.addChild(topper);

        ModelRenderer cross1 = new ModelRenderer(this);
        cross1.setRotationPoint(0.0F, -3.0F, 0.0F);
        topper.addChild(cross1);
        setRotationAngle(cross1, 0.0F, 0.7854F, 0.0F);
        cross1.setTextureOffset(5, 6).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, true);

        ModelRenderer cross2 = new ModelRenderer(this);
        cross2.setRotationPoint(0.0F, -3.0F, 0.0F);
        topper.addChild(cross2);
        setRotationAngle(cross2, 0.0F, -0.7854F, 0.0F);
        cross2.setTextureOffset(5, 6).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);

        ModelRenderer hook = new ModelRenderer(this);
        hook.setRotationPoint(0.0F, 0.0F, 0.0F);
        bobber.addChild(hook);
        hook.setTextureOffset(0, 3).addBox(0.0F, 0.0F, -2.5F, 0.0F, 4.0F, 3.0F, 0.0F, true);
    }

    @Override
    public void setRotationAngles(FishingBobberEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bobber.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}