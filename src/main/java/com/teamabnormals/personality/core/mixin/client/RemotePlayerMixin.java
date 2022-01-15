package com.teamabnormals.personality.core.mixin.client;

import com.mojang.authlib.GameProfile;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RemotePlayer.class)
public abstract class RemotePlayerMixin extends AbstractClientPlayer {
    public RemotePlayerMixin(ClientLevel level, GameProfile profile) {
        super(level, profile);
    }

    @Override
    public boolean isCrouching() {
        return super.isCrouching() && !Personality.SYNCED_SITTING_PLAYERS.contains(this.getUUID()) && this.getForcedPose() != Pose.SWIMMING;
    }
}
