package com.minecraftabnormals.personality.core.mixin;

import com.minecraftabnormals.personality.client.ClimbAnimation;
import com.minecraftabnormals.personality.common.CommonEvents;
import com.minecraftabnormals.personality.core.Personality;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ClimbAnimation {
	private float climbAnim;
	private float prevClimbAnim;

	public PlayerEntityMixin(EntityType<? extends LivingEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Inject(method = "aiStep()V", at = @At("TAIL"))
	public void tickClimbAnim(CallbackInfo ci) {
		this.prevClimbAnim = this.climbAnim;
		if (CommonEvents.isClimbing((PlayerEntity) (Object) this)) {
			this.climbAnim = Math.min(this.climbAnim + 1, 4.0F);
		} else {
			this.climbAnim = Math.max(this.climbAnim - 1, 0.0F);
		}
	}

	@Override
	public void move(MoverType typeIn, Vector3d pos) {
		double x = pos.x();
		double y = pos.y();
		double z = pos.z();
		if (Personality.SITTING_PLAYERS.contains(this.getUUID()) && Math.cbrt(x * x + y * y + z * z) >= 0.185) {
			Personality.SITTING_PLAYERS.remove(this.getUUID());
		}
		super.move(typeIn, pos);
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
