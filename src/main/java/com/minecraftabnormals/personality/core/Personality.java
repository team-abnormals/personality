package com.minecraftabnormals.personality.core;

import com.minecraftabnormals.personality.client.PersonalityKeyBindings;
import com.minecraftabnormals.personality.client.renderer.SeatRenderer;
import com.minecraftabnormals.personality.common.entity.SeatEntity;
import com.minecraftabnormals.personality.common.network.MessageC2SCrawl;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.DataProcessors;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.TrackedData;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.TrackedDataManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Personality.MODID)
public class Personality {
	public static final String MODID = "personality";
	public static final TrackedData<Boolean> CRAWLING = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).build();

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
	public static final RegistryObject<EntityType<SeatEntity>> SEAT = ENTITIES.register("seat", () -> EntityType.Builder.create(SeatEntity::new, EntityClassification.MISC).setTrackingRange(256).setUpdateInterval(20).size(0.0001F, 0.0001F).build(MODID + ":seat"));

	public static final String NETWORK_PROTOCOL = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "net"))
			.networkProtocolVersion(() -> NETWORK_PROTOCOL)
			.clientAcceptedVersions(NETWORK_PROTOCOL::equals)
			.serverAcceptedVersions(NETWORK_PROTOCOL::equals)
			.simpleChannel();

	public Personality() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		MinecraftForge.EVENT_BUS.register(this);

		this.setupMessages();

		ENTITIES.register(bus);

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PersonalityConfig.CLIENT_SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(MODID, "crawling"), CRAWLING);
	}

	private void clientSetup(FMLClientSetupEvent event) {
		PersonalityKeyBindings.registerKeyBinds();
		RenderingRegistry.registerEntityRenderingHandler(SEAT.get(), SeatRenderer::new);
	}

	private void setupMessages() {
		int id = -1;

		CHANNEL.registerMessage(id, MessageC2SCrawl.class, MessageC2SCrawl::serialize, MessageC2SCrawl::deserialize, MessageC2SCrawl::handle);
	}

	@SubscribeEvent
	public void onPlayerTick(RightClickBlock event) {
		PlayerEntity player = event.getPlayer();
		World world = event.getWorld();
		if (!world.isRemote() && player.isOnGround()) {
			SeatEntity seat = SEAT.get().create(world);

			if (seat == null)
				return;

			seat.setPosition(player.getPosX(), player.getPosY() - 0.2F, player.getPosZ());
			world.addEntity(seat);
			player.startRiding(seat);
		}
	}
}
