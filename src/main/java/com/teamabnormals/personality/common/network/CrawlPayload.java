package com.teamabnormals.personality.common.network;

import com.teamabnormals.personality.common.network.handler.ServerPayloadHandler;
import com.teamabnormals.personality.core.Personality;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CrawlPayload(boolean isCrawling) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<CrawlPayload> TYPE = new CustomPacketPayload.Type<>(Personality.location("crawl"));

	public static final StreamCodec<ByteBuf, CrawlPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, CrawlPayload::isCrawling,
			CrawlPayload::new
	);

	public static void handle(CrawlPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> ServerPayloadHandler.handleCrawl(payload, context)).exceptionally(e -> null);
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}