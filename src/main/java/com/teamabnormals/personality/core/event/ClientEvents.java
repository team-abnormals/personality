package com.teamabnormals.personality.core.event;

import com.minecraftabnormals.abnormals_core.core.events.AnimateTickEvent;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
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
}
