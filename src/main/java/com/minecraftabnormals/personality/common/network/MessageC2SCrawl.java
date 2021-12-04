package com.minecraftabnormals.personality.common.network;

import com.minecraftabnormals.personality.common.network.handler.ServerNetHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public final class MessageC2SCrawl {
	private final boolean crawl;

	public MessageC2SCrawl(boolean crawl) {
		this.crawl = crawl;
	}

	public static MessageC2SCrawl deserialize(PacketBuffer buf) {
		return new MessageC2SCrawl(buf.readBoolean());
	}

	public static void handle(MessageC2SCrawl message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> ServerNetHandler.handleCrawl(message, context));
			context.setPacketHandled(true);
		}
	}

	public void serialize(PacketBuffer buf) {
		buf.writeBoolean(this.crawl);
	}

	public boolean isCrawling() {
		return crawl;
	}
}