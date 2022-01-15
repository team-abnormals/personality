package com.teamabnormals.personality.common.network;

import com.teamabnormals.personality.common.network.handler.ServerNetHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class MessageC2SCrawl {
	private final boolean crawl;

	public MessageC2SCrawl(boolean crawl) {
		this.crawl = crawl;
	}

	public static MessageC2SCrawl deserialize(FriendlyByteBuf buf) {
		return new MessageC2SCrawl(buf.readBoolean());
	}

	public static void handle(MessageC2SCrawl message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> ServerNetHandler.handleCrawl(message, context));
			context.setPacketHandled(true);
		}
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeBoolean(this.crawl);
	}

	public boolean isCrawling() {
		return crawl;
	}
}