package com.minecraftabnormals.personality.common;

import com.minecraftabnormals.personality.core.Personality;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Personality.MOD_ID)
public class CommonEvents {

	@SubscribeEvent
	public static void onEvent(TickEvent.PlayerTickEvent event) {
		PlayerEntity player = event.player;

		if(player == null || player.world.isRemote)
			return;

		// TODO: add proning
	}
}
