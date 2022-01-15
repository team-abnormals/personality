package com.teamabnormals.personality.common.network.handler;

import com.teamabnormals.personality.common.CommonEvents;
import com.teamabnormals.personality.common.network.MessageC2SCrawl;
import com.teamabnormals.personality.common.network.MessageC2SSit;
import com.teamabnormals.personality.common.network.MessageS2CSyncCrawl;
import com.teamabnormals.personality.common.network.MessageS2CSyncSit;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.Set;
import java.util.UUID;

public class ServerNetHandler {

	public static void handleCrawl(MessageC2SCrawl message, NetworkEvent.Context context) {
		ServerPlayer player = context.getSender();
		if (player == null)
			return;

		UUID uuid = player.getUUID();
		if (!message.isCrawling() || Personality.SITTING_PLAYERS.contains(uuid) || player.isPassenger()) {
			player.setForcedPose(null);
			Personality.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new MessageS2CSyncCrawl(uuid, false));
			return;
		}

		player.setForcedPose(Pose.SWIMMING);
		Personality.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new MessageS2CSyncCrawl(uuid, true));
	}

	public static void handleSit(MessageC2SSit message, NetworkEvent.Context context) {
		ServerPlayer player = context.getSender();
		if (player == null)
			return;

		UUID uuid = player.getUUID();
		Set<UUID> players = Personality.SITTING_PLAYERS;

		if (!message.isSitting() || !CommonEvents.testSit(player)) {
			players.remove(player.getUUID());
			player.refreshDimensions();
			Personality.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new MessageS2CSyncSit(uuid, false));
			return;
		}

		players.add(player.getUUID());
		player.refreshDimensions();
		Personality.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), new MessageS2CSyncSit(uuid, true));
	}
}
