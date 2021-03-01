package com.minecraftabnormals.personality.core.mixin.client;

import com.minecraftabnormals.personality.client.SittableModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableModel.class)
public abstract class AgeableModelMixin implements SittableModel {
    private boolean isForcedSitting;

    @Inject(at = @At("HEAD"), method = "render")
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, CallbackInfo info) {
        if (isForcedSitting) {
            matrixStackIn.translate(0.0F, 0.55F, 0.0F);
        }
    }

    @Override
    public void setForcedSitting(boolean sitting) {
        this.isForcedSitting = sitting;
    }
}