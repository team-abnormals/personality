package com.teamabnormals.personality.core.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {

    @Shadow
    private BlockState fallTile;

    public FallingBlockEntityMixin(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/item/FallingBlockEntity;move(Lnet/minecraft/entity/MoverType;Lnet/minecraft/util/math/vector/Vector3d;)V", shift = At.Shift.AFTER))
    public void tick(CallbackInfo ci) {
        this.playSound(this.fallTile.getSoundType().getHitSound(), 1, 1.8F);
        for (int i = 0; i < 4; i++) {
            if (rand.nextBoolean()) {
                double d0 = this.prevPosX - 0.5D + rand.nextDouble();
                double d1 = this.prevPosY + rand.nextDouble();
                double d2 = this.prevPosZ - 0.5D + rand.nextDouble();
                this.world.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, this.fallTile), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
