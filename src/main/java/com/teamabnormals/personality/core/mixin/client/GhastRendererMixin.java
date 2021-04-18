package com.teamabnormals.personality.core.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teamabnormals.personality.core.extension.GhastExtension;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GhastRenderer.class, priority = 200)
public class GhastRendererMixin {

    @Inject(method = "preRenderCallback", at = @At("HEAD"), cancellable = true)
    protected void preRenderCallback(GhastEntity entity, MatrixStack matrixStack, float partialTicks, CallbackInfo ci) {
        GhastExtension ext = (GhastExtension) entity;

        float attack = MathHelper.lerp(partialTicks, ext.getPreviousAttackTimer(), ext.getAttackTimer()) / 10F;
        float anim = (float) (attack * attack - Math.pow(attack, 6) - Math.pow(attack, 7) + attack);
        float y = 4F - anim / 1.5F;
        float xz = 4F + anim;

        matrixStack.scale(xz, y, xz);

        ci.cancel(); // TODO: config
    }
}
