package com.minecraftabnormals.personality.common.network;

import com.minecraftabnormals.personality.core.Personality;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Set;
import java.util.UUID;
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
			context.enqueueWork(() -> {
				ServerPlayerEntity player = context.getSender();
				if(player == null)
					return;
				
				UUID uuid = player.getUniqueID();
				if(Personality.SITTING_PLAYERS.contains(uuid) || player.isPassenger() || player.openContainer != null) 
					return;

				Set<UUID> players = Personality.CRAWLING_PLAYERS;
				
				if(message.crawl) players.add(player.getUniqueID());
				else players.remove(player.getUniqueID());
			});
			context.setPacketHandled(true);
		}
	}

	public void serialize(PacketBuffer buf) {
		buf.writeBoolean(this.crawl);
	}
}