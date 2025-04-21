package com.teamabnormals.personality.core;

import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.personality.common.network.CrawlPayload;
import com.teamabnormals.personality.common.network.SitPayload;
import com.teamabnormals.personality.common.network.SyncCrawlPayload;
import com.teamabnormals.personality.common.network.SyncSitPayload;
import com.teamabnormals.personality.core.other.PersonalityKeyBindings;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod(Personality.MOD_ID)
public class Personality {
	public static final String MOD_ID = "personality";

	public static final TrackedData<Byte> CLIMBING = TrackedData.Builder.create(ByteBufCodecs.BYTE, () -> (byte) 0).build();

	public static final Set<UUID> SITTING_PLAYERS = new HashSet<>();
	public static final Set<UUID> SYNCED_SITTING_PLAYERS = new HashSet<>();

	public Personality(IEventBus bus, ModContainer container) {
		bus.addListener(this::registerPayloadHandlers);

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);

		container.registerConfig(ModConfig.Type.COMMON, PersonalityConfig.COMMON_SPEC);
		container.registerConfig(ModConfig.Type.CLIENT, PersonalityConfig.CLIENT_SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		TrackedDataManager.INSTANCE.registerData(Personality.location("climbing"), CLIMBING);
	}

	private void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(PersonalityKeyBindings::setupToggleSettings);
	}

	private void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");
		registrar.playToServer(CrawlPayload.TYPE, CrawlPayload.STREAM_CODEC, CrawlPayload::handle);
		registrar.playToServer(SitPayload.TYPE, SitPayload.STREAM_CODEC, SitPayload::handle);
		registrar.playToClient(SyncCrawlPayload.TYPE, SyncCrawlPayload.STREAM_CODEC, SyncCrawlPayload::handle);
		registrar.playToClient(SyncSitPayload.TYPE, SyncSitPayload.STREAM_CODEC, SyncSitPayload::handle);
	}

	public static ResourceLocation location(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}