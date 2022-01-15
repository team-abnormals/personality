package com.teamabnormals.personality.core.mixin;

import com.teamabnormals.personality.client.ClimbAnimation;
import com.teamabnormals.personality.common.CommonEvents;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ClimbAnimation {
	private float climbAnim;
	private float prevClimbAnim;

	public PlayerEntityMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@Inject(method = "aiStep()V", at = @At("TAIL"))
	public void tickClimbAnim(CallbackInfo ci) {
		this.prevClimbAnim = this.climbAnim;
		if (CommonEvents.isClimbing((Player) (Object) this)) {
			this.climbAnim = Math.min(this.climbAnim + 1, 4.0F);
		} else {
			this.climbAnim = Math.max(this.climbAnim - 1, 0.0F);
		}
	}

	@Override
	public void move(MoverType type, Vec3 pos) {
		double x = pos.x();
		double y = pos.y();
		double z = pos.z();
		if (Personality.SITTING_PLAYERS.contains(this.getUUID()) && Math.cbrt(x * x + y * y + z * z) >= 0.185) {
			Personality.SITTING_PLAYERS.remove(this.getUUID());
		}
		super.move(type, pos);
	}

	@Override
	public float getClimbAnim() {
		return this.climbAnim;
	}

	@Override
	public float getPrevClimbAnim() {
		return this.prevClimbAnim;
	}
}
