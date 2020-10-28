package com.minecraftabnormals.personality.client;

import com.minecraftabnormals.personality.core.Personality;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Personality.MODID, value = Dist.CLIENT)
public class ClientEvents {

	@SubscribeEvent
	public static void onEvent(InputEvent.KeyInputEvent event) {
		//TODO: Check for proning key input
	}
}