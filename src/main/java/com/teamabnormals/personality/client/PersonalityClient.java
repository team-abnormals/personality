package com.teamabnormals.personality.client;

import com.teamabnormals.personality.core.PersonalityConfig;
import com.teamabnormals.personality.core.mixin.client.AccessibilityOptionsScreenAccessor;
import net.minecraft.client.CycleOption;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Option;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class PersonalityClient {
	public static final KeyMapping CRAWL = new ToggleKeyMapping("key.personality.crawl", GLFW.GLFW_KEY_C, "key.categories.gameplay", PersonalityConfig.CLIENT.keybinds.toggleCrawl::get);
	public static final KeyMapping SIT = new ToggleKeyMapping("key.personality.sit", GLFW.GLFW_KEY_Z, "key.categories.gameplay", PersonalityConfig.CLIENT.keybinds.toggleSitting::get);

	private static final Component MOVEMENT_TOGGLE = new TranslatableComponent("options.key.toggle");
	private static final Component MOVEMENT_HOLD = new TranslatableComponent("options.key.hold");

	public static final CycleOption<Boolean> TOGGLE_CRAWL = CycleOption.createBinaryOption(
			PersonalityClient.CRAWL.getName(),
			MOVEMENT_TOGGLE,
			MOVEMENT_HOLD,
			(options) -> PersonalityConfig.CLIENT.keybinds.toggleCrawl.get(),
			(options, option, value) ->  PersonalityConfig.CLIENT.keybinds.toggleCrawl.set(value)
	);

	public static final CycleOption<Boolean> TOGGLE_SIT = CycleOption.createBinaryOption(
			PersonalityClient.SIT.getName(),
			MOVEMENT_TOGGLE,
			MOVEMENT_HOLD,
			(options) -> PersonalityConfig.CLIENT.keybinds.toggleCrawl.get(),
			(options, option, value) ->  PersonalityConfig.CLIENT.keybinds.toggleCrawl.set(value)
	);

	private static final Logger LOGGER = LogManager.getLogger();

	public static void registerKeyBinds() {
		CRAWL.setKeyConflictContext(KeyConflictContext.IN_GAME);
		SIT.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(CRAWL);
		ClientRegistry.registerKeyBinding(SIT);
	}

	public static void addAccessibilityOptions() {
		try {
			AccessibilityOptionsScreenAccessor.setOptions(addButtons(AccessibilityOptionsScreenAccessor.getOptions(), 10, PersonalityClient.TOGGLE_CRAWL, PersonalityClient.TOGGLE_SIT));
		} catch (Exception e) {
			LOGGER.error("Error adding options to AccessibilityScreen", e);
		}
	}

	private static Option[] addButtons(Option[] src, int startingIndex, Option... options) {
		int index = startingIndex;
		for (Option option : options) {
			Option[] tmp = new Option[src.length + 1];

			System.arraycopy(src, 0, tmp, 0, index);
			tmp[index] = option;
			System.arraycopy(src, index, tmp, index + 1, src.length - index);
			src = tmp;
			index++;
		}
		return src;
	}
}
