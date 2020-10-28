package com.minecraftabnormals.personality.core;

import com.minecraftabnormals.personality.client.Keybinds;
import com.minecraftabnormals.personality.client.renderer.SeatRenderer;
import com.minecraftabnormals.personality.common.entity.SeatEntity;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.DataProcessors;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.TrackedData;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Personality.MODID)
public class Personality {
	public static final String MODID = "personality";
	public static final TrackedData<Boolean> PRONED = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).build();

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
	public static final RegistryObject<EntityType<SeatEntity>> SEAT = ENTITIES.register("seat", () -> EntityType.Builder.<SeatEntity>create(SeatEntity::new, EntityClassification.MISC).setTrackingRange(256).setUpdateInterval(20).size(0.0001F, 0.0001F).build(MODID + ":seat"));

	public Personality() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ENTITIES.register(bus);

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);

		MinecraftForge.EVENT_BUS.register(this);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
	}

	private void clientSetup(FMLClientSetupEvent event) {
		Keybinds.registerKeyBinds();
		RenderingRegistry.registerEntityRenderingHandler(SEAT.get(), SeatRenderer::new);
	}

	@SubscribeEvent
	public void onPlayerTick(RightClickBlock event) {
		PlayerEntity player = event.getPlayer();
		World world = event.getWorld();
		if (!world.isRemote() && player.isOnGround()) {
			SeatEntity seat = SEAT.get().create(world);
			seat.setPosition(player.getPosX(), player.getPosY() - 0.2F, player.getPosZ());
			world.addEntity(seat);
			player.startRiding(seat);
		}
	}
}
