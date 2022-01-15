package com.teamabnormals.personality.common.network;

import com.teamabnormals.personality.common.network.handler.ClientNetHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public final class MessageS2CSyncCrawl {
	private final UUID uuid;
	private final boolean crawl;

	public MessageS2CSyncCrawl(UUID uuid, boolean crawl) {
		this.uuid = uuid;
		this.crawl = crawl;
	}

	public static MessageS2CSyncCrawl deserialize(FriendlyByteBuf buf) {
		return new MessageS2CSyncCrawl(buf.readUUID(), buf.readBoolean());
	}

	public static void handle(MessageS2CSyncCrawl message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> ClientNetHandler.handleCrawlSync(message, context));
			context.setPacketHandled(true);
		}
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeUUID(this.uuid);
		buf.writeBoolean(this.crawl);
	}

	public UUID getUUID() {
		return uuid;
	}

	public boolean isCrawling() {
		return crawl;
	}
}