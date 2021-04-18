package com.teamabnormals.personality.core.event;

import com.minecraftabnormals.abnormals_core.core.events.AnimateTickEvent;
import com.teamabnormals.personality.client.PersonalityClient;
import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.registry.PersonalityParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Personality.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onEvent(RenderHandEvent event) {
        PlayerEntity entity = Minecraft.getInstance().player;
        if (entity == null)
            return;

        if (PersonalityClient.isFreezing(entity))
            event.getMatrixStack().rotate(Vector3f.YP.rotation((float) (Math.cos(entity.ticksExisted * 3.25D) * Math.PI * 0.001F)));
    }

    @SubscribeEvent
    public static void onEvent(AnimateTickEvent event) {
        IWorld world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        Random rand = event.getRandom();

        if (state.getBlock() instanceof LeavesBlock) { // TODO: config
            if (rand.nextInt(48) == 0) {
                BlockPos blockpos = pos.down();
                if (world.isAirBlock(blockpos) || FallingBlock.canFallThrough(world.getBlockState(blockpos))) {
                    double posX = pos.getX() + rand.nextDouble();
                    double posY = pos.getY();
                    double posZ = pos.getZ() + rand.nextDouble();
                    world.addParticle(new BlockParticleData(PersonalityParticles.FALLING_LEAF.get(), state), posX, posY, posZ, 0.0D, 0.0D, 0.0D);
                }
            }
        }

        if (state.getBlock() instanceof JukeboxBlock && state.get(JukeboxBlock.HAS_RECORD)) { // TODO: config
            Minecraft minecraft = Minecraft.getInstance();
            Map<BlockPos, ISound> sounds = minecraft.worldRenderer.mapSoundPositions;
            if (sounds.containsKey(pos) && minecraft.getSoundHandler().isPlaying(sounds.get(pos)))
                world.addParticle(ParticleTypes.NOTE, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, rand.nextInt(25) / 24D, 0, 0);
        }
    }
}
