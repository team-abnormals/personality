package com.teamabnormals.personality.core.event;

import com.minecraftabnormals.abnormals_core.core.events.AnimateTickEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Personality.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onEvent(AnimateTickEvent event) {
        // TODO: config
        IWorld world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof JukeboxBlock && state.get(JukeboxBlock.HAS_RECORD)) {
            Minecraft minecraft = Minecraft.getInstance();
            Map<BlockPos, ISound> sounds = minecraft.worldRenderer.mapSoundPositions;
            Random rand = event.getRandom();
            if (sounds.containsKey(pos) && minecraft.getSoundHandler().isPlaying(sounds.get(pos)))
                world.addParticle(ParticleTypes.NOTE, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, rand.nextInt(25) / 24D, 0, 0);
        }
    }

    @SubscribeEvent
    public static void onEvent(RenderLivingEvent<?, ?> event) {
        LivingEntity entity = event.getEntity();
        MatrixStack matrixStack = event.getMatrixStack();
        if (isFreezing(entity))
            matrixStack.rotate(Vector3f.YP.rotation((float) (Math.cos(entity.ticksExisted * 3.25D) * Math.PI * 0.003F)));
    }

    private static boolean isFreezing(LivingEntity entity) {
        World world = entity.getEntityWorld();
        BlockPos pos = entity.getPosition();
        if (!world.isRaining())
            return false;
        if (!world.canSeeSky(pos))
            return false;
        if (world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos).getY() > pos.getY())
            return false;

        Biome biome = world.getBiome(pos);
        return biome.getPrecipitation() == Biome.RainType.SNOW;
        // TODO: config
    }
}
