package com.minecraftabnormals.personality.common.network;

import com.minecraftabnormals.personality.common.network.handler.ClientNetHandler;
import net.minecraft.network.PacketBuffer;
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
			context.enqueueWork(() -> ClientNetHandler.handleSitSync(message, context));
			context.setPacketHandled(true);
		}
	}

	public void serialize(PacketBuffer buf) {
		buf.writeUniqueId(this.uuid);
		buf.writeBoolean(this.sitting);
	}

	public UUID getUUID() {
		return uuid;
	}

	public boolean isSitting() {
		return sitting;
	}
}