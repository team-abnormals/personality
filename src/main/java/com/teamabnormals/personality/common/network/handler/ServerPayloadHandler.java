package com.teamabnormals.personality.common.network.handler;

import com.teamabnormals.personality.common.network.CrawlPayload;
import com.teamabnormals.personality.common.network.SitPayload;
import com.teamabnormals.personality.common.network.SyncCrawlPayload;
import com.teamabnormals.personality.common.network.SyncSitPayload;
import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.other.PersonalityEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Set;
import java.util.UUID;

public class ServerPayloadHandler {

	public static void handleCrawl(CrawlPayload payload, IPayloadContext context) {
		if (context.player() instanceof ServerPlayer player) {
			UUID uuid = player.getUUID();
			if (!payload.isCrawling() || Personality.SITTING_PLAYERS.contains(uuid) || player.isPassenger()) {
				player.setForcedPose(null);
				PacketDistributor.sendToPlayer(player, new SyncCrawlPayload(uuid, false));
				return;
			}

			player.setForcedPose(Pose.SWIMMING);
			PacketDistributor.sendToPlayer(player, new SyncCrawlPayload(uuid, true));
		}
	}

	public static void handleSit(SitPayload payload, IPayloadContext context) {
		if (context.player() instanceof ServerPlayer player) {
			UUID uuid = player.getUUID();
			Set<UUID> players = Personality.SITTING_PLAYERS;

			if (!payload.isSitting() || !PersonalityEvents.testSit(player)) {
				players.remove(player.getUUID());
				player.refreshDimensions();
				PacketDistributor.sendToPlayer(player, new SyncSitPayload(uuid, false));
				return;
			}

			players.add(player.getUUID());
			player.refreshDimensions();
			PacketDistributor.sendToPlayer(player, new SyncSitPayload(uuid, true));
		}
	}
}
