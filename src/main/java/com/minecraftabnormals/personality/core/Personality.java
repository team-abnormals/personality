package com.minecraftabnormals.personality.core;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.DataProcessors;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.TrackedData;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.TrackedDataManager;
import com.minecraftabnormals.personality.client.PersonalityClient;
import com.minecraftabnormals.personality.common.network.MessageC2SCrawl;
import com.minecraftabnormals.personality.common.network.MessageC2SSit;
import com.minecraftabnormals.personality.common.network.MessageS2CSyncCrawl;
import com.minecraftabnormals.personality.common.network.MessageS2CSyncSit;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod(Personality.MOD_ID)
public class Personality {
    public static final String MOD_ID = "personality";
    public static final String NETWORK_PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "net"))
            .networkProtocolVersion(() -> NETWORK_PROTOCOL)
            .clientAcceptedVersions(NETWORK_PROTOCOL::equals)
            .serverAcceptedVersions(NETWORK_PROTOCOL::equals)
            .simpleChannel();

    public static final TrackedData<Byte> CLIMBING = TrackedData.Builder.create(DataProcessors.BYTE, () -> (byte) 0).build();

    public static final Set<UUID> SITTING_PLAYERS = new HashSet<>();
    public static final Set<UUID> SYNCED_SITTING_PLAYERS = new HashSet<>();

    public Personality() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        this.networkSetup();

        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PersonalityConfig.CLIENT_SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> TrackedDataManager.INSTANCE.registerData(new ResourceLocation(Personality.MOD_ID, "climbing"), CLIMBING));
    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(PersonalityClient::registerKeyBinds);
    }

    private void networkSetup() {
        int id = -1;

        CHANNEL.registerMessage(id++, MessageC2SCrawl.class, MessageC2SCrawl::serialize, MessageC2SCrawl::deserialize, MessageC2SCrawl::handle);
        CHANNEL.registerMessage(id++, MessageC2SSit.class, MessageC2SSit::serialize, MessageC2SSit::deserialize, MessageC2SSit::handle);
        CHANNEL.registerMessage(id++, MessageS2CSyncCrawl.class, MessageS2CSyncCrawl::serialize, MessageS2CSyncCrawl::deserialize, MessageS2CSyncCrawl::handle);
        CHANNEL.registerMessage(id, MessageS2CSyncSit.class, MessageS2CSyncSit::serialize, MessageS2CSyncSit::deserialize, MessageS2CSyncSit::handle);
    }
}