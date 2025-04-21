package com.teamabnormals.personality.common.network;

import com.teamabnormals.personality.common.network.handler.ServerPayloadHandler;
import com.teamabnormals.personality.core.Personality;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SitPayload(boolean isSitting) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SitPayload> TYPE = new CustomPacketPayload.Type<>(Personality.location("sit"));

	public static final StreamCodec<ByteBuf, SitPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, SitPayload::isSitting,
			SitPayload::new
	);

	public static void handle(SitPayload payload, IPayloadContext context) {
		context.enqueueWork(() -> ServerPayloadHandler.handleSit(payload, context)).exceptionally(e -> null);
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}