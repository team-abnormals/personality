package com.teamabnormals.personality.core.mixin;

import com.teamabnormals.personality.core.registry.PersonalitySounds;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin {

    @Shadow
    public abstract boolean isCharged();

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/util/SoundEvents;ENTITY_CREEPER_PRIMED:Lnet/minecraft/util/SoundEvent;"))
    public SoundEvent tick() {
        if (this.isCharged()) return PersonalitySounds.ENTITY_CREEPER_CHARGED_PRIMED.get();
        return SoundEvents.ENTITY_CREEPER_PRIMED; // TODO: config & explosion sound
    }
}
