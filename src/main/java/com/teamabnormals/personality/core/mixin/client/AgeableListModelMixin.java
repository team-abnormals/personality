package com.teamabnormals.personality.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.personality.client.SittableModel;
import net.minecraft.client.model.AgeableListModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableListModel.class)
public abstract class AgeableListModelMixin implements SittableModel {
	private boolean isForcedSitting;

	@Inject(at = @At("HEAD"), method = "renderToBuffer")
	public void render(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, CallbackInfo info) {
		if (this.isForcedSitting) {
			matrixStackIn.translate(0.0F, 0.55F, 0.0F);
		}
	}

	@Override
	public void setForcedSitting(boolean sitting) {
		this.isForcedSitting = sitting;
	}
}