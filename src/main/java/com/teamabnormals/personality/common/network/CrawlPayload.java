package com.teamabnormals.personality.common.network;

import com.teamabnormals.personality.core.Personality;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record CrawlPayload(boolean isCrawling) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<CrawlPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Personality.MOD_ID, "crawl"));

	public static final StreamCodec<ByteBuf, CrawlPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, CrawlPayload::isCrawling,
			CrawlPayload::new
	);

	public static void handle(CrawlPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> {
			if (context.player() instanceof ServerPlayer player) {
				UUID uuid = player.getUUID();
				if (!payload.isCrawling() || Personality.SITTING_PLAYERS.contains(uuid) || player.isPassenger()) {
					player.setForcedPose(null);
					PacketDistributor.sendToPlayer(player, new SyncCrawlPayload(uuid, false));
					return;
				}

				player.setForcedPose(Pose.SWIMMING);
				PacketDistributor.sendToPlayer(player, new SyncCrawlPayload(uuid, true));
			}
		}).exceptionally(e -> null);
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}