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
	public ModelRenderer bipedRightArm;

	@Shadow
	public ModelRenderer bipedLeftArm;

	@Shadow
	public ModelRenderer bipedRightLeg;

	@Shadow
	public ModelRenderer bipedLeftLeg;

	@Inject(method = "setRotationAngles", at = @At("HEAD"))
	public void sitModel(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (entityIn instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityIn;
			this.isSitting = Personality.SYNCED_SITTING_PLAYERS.contains(player.getUniqueID());
		}
	}

	@Inject(method = "setRotationAngles", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/model/BipedModel;isSitting:Z", shift = At.Shift.BEFORE))
	public void climbAnimation(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (entityIn instanceof PlayerEntity) {
			float f = !this.isSitting ? CommonEvents.getClimbingAnimationScale((PlayerEntity) entityIn, Animation.getPartialTickTime()) : 0.0F;
			float climbAnim = -f * (float) Math.PI / 2F;

			this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / 1.0F + climbAnim * 1.4F;
			this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / 1.0F + climbAnim * 1.4F;
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = -climbAnim * 0.4F;
			this.bipedLeftArm.rotateAngleY = climbAnim * 0.4F;

			this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / 1.0F + climbAnim * 0.5F;
			this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / 1.0F + climbAnim * 0.5F;
			this.bipedRightLeg.rotateAngleY = 0.0F;
			this.bipedLeftLeg.rotateAngleY = 0.0F;
			this.bipedRightLeg.rotateAngleZ = 0.0F;
			this.bipedLeftLeg.rotateAngleZ = 0.0F;
		}
	}
}
