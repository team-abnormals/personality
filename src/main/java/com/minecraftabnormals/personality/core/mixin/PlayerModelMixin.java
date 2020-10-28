package com.minecraftabnormals.personality.core.mixin;

import com.minecraftabnormals.personality.core.Personality;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerModelMixin<T extends LivingEntity> extends BipedModel<T> {
	@Final
	@Shadow
	public ModelRenderer bipedLeftArmwear;
	@Final
	@Shadow
	public ModelRenderer bipedRightArmwear;

	public PlayerModelMixin(float p_i1148_1_) {
		super(p_i1148_1_);
	}

	@Inject(at = @At("TAIL"), method = "setRotationAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
	public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
		if (entity.getPrimaryHand() == HandSide.RIGHT) {
			this.eatingAnimationRightHand(Hand.MAIN_HAND, entity, ageInTicks);
			this.eatingAnimationLeftHand(Hand.OFF_HAND, entity, ageInTicks);
		} else {
			this.eatingAnimationRightHand(Hand.OFF_HAND, entity, ageInTicks);
			this.eatingAnimationLeftHand(Hand.MAIN_HAND, entity, ageInTicks);
		}

		IDataManager data = (IDataManager) entity;
		if (data.getValue(Personality.SITTING) && !data.getValue(Personality.CRAWLING) && !entity.isPassenger()) {
			this.bipedRightArm.rotateAngleX += (-(float) Math.PI / 5F);
			this.bipedLeftArm.rotateAngleX += (-(float) Math.PI / 5F);
			this.bipedRightLeg.rotateAngleX = -1.4137167F;
			this.bipedRightLeg.rotateAngleY = ((float) Math.PI / 10F);
			this.bipedRightLeg.rotateAngleZ = 0.07853982F;
			this.bipedLeftLeg.rotateAngleX = -1.4137167F;
			this.bipedLeftLeg.rotateAngleY = (-(float) Math.PI / 10F);
			this.bipedLeftLeg.rotateAngleZ = -0.07853982F;
		}
	}

	public void eatingAnimationRightHand(Hand hand, LivingEntity entity, float ageInTicks) {
		ItemStack stack = entity.getHeldItem(hand);
		boolean eating = stack.getUseAction() == UseAction.EAT || stack.getUseAction() == UseAction.DRINK;
		if (entity.getItemInUseCount() > 0 && eating && entity.getActiveHand() == hand) {
			this.bipedRightArm.rotateAngleY = -0.5F;
			this.bipedRightArm.rotateAngleX = -1.3F;
			this.bipedRightArm.rotateAngleZ = MathHelper.cos(ageInTicks) * 0.1F;
			this.bipedRightArmwear.copyModelAngles(bipedRightArm);
		}
	}

	public void eatingAnimationLeftHand(Hand hand, LivingEntity entity, float ageInTicks) {
		ItemStack stack = entity.getHeldItem(hand);
		boolean eating = stack.getUseAction() == UseAction.EAT || stack.getUseAction() == UseAction.DRINK;
		if (entity.getItemInUseCount() > 0 && eating && entity.getActiveHand() == hand) {
			this.bipedLeftArm.rotateAngleY = 0.5F;
			this.bipedLeftArm.rotateAngleX = -1.3F;
			this.bipedLeftArm.rotateAngleZ = MathHelper.cos(ageInTicks) * 0.1F;
			this.bipedLeftArmwear.copyModelAngles(bipedLeftArm);
		}
	}
}