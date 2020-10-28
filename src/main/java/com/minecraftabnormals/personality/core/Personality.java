package com.minecraftabnormals.personality.core;

import com.teamabnormals.abnormals_core.common.world.storage.tracking.DataProcessors;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.TrackedData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Personality.MOD_ID)
public class Personality
{
    public static final String MOD_ID = "personality";
    public static final TrackedData<Boolean> PRONED = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).build();

    public Personality()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(FMLCommonSetupEvent event)
    {
    }

    private void clientSetup(FMLClientSetupEvent event)
    {
    }
}
