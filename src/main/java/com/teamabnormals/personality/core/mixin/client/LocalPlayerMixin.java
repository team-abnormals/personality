package com.teamabnormals.personality.core.mixin.client;

import com.mojang.authlib.GameProfile;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

	public LocalPlayerMixin(ClientLevel level, GameProfile profile) {
		super(level, profile);
	}

	@Inject(method = "isCrouching", at = @At("HEAD"), cancellable = true)
	public void stopCrouch(CallbackInfoReturnable<Boolean> cir) {
		if (Personality.SYNCED_SITTING_PLAYERS.contains(this.getUUID()) || this.getForcedPose() == Pose.SWIMMING)
			cir.setReturnValue(false);
	}
}
