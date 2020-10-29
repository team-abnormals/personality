package com.minecraftabnormals.personality.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecraftabnormals.personality.core.accessor.AgeableModelAccessor;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.AgeableModel;

@Mixin(AgeableModel.class)
public class AgeableModelMixin implements AgeableModelAccessor {

	private boolean isForcedSitting;

	@Inject(at = @At("HEAD"), method = "render")
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, CallbackInfo info) {
		if (isForcedSitting) {
			matrixStackIn.translate(0.0F, 0.55F, 0.0F);
		}
	}

	@Override
	public boolean isForcedSitting() {
		return this.isForcedSitting;
	}

	@Override
	public void setForcedSitting(boolean sitting) {
		this.isForcedSitting = sitting;
	}
}