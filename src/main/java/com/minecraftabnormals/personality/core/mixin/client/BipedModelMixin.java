package com.minecraftabnormals.personality.core.mixin.client;

import com.minecraftabnormals.personality.common.CommonEvents;
import com.minecraftabnormals.personality.core.Personality;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.animation.Animation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedModel.class)
public abstract class BipedModelMixin<T extends LivingEntity> extends AgeableModel<T> {

	@Shadow
	public ModelRenderer rightArm;

	@Shadow
	public ModelRenderer leftArm;

	@Shadow
	public ModelRenderer rightLeg;

	@Shadow
	public ModelRenderer leftLeg;

	@Inject(method = "setupAnim", at = @At("HEAD"))
	public void sitModel(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (entityIn instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityIn;
			if (Personality.SYNCED_SITTING_PLAYERS.contains(player.getUUID()))
				this.riding = true;
		}
	}

	@Inject(method = "setupAnim", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/model/BipedModel;riding:Z", shift = At.Shift.BEFORE))
	public void climbAnimation(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (entityIn instanceof PlayerEntity) {
			float f = !this.riding ? CommonEvents.getClimbingAnimationScale((PlayerEntity) entityIn, Animation.getPartialTickTime()) : 0.0F;
			float climbAnim = -f * (float) Math.PI / 2F;

			this.rightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F + climbAnim * 1.4F;
			this.leftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F + climbAnim * 1.4F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.rightArm.yRot = -climbAnim * 0.4F;
			this.leftArm.yRot = climbAnim * 0.4F;

			this.rightLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount + climbAnim * 0.5F;
			this.leftLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount + climbAnim * 0.5F;
			this.rightLeg.yRot = 0.0F;
			this.leftLeg.yRot = 0.0F;
			this.rightLeg.zRot = 0.0F;
			this.leftLeg.zRot = 0.0F;
		}
	}
}
