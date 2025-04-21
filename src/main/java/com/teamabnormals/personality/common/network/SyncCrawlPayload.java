package com.teamabnormals.personality.common.network;

import com.teamabnormals.personality.common.network.handler.ClientPayloadHandler;
import com.teamabnormals.personality.core.Personality;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record SyncCrawlPayload(UUID uuid, boolean isCrawling) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SyncCrawlPayload> TYPE = new CustomPacketPayload.Type<>(Personality.location("sync_crawl"));

	public static final StreamCodec<ByteBuf, SyncCrawlPayload> STREAM_CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, SyncCrawlPayload::uuid,
			ByteBufCodecs.BOOL, SyncCrawlPayload::isCrawling,
			SyncCrawlPayload::new
	);

	public static void handle(SyncCrawlPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> ClientPayloadHandler.handleCrawlSync(payload)).exceptionally(e -> null);
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}