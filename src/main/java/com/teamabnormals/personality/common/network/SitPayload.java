package com.teamabnormals.personality.common.network;

import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.other.PersonalityEvents;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Set;
import java.util.UUID;

public record SitPayload(boolean isSitting) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SitPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Personality.MOD_ID, "sit"));

	public static final StreamCodec<ByteBuf, SitPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, SitPayload::isSitting,
			SitPayload::new
	);

	public static void handle(SitPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				UUID uuid = player.getUUID();
				Set<UUID> players = Personality.SITTING_PLAYERS;

				if (!payload.isSitting() || !PersonalityEvents.testSit(player)) {
					players.remove(player.getUUID());
					player.refreshDimensions();
					PacketDistributor.sendToPlayer(player, new SyncSitPayload(uuid, false));
					return;
				}

				players.add(player.getUUID());
				player.refreshDimensions();
				PacketDistributor.sendToPlayer(player, new SyncSitPayload(uuid, true));
			}
		}).exceptionally(e -> null);
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}