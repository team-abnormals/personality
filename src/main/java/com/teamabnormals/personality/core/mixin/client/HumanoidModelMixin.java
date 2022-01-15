package com.teamabnormals.personality.core.mixin.client;

import com.teamabnormals.personality.common.CommonEvents;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> {

	@Final
	@Shadow
	public ModelPart rightArm;

	@Final
	@Shadow
	public ModelPart leftArm;

	@Final
	@Shadow
	public ModelPart rightLeg;

	@Final
	@Shadow
	public ModelPart leftLeg;

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
	public void sitModel(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (entityIn instanceof Player player) {
			if (Personality.SYNCED_SITTING_PLAYERS.contains(player.getUUID()))
				this.riding = true;
		}
	}

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/HumanoidModel;riding:Z", shift = At.Shift.BEFORE))
	public void climbAnimation(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (entityIn instanceof Player) {
			float f = !this.riding ? CommonEvents.getClimbingAnimationScale((Player) entityIn, Minecraft.getInstance().getFrameTime()) : 0.0F;
			float climbAnim = -f * (float) Math.PI / 2F;

			this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F + climbAnim * 1.4F;
			this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F + climbAnim * 1.4F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.zRot = 0.0F;
			this.rightArm.yRot = -climbAnim * 0.4F;
			this.leftArm.yRot = climbAnim * 0.4F;

			this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount + climbAnim * 0.5F;
			this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount + climbAnim * 0.5F;
			this.rightLeg.yRot = 0.0F;
			this.leftLeg.yRot = 0.0F;
			this.rightLeg.zRot = 0.0F;
			this.leftLeg.zRot = 0.0F;
		}
	}
}
