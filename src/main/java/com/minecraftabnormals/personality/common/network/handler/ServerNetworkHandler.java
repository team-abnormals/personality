package com.minecraftabnormals.personality.common.network.handler;

import com.minecraftabnormals.personality.common.network.MessageC2SCrawl;
import com.minecraftabnormals.personality.common.network.MessageC2SSit;
import com.minecraftabnormals.personality.core.Personality;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerNetworkHandler {

	public static void handleProne(MessageC2SCrawl message, NetworkEvent.Context context) {
		ServerPlayerEntity player = context.getSender();
		IDataManager data = (IDataManager) player;

		if (data == null)
			return;

		if(player.isPassenger())
			return;

		data.setValue(Personality.CRAWLING, message.isCrawling());
	}

	public static void handleSitting(MessageC2SSit message, NetworkEvent.Context context) {
		ServerPlayerEntity player = context.getSender();
		IDataManager data = (IDataManager) player;

		if (data == null)
			return;

		if(player.isPassenger())
			return;

		data.setValue(Personality.SITTING, message.isSitting());
	}
}
