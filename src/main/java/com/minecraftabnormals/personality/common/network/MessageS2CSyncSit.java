package com.minecraftabnormals.personality.common.network;

import com.minecraftabnormals.personality.client.ClientEvents;
import com.minecraftabnormals.personality.client.PersonalityClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public final class MessageS2CSyncSit {
	private final UUID uuid;
	private final boolean sitting;

	public MessageS2CSyncSit(UUID uuid, boolean sitting) {
		this.uuid = uuid;
		this.sitting = sitting;
	}

	public static MessageS2CSyncSit deserialize(PacketBuffer buf) {
		return new MessageS2CSyncSit(buf.readUniqueId(), buf.readBoolean());
	}

	public static void handle(MessageS2CSyncSit message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				Minecraft minecraft = Minecraft.getInstance();
				World world = minecraft.world;
				if (world == null)
					return;

				PlayerEntity player = world.getPlayerByUuid(message.uuid);
				if (player == null)
					return;

				if (message.sitting) PersonalityClient.SITTING_PLAYERS.add(message.uuid);
				else PersonalityClient.SITTING_PLAYERS.remove(message.uuid);

				player.recalculateSize();

				if (player == minecraft.player)
					ClientEvents.sitting = message.sitting;
			});
			context.setPacketHandled(true);
		}
	}

	public void serialize(PacketBuffer buf) {
		buf.writeUniqueId(this.uuid);
		buf.writeBoolean(this.sitting);
	}
}