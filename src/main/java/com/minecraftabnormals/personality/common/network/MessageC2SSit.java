package com.minecraftabnormals.personality.common.network;

import com.minecraftabnormals.personality.core.Personality;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public final class MessageC2SSit {
	private final boolean sit;

	public MessageC2SSit(boolean sit) {
		this.sit = sit;
	}

	public static MessageC2SSit deserialize(PacketBuffer buf) {
		return new MessageC2SSit(buf.readBoolean());
	}

	public static void handle(MessageC2SSit message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				ServerPlayerEntity player = context.getSender();
				if(player == null)
					return;

				UUID uuid = player.getUniqueID();
				if(Personality.CRAWLING_PLAYERS.contains(uuid) || !player.isOnGround() || player.isPassenger() || player.openContainer != null)
					return;

				Set<UUID> players = Personality.SITTING_PLAYERS;
				if(message.sit) players.add(player.getUniqueID());
				else players.remove(player.getUniqueID());
			});
			context.setPacketHandled(true);
		}
	}

	public void serialize(PacketBuffer buf) {
		buf.writeBoolean(this.sit);
	}
}