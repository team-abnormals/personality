package com.minecraftabnormals.personality.core.mixin;

import com.minecraftabnormals.personality.common.CommonEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private boolean isFlying;

    public LivingEntityMixin(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Inject(method = "func_233629_a_", at = @At("HEAD"))
    public void captureFlying(LivingEntity p_233629_1_, boolean flying, CallbackInfo ci) {
        this.isFlying = flying;
    }

    @ModifyVariable(method = "func_233629_a_", ordinal = 1, at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;prevPosZ:D", shift = At.Shift.AFTER))
    public double swingArm(double d1) {
        boolean flag = this.isFlying;
        if (((LivingEntity) (Object) this instanceof PlayerEntity)) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            flag |= player.lastTickPosY < player.getPosY() && CommonEvents.isClimbing(player);
        }
        return flag ? this.getPosY() - this.prevPosY : 0.0D;
    }
}
