package com.teamabnormals.personality.client.render.particle;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class FallingLeafParticle extends SpriteTexturedParticle {
    private final float rotSpeed;
    private final IAnimatedSprite spriteWithAge;

    private FallingLeafParticle(ClientWorld world, double x, double y, double z, float red, float green, float blue, IAnimatedSprite spriteWithAge) {
        super(world, x, y, z);
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;

        this.particleScale *= 1.2F;
        this.rotSpeed = ((float) Math.random() - 0.5F) * 0.1F;

        this.maxAge = 80;
        this.spriteWithAge = spriteWithAge;
        this.selectSpriteWithAge(spriteWithAge);
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
            return;
        }

        this.selectSpriteWithAge(this.spriteWithAge);
        this.prevParticleAngle = this.particleAngle;

        this.particleAngle += (float) Math.PI * this.rotSpeed * 2F;
        if (this.onGround) {
            this.prevParticleAngle = this.particleAngle = 0.0F;
        }

        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionY -= 0.003F;
        this.motionY = Math.max(this.motionY, -0.14F);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BlockParticleData> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSetIn) {
            this.spriteSet = spriteSetIn;
        }

        @Nullable
        public Particle makeParticle(BlockParticleData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BlockState state = data.getBlockState();
            if (!state.isAir() && state.getRenderType() == BlockRenderType.INVISIBLE) {
                return null;
            }

            BlockPos pos = new BlockPos(x, y, z);
            int color = Minecraft.getInstance().getBlockColors().getColor(state, world, pos, 0);
            if (color == -1)
                color = state.getMaterialColor(world, pos).colorValue;
            
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;
            return new FallingLeafParticle(world, x, y, z, red, green, blue, this.spriteSet);
        }
    }
}