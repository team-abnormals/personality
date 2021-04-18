package com.teamabnormals.personality.core.mixin;

import com.teamabnormals.personality.core.extension.GhastExtension;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GhastEntity.class)
public abstract class GhastEntityMixin extends FlyingEntity implements GhastExtension {
    @Unique
    private int attackTimer;
    @Unique
    private int prevAttackTimer;

    private GhastEntityMixin(EntityType<? extends FlyingEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void tick() {
        super.tick(); // TODO: config
        if (this.world.isRemote()) {
            this.prevAttackTimer = this.attackTimer;
            if (this.attackTimer > 0) {
                this.attackTimer--;
            }
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 4) {
            this.prevAttackTimer = 10;
            this.attackTimer = 10; // TODO: config
            return;
        }

        super.handleStatusUpdate(id);
    }

    @Override
    public int getPreviousAttackTimer() {
        return this.prevAttackTimer;
    }

    @Override
    public int getAttackTimer() {
        return this.attackTimer;
    }
}
