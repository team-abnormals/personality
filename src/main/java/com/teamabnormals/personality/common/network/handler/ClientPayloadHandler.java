package com.teamabnormals.personality.common.network.handler;

import com.teamabnormals.personality.common.network.SyncCrawlPayload;
import com.teamabnormals.personality.common.network.SyncSitPayload;
import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.other.PersonalityClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ClientPayloadHandler {

	public static void handleCrawlSync(SyncCrawlPayload payload) {
		Minecraft minecraft = Minecraft.getInstance();
		Level level = minecraft.level;
		if (level == null)
			return;

		Player player = level.getPlayerByUUID(payload.uuid());
		if (player == null)
			return;

		player.setForcedPose(payload.isCrawling() ? Pose.SWIMMING : null);

		if (player == minecraft.player) {
			PersonalityClientEvents.crawling = payload.isCrawling();
		}
	}

	public static void handleSitSync(SyncSitPayload payload) {
		Minecraft minecraft = Minecraft.getInstance();
		Level level = minecraft.level;
		if (level == null)
			return;

		Player player = level.getPlayerByUUID(payload.uuid());
		if (player == null)
			return;

		if (payload.isSitting()) Personality.SYNCED_SITTING_PLAYERS.add(payload.uuid());
		else Personality.SYNCED_SITTING_PLAYERS.remove(payload.uuid());

		player.refreshDimensions();

		if (player == minecraft.player) {
			PersonalityClientEvents.sitting = payload.isSitting();
		}
	}
}
