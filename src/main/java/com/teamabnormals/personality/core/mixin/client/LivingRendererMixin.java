package com.teamabnormals.personality.core.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teamabnormals.personality.client.PersonalityClient;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingRenderer.class)
public class LivingRendererMixin<T extends LivingEntity> {
    @Unique
    private T entity;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingRenderer;applyRotations(Lnet/minecraft/entity/LivingEntity;Lcom/mojang/blaze3d/matrix/MatrixStack;FFF)V", shift = At.Shift.BEFORE))
    public void capture(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, CallbackInfo ci) {
        this.entity = entityIn;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingRenderer;applyRotations(Lnet/minecraft/entity/LivingEntity;Lcom/mojang/blaze3d/matrix/MatrixStack;FFF)V"), index = 3)
    public float applyRotations(float rotationYaw) {
        if (PersonalityClient.isFreezing(this.entity)) {
            rotationYaw += (float) (Math.cos(this.entity.ticksExisted * 3.25D) * Math.PI * 0.2F);
        }
        return rotationYaw;
    }
}