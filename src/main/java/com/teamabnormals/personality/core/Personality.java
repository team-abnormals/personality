package com.teamabnormals.personality.core;

import com.teamabnormals.personality.core.registry.PersonalityParticles;
import com.teamabnormals.personality.core.registry.PersonalitySounds;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Personality.MOD_ID)
public class Personality {
    public static final String MOD_ID = "personality";

    public Personality() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        PersonalityParticles.PARTICLE_TYPES.register(bus);
        PersonalitySounds.SOUND_EVENTS.register(bus);

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }
}