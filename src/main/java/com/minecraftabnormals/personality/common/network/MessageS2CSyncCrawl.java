package com.minecraftabnormals.personality.common.network;

import com.minecraftabnormals.personality.client.ClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public final class MessageS2CSyncCrawl {
	private final UUID uuid;
	private final boolean crawl;

	public MessageS2CSyncCrawl(UUID uuid, boolean crawl) {
		this.uuid = uuid;
		this.crawl = crawl;
	}

	public static MessageS2CSyncCrawl deserialize(PacketBuffer buf) {
		return new MessageS2CSyncCrawl(buf.readUniqueId(), buf.readBoolean());
	}

	public static void handle(MessageS2CSyncCrawl message, Supplier<NetworkEvent.Context> ctx) {
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

				player.setForcedPose(message.crawl ? Pose.SWIMMING : null);

				if (player == minecraft.player)
					ClientEvents.crawling = message.crawl;
			});
			context.setPacketHandled(true);
		}
	}

	public void serialize(PacketBuffer buf) {
		buf.writeUniqueId(this.uuid);
		buf.writeBoolean(this.crawl);
	}
}