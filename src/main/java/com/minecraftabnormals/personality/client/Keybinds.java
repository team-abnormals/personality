package com.minecraftabnormals.personality.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybinds {

	public static final KeyBinding SIT = new KeyBinding("key.personality.sit", 90, "key.categories.gameplay");
	public static final KeyBinding CRAWL = new KeyBinding("key.personality.crawl", 67, "key.categories.gameplay");

	public static void registerKeyBinds() {
		ClientRegistry.registerKeyBinding(SIT);
		ClientRegistry.registerKeyBinding(CRAWL);
	}
}
