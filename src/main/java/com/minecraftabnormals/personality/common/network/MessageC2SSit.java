package com.minecraftabnormals.personality.common.network;

import com.minecraftabnormals.personality.common.CommonEvents;
import com.minecraftabnormals.personality.core.Personality;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

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
				if (player == null)
					return;

				UUID uuid = player.getUniqueID();
				Set<UUID> players = Personality.SITTING_PLAYERS;

				if (!message.sit || !CommonEvents.testSit(player)) {
					players.remove(player.getUniqueID());
					player.recalculateSize();
					Personality.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MessageS2CSyncSit(uuid, false));
					return;
				}

				players.add(player.getUniqueID());
				player.recalculateSize();
				Personality.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MessageS2CSyncSit(uuid, true));
			});
			context.setPacketHandled(true);
		}
	}

	public void serialize(PacketBuffer buf) {
		buf.writeBoolean(this.sit);
	}
}