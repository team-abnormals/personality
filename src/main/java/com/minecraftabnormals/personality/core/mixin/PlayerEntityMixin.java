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
    @Shadow @Final private static Map<Pose, EntitySize> SIZE_BY_POSE;
    @Shadow @Final public static EntitySize STANDING_SIZE;

    @Inject(method = "getSize", at = @At("RETURN"), cancellable = true)
    public void fireSizeEvent(Pose pose, CallbackInfoReturnable<EntitySize> cir) {
        cir.setReturnValue(PersonalityHooks.getPlayerSize(SIZE_BY_POSE.getOrDefault(pose, STANDING_SIZE), (PlayerEntity) (Object)this, pose));
    }
}
