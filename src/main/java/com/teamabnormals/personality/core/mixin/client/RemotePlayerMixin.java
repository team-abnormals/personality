package com.teamabnormals.personality.core.mixin.client;

import com.mojang.authlib.GameProfile;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

@Mixin(RemotePlayer.class)
public abstract class RemotePlayerMixin extends AbstractClientPlayer {

	public RemotePlayerMixin(ClientLevel level, GameProfile profile, @Nullable ProfilePublicKey key) {
		super(level, profile, key);
	}

	@Override
	public boolean isCrouching() {
		return super.isCrouching() && !Personality.SYNCED_SITTING_PLAYERS.contains(this.getUUID()) && this.getForcedPose() != Pose.SWIMMING;
	}
}
