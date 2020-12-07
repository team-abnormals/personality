package com.minecraftabnormals.personality.common.network.handler;

import com.minecraftabnormals.personality.client.ClientEvents;
import com.minecraftabnormals.personality.common.network.MessageS2CSyncCrawl;
import com.minecraftabnormals.personality.common.network.MessageS2CSyncSit;
import com.minecraftabnormals.personality.core.Personality;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class ClientNetHandler {

	public static void handleCrawlSync(MessageS2CSyncCrawl message, NetworkEvent.Context context) {
		Minecraft minecraft = Minecraft.getInstance();
		World world = minecraft.world;
		if (world == null)
			return;

		PlayerEntity player = world.getPlayerByUuid(message.getUUID());
		if (player == null)
			return;

		player.setForcedPose(message.isCrawling() ? Pose.SWIMMING : null);

		if (player == minecraft.player)
			ClientEvents.crawling = message.isCrawling();
	}

	public static void handleSitSync(MessageS2CSyncSit message, NetworkEvent.Context context) {
		Minecraft minecraft = Minecraft.getInstance();
		World world = minecraft.world;
		if (world == null)
			return;

		PlayerEntity player = world.getPlayerByUuid(message.getUUID());
		if (player == null)
			return;

		if (message.isSitting()) Personality.SITTING_PLAYERS.add(message.getUUID());
		else Personality.SITTING_PLAYERS.remove(message.getUUID());

		player.recalculateSize();

		if (player == minecraft.player)
			ClientEvents.sitting = message.isSitting();
	}
}
