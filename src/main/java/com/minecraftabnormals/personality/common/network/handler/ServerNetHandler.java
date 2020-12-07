package com.minecraftabnormals.personality.common.network.handler;

import com.minecraftabnormals.personality.common.CommonEvents;
import com.minecraftabnormals.personality.common.network.MessageC2SCrawl;
import com.minecraftabnormals.personality.common.network.MessageC2SSit;
import com.minecraftabnormals.personality.common.network.MessageS2CSyncCrawl;
import com.minecraftabnormals.personality.common.network.MessageS2CSyncSit;
import com.minecraftabnormals.personality.core.Personality;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Set;
import java.util.UUID;

public class ServerNetHandler {
	
	public static void handleCrawl(MessageC2SCrawl message, NetworkEvent.Context context) {
		ServerPlayerEntity player = context.getSender();
		if (player == null)
			return;

		UUID uuid = player.getUniqueID();
		if (!message.isCrawling() || Personality.SITTING_PLAYERS.contains(uuid) || player.isPassenger()) {
			player.setForcedPose(null);
			Personality.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MessageS2CSyncCrawl(uuid, false));
			return;
		}

		player.setForcedPose(Pose.SWIMMING);
		Personality.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new MessageS2CSyncCrawl(uuid, true));
	}
	
	public static void handleSit(MessageC2SSit message, NetworkEvent.Context context) {
		ServerPlayerEntity player = context.getSender();
		if (player == null)
			return;

		UUID uuid = player.getUniqueID();
		Set<UUID> players = Personality.SITTING_PLAYERS;

		if (!message.isSitting() || !CommonEvents.testSit(player)) {
			players.remove(player.getUniqueID());
			player.recalculateSize();
			Personality.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MessageS2CSyncSit(uuid, false));
			return;
		}

		players.add(player.getUniqueID());
		player.recalculateSize();
		Personality.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MessageS2CSyncSit(uuid, true));
	}
}
