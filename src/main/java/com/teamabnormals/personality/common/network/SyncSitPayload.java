package com.teamabnormals.personality.common.network;

import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.other.PersonalityClientEvents;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record SyncSitPayload(UUID uuid, boolean isSitting) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SyncSitPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Personality.MOD_ID, "sync_sit"));

	public static final StreamCodec<ByteBuf, SyncSitPayload> STREAM_CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, SyncSitPayload::uuid,
			ByteBufCodecs.BOOL, SyncSitPayload::isSitting,
			SyncSitPayload::new
	);

	public static void handle(SyncSitPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			Minecraft minecraft = Minecraft.getInstance();
			Level level = minecraft.level;
			if (level == null)
				return;

			Player player = level.getPlayerByUUID(payload.uuid());
			if (player == null)
				return;

			if (payload.isSitting()) Personality.SYNCED_SITTING_PLAYERS.add(payload.uuid());
			else Personality.SYNCED_SITTING_PLAYERS.remove(payload.uuid());

			player.refreshDimensions();

			if (player == minecraft.player)
				PersonalityClientEvents.sitting = payload.isSitting();
		}).exceptionally(e -> null);
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}