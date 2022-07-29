package com.teamabnormals.personality.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class PersonalityClient {
	private static final Component MOVEMENT_TOGGLE = Component.translatable("options.key.toggle");
	private static final Component MOVEMENT_HOLD = Component.translatable("options.key.hold");

	public static final OptionInstance<Boolean> TOGGLE_CRAWL = new OptionInstance<>(
			"key.personality.sit", OptionInstance.noTooltip(),
			(component, toggle) -> toggle ? MOVEMENT_TOGGLE : MOVEMENT_HOLD,
			OptionInstance.BOOLEAN_VALUES, true, (p_231875_) -> {
	});

	public static final OptionInstance<Boolean> TOGGLE_SIT = new OptionInstance<>(
			"key.personality.crawl", OptionInstance.noTooltip(),
			(component, toggle) -> toggle ? MOVEMENT_TOGGLE : MOVEMENT_HOLD,
			OptionInstance.BOOLEAN_VALUES, false, (p_231875_) -> {
	});

	public static final KeyMapping CRAWL = new ToggleKeyMapping("key.personality.crawl", GLFW.GLFW_KEY_C, "key.categories.movement", TOGGLE_CRAWL::get);
	public static final KeyMapping SIT = new ToggleKeyMapping("key.personality.sit", GLFW.GLFW_KEY_Z, "key.categories.movement", TOGGLE_SIT::get);
}
