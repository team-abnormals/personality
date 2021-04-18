package com.teamabnormals.personality.client;

import com.teamabnormals.personality.client.render.particle.FallingLeafParticle;
import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.registry.PersonalityParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
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
}
