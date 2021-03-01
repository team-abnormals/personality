package com.minecraftabnormals.personality.core.mixin.client;

import com.minecraftabnormals.personality.core.Personality;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "isCrouching", at = @At("HEAD"), cancellable = true)
    public void stopCrouch(CallbackInfoReturnable<Boolean> cir) {
        if (Personality.SYNCED_SITTING_PLAYERS.contains(this.getUniqueID()) || this.getForcedPose() == Pose.SWIMMING)
            cir.setReturnValue(false);
    }
}
