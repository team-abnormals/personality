package com.minecraftabnormals.personality.core.mixin;

import com.minecraftabnormals.personality.core.Personality;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	public abstract UUID getUniqueID();

	@Inject(method = "move", at = @At("HEAD"))
	public void move(MoverType typeIn, Vector3d pos, CallbackInfo info) {
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();

		if (Personality.SITTING_PLAYERS.contains(this.getUniqueID()) && Math.cbrt(x * x + y * y + z * z) >= 0.185) {
			Personality.SITTING_PLAYERS.remove(this.getUniqueID());
		}
	}
}
