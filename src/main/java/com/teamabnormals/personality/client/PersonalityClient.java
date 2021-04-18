package com.teamabnormals.personality.client;

import com.teamabnormals.personality.client.render.particle.CaveDustParticle;
import com.teamabnormals.personality.client.render.particle.FallingLeafParticle;
import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.registry.PersonalityParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Personality.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PersonalityClient {
    @SubscribeEvent
    public static void onEvent(ParticleFactoryRegisterEvent event) {
        ParticleManager manager = Minecraft.getInstance().particles;
        if (PersonalityParticles.FALLING_LEAF.isPresent()) {
            manager.registerFactory(PersonalityParticles.FALLING_LEAF.get(), FallingLeafParticle.Factory::new);
        }
    }

    public static boolean isFreezing(LivingEntity entity) {
        World world = entity.getEntityWorld();
        BlockPos pos = entity.getPosition().up();
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
