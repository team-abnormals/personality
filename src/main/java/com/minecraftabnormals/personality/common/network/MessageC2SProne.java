package com.minecraftabnormals.personality.common.network;

import com.minecraftabnormals.personality.common.network.handler.ServerNetworkHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public final class MessageC2SProne {
	private final boolean prone;

	public MessageC2SProne(boolean prone) {
		this.prone = prone;
	}

	public void serialize(PacketBuffer buf) {
		buf.writeBoolean(this.prone);
	}

	public static MessageC2SProne deserialize(PacketBuffer buf) {
		return new MessageC2SProne(buf.readBoolean());
	}

	public static void handle(MessageC2SProne message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> ServerNetworkHandler.handleProne(message, context));
			context.setPacketHandled(true);
		}
	}

	public boolean isProning() {
		return this.prone;
	}
}