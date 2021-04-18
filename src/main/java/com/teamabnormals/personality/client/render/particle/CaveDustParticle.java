package com.teamabnormals.personality.client.render.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class CaveDustParticle extends SpriteTexturedParticle {
    private final IAnimatedSprite spriteWithAge;
    private final double yAccel;

    protected CaveDustParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, float scale, IAnimatedSprite spriteWithAge) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.particleRed = 0.380392157F;
        this.particleGreen = 0.337254902F;
        this.particleBlue = 0.333333333F;
        this.particleScale *= 1.2F;

        this.yAccel = -0.0025D;
        this.motionX *= 0.3F;
        this.motionY *= -0.3F;
        this.motionZ *= 0.3F;

        this.canCollide = true;

        this.maxAge = 120;
        this.spriteWithAge = spriteWithAge;
        this.selectSpriteWithAge(spriteWithAge);
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public float getScale(float scaleFactor) {
        return this.particleScale * MathHelper.clamp(((float) this.age + scaleFactor) / (float) this.maxAge * 32.0F, 0.0F, 1.0F);
    }

    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.age++ >= this.maxAge) {
            this.setExpired();
        } else {
            this.selectSpriteWithAge(this.spriteWithAge);
            this.motionY += this.yAccel;
            this.move(this.motionX, this.motionY, this.motionZ);
            if (this.posY == this.prevPosY) {
                this.motionX *= 1.1D;
                this.motionZ *= 1.1D;
            }

            this.motionX *= 0.96F;
            this.motionY *= 0.96F;
            this.motionZ *= 0.96F;
            if (this.onGround) {
                this.motionX *= 0.7F;
                this.motionZ *= 0.7F;
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Random random = worldIn.rand;
            double motionX = (double) random.nextFloat() * -1.9D * (double) random.nextFloat() * 0.1D;
            double motionY = (double) random.nextFloat() * -0.5D * (double) random.nextFloat() * 0.1D * 5.0D;
            double motionZ = (double) random.nextFloat() * -1.9D * (double) random.nextFloat() * 0.1D;
            return new CaveDustParticle(worldIn, x, y, z, motionX, motionY, motionZ, 1.0F, this.spriteSet);
        }
    }
}