package com.teamabnormals.personality.core;

import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import com.teamabnormals.personality.client.PersonalityClient;
import com.teamabnormals.personality.client.model.FishingHookModel;
import com.teamabnormals.personality.common.network.CrawlPayload;
import com.teamabnormals.personality.common.network.SitPayload;
import com.teamabnormals.personality.common.network.SyncCrawlPayload;
import com.teamabnormals.personality.common.network.SyncSitPayload;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
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

		if (FMLEnvironment.dist == Dist.CLIENT) {
			bus.addListener(this::registerKeyBindings);
			bus.addListener(this::registerLayerDefinitions);
			bus.addListener(this::modConfigEvent);
		}

		container.registerConfig(ModConfig.Type.COMMON, PersonalityConfig.COMMON_SPEC);
		container.registerConfig(ModConfig.Type.CLIENT, PersonalityConfig.CLIENT_SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		TrackedDataManager.INSTANCE.registerData(ResourceLocation.fromNamespaceAndPath(Personality.MOD_ID, "climbing"), CLIMBING);
	}

	private void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			PersonalityClient.TOGGLE_CRAWL.set(PersonalityConfig.CLIENT.toggleCrawl.get());
			PersonalityClient.TOGGLE_SIT.set(PersonalityConfig.CLIENT.toggleSitting.get());
		});
	}

	@OnlyIn(Dist.CLIENT)
	private void registerKeyBindings(RegisterKeyMappingsEvent event) {
		PersonalityClient.CRAWL.setKeyConflictContext(KeyConflictContext.IN_GAME);
		PersonalityClient.SIT.setKeyConflictContext(KeyConflictContext.IN_GAME);
		event.register(PersonalityClient.CRAWL);
		event.register(PersonalityClient.SIT);
	}


	@OnlyIn(Dist.CLIENT)
	private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MOD_ID, "fishing_hook"), "main"), FishingHookModel::createBodyLayer);
	}

	@OnlyIn(Dist.CLIENT)
	private void modConfigEvent(ModConfigEvent event) {
		if (event.getConfig().getType() == ModConfig.Type.CLIENT) {
			updateArmorValues();
		}
	}

	private static void updateArmorValues() {
		if (PersonalityConfig.CLIENT.deflateArmorModel.get()) {
			LayerDefinitions.INNER_ARMOR_DEFORMATION = new CubeDeformation(PersonalityConfig.CLIENT.innerArmorDeformation.get().floatValue());
			LayerDefinitions.OUTER_ARMOR_DEFORMATION = new CubeDeformation(PersonalityConfig.CLIENT.outerArmorDeformation.get().floatValue());
		} else {
			LayerDefinitions.INNER_ARMOR_DEFORMATION = new CubeDeformation(0.5F);
			LayerDefinitions.OUTER_ARMOR_DEFORMATION = new CubeDeformation(1.0F);
		}
	}

	private void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");
		registrar.playToServer(CrawlPayload.TYPE, CrawlPayload.STREAM_CODEC, CrawlPayload::handle);
		registrar.playToServer(SitPayload.TYPE, SitPayload.STREAM_CODEC, SitPayload::handle);
		registrar.playToClient(SyncCrawlPayload.TYPE, SyncCrawlPayload.STREAM_CODEC, SyncCrawlPayload::handle);
		registrar.playToClient(SyncSitPayload.TYPE, SyncSitPayload.STREAM_CODEC, SyncSitPayload::handle);
	}
}