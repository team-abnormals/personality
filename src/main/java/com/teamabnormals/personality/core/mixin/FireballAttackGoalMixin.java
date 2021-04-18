package com.teamabnormals.personality.core.mixin;

import net.minecraft.entity.monster.GhastEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.entity.monster.GhastEntity$FireballAttackGoal")
public class FireballAttackGoalMixin {

    @Shadow
    public int attackTimer;
    @Shadow
    @Final
    private GhastEntity parentEntity;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/monster/GhastEntity;setAttacking(Z)V", shift = At.Shift.BEFORE))
    public void tick(CallbackInfo ci) {
        if (this.attackTimer > 10 && !this.parentEntity.isAttacking())
            this.parentEntity.world.setEntityState(this.parentEntity, (byte) 4); // TODO: config
    }
}
