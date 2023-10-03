package com.teamabnormals.personality.core.mixin;

import com.teamabnormals.personality.core.other.PersonalityEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	private boolean isFlying;

	public LivingEntityMixin(EntityType<?> entityTypeIn, Level level) {
		super(entityTypeIn, level);
	}

	@Inject(method = "calculateEntityAnimation", at = @At("HEAD"))
	public void captureFlying(boolean flying, CallbackInfo ci) {
		this.isFlying = flying;
	}

	@ModifyVariable(method = "calculateEntityAnimation", ordinal = 0, at = @At("STORE"))
	public float swingArm(float f) {
		boolean flag = this.isFlying;
		if ((((LivingEntity) (Object) this) instanceof Player)) {
			Player player = (Player) (Object) this;
			flag |= player.yOld < player.getY() && PersonalityEvents.isClimbing(player);
		}
		return (float) Mth.length(this.getX() - this.xo, flag ? this.getY() - this.yo : 0.0D, this.getZ() - this.zo);
	}
}
