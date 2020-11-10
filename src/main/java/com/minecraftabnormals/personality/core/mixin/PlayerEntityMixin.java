package com.minecraftabnormals.personality.core.mixin;

import com.minecraftabnormals.personality.core.event.PersonalityHooks;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(method = "getSize", at = @At("RETURN"), cancellable = true)
	public void fireSizeEvent(Pose pose, CallbackInfoReturnable<EntitySize> cir) {
		cir.setReturnValue(PersonalityHooks.getPlayerSize(cir.getReturnValue(), (PlayerEntity) (Object) this, pose));
	}
}
