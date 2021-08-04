package com.minecraftabnormals.personality.core.mixin.client;

import com.minecraftabnormals.personality.core.Personality;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RemoteClientPlayerEntity.class)
public abstract class RemoteClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public RemoteClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public boolean isCrouching() {
        return super.isCrouching() && !Personality.SYNCED_SITTING_PLAYERS.contains(this.getUUID()) && this.getForcedPose() != Pose.SWIMMING;
    }
}
