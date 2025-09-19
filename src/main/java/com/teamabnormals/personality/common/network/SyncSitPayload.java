package com.teamabnormals.personality.common.network;

import com.teamabnormals.personality.common.network.handler.ClientPayloadHandler;
import com.teamabnormals.personality.core.Personality;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record SyncSitPayload(UUID uuid, boolean isSitting) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<SyncSitPayload> TYPE = new CustomPacketPayload.Type<>(Personality.location("sync_sit"));

	public static final StreamCodec<ByteBuf, SyncSitPayload> STREAM_CODEC = StreamCodec.composite(
			UUIDUtil.STREAM_CODEC, SyncSitPayload::uuid,
			ByteBufCodecs.BOOL, SyncSitPayload::isSitting,
			SyncSitPayload::new
	);

	public static void handle(SyncSitPayload payload, IPayloadContext context) {
		if (context.connection().getDirection() == PacketFlow.CLIENTBOUND) {
			context.enqueueWork(() -> ClientPayloadHandler.handleSitSync(payload)).exceptionally(e -> null);
		}
	}

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}