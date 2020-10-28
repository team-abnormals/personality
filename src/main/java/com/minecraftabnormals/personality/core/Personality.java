package com.minecraftabnormals.personality.core;

import com.minecraftabnormals.personality.client.PersonalityKeyBindings;
import com.minecraftabnormals.personality.common.network.MessageC2SCrawl;
import com.minecraftabnormals.personality.common.network.MessageC2SSit;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.DataProcessors;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.IDataProcessor;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.TrackedData;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.TrackedDataManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

import javax.annotation.Nullable;

@Mod(Personality.MODID)
public class Personality {
	public static final String MODID = "personality";

	public static final IDataProcessor<PlayerState> PLAYER_STATE_ENUM = new IDataProcessor<PlayerState>() {

		@Override
		public CompoundNBT write(PlayerState state) {
			CompoundNBT compound = new CompoundNBT();
			compound.putInt("State", state.ordinal());
			return compound;
		}

		@Override
		public PlayerState read(CompoundNBT compound) {
			return PlayerState.values()[compound.getInt("State")];
		}
	};

	public static final TrackedData<Boolean> CRAWLING = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).build();
	public static final TrackedData<Boolean> SITTING = TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).build();
	public static final TrackedData<PlayerState> PLAYER_STATE = TrackedData.Builder.create(PLAYER_STATE_ENUM, () -> PlayerState.PLAYING).build();

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

		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PersonalityConfig.CLIENT_SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(MODID, "crawling"), CRAWLING);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(MODID, "sitting"), SITTING);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(MODID, "state"), PLAYER_STATE);
	}

	private void clientSetup(FMLClientSetupEvent event) {
		PersonalityKeyBindings.registerKeyBinds();
	}

	private void setupMessages() {
		int id = -1;

		CHANNEL.registerMessage(id++, MessageC2SCrawl.class, MessageC2SCrawl::serialize, MessageC2SCrawl::deserialize, MessageC2SCrawl::handle);
		CHANNEL.registerMessage(id, MessageC2SSit.class, MessageC2SSit::serialize, MessageC2SSit::deserialize, MessageC2SSit::handle);
	}

	public enum PlayerState {
		PLAYING(null, null),
		PAUSED(new ResourceLocation(MODID, "textures/gui/paused.png"), new StringTextComponent("\u23F8")),
		CHATTING(new ResourceLocation(MODID, "textures/gui/chatting.png"), new StringTextComponent("\u22EF")),
		INVENTORY(new ResourceLocation(MODID, "textures/gui/inventory.png"), new StringTextComponent("\u23F9"));

		private final ResourceLocation icon;
		private final ITextComponent simpleText;

		PlayerState(@Nullable ResourceLocation icon, @Nullable ITextComponent simpleText) {
			this.icon = icon;
			this.simpleText = simpleText;
		}

		@Nullable
		@OnlyIn(Dist.CLIENT)
		public ResourceLocation getIcon() {
			return icon;
		}

		@Nullable
		@OnlyIn(Dist.CLIENT)
		public ITextComponent getSimpleText() {
			return simpleText;
		}
	}
}
