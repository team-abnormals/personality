package com.teamabnormals.personality.common.network.handler;

import com.teamabnormals.personality.common.network.MessageS2CSyncCrawl;
import com.teamabnormals.personality.common.network.MessageS2CSyncSit;
import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.other.PersonalityClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class ClientNetHandler {

	public static void handleCrawlSync(MessageS2CSyncCrawl message, NetworkEvent.Context context) {
		Minecraft minecraft = Minecraft.getInstance();
		Level level = minecraft.level;
		if (level == null)
			return;

		Player player = level.getPlayerByUUID(message.getUUID());
		if (player == null)
			return;

		player.setForcedPose(message.isCrawling() ? Pose.SWIMMING : null);

		if (player == minecraft.player)
			PersonalityClientEvents.crawling = message.isCrawling();
	}

	public static void handleSitSync(MessageS2CSyncSit message, NetworkEvent.Context context) {
		Minecraft minecraft = Minecraft.getInstance();
		Level level = minecraft.level;
		if (level == null)
			return;

		Player player = level.getPlayerByUUID(message.getUUID());
		if (player == null)
			return;

		if (message.isSitting()) Personality.SYNCED_SITTING_PLAYERS.add(message.getUUID());
		else Personality.SYNCED_SITTING_PLAYERS.remove(message.getUUID());

		player.refreshDimensions();

		if (player == minecraft.player)
			PersonalityClientEvents.sitting = message.isSitting();
	}
}
