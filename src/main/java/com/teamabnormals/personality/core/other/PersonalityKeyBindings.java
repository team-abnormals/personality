package com.teamabnormals.personality.core.other;

import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Personality.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class PersonalityKeyBindings {
	private static final Component MOVEMENT_TOGGLE = Component.translatable("options.key.toggle");
	private static final Component MOVEMENT_HOLD = Component.translatable("options.key.hold");

	public static final OptionInstance<Boolean> TOGGLE_CRAWL = new OptionInstance<>(
			"key.personality.crawl", OptionInstance.noTooltip(),
			(component, toggle) -> toggle ? MOVEMENT_TOGGLE : MOVEMENT_HOLD,
			OptionInstance.BOOLEAN_VALUES, false, value -> {
		PersonalityConfig.CLIENT.toggleCrawl.set(value);
		PersonalityConfig.CLIENT_SPEC.save();
	});

	public static final OptionInstance<Boolean> TOGGLE_SIT = new OptionInstance<>(
			"key.personality.sit", OptionInstance.noTooltip(),
			(component, toggle) -> toggle ? MOVEMENT_TOGGLE : MOVEMENT_HOLD,
			OptionInstance.BOOLEAN_VALUES, false, value -> {
		PersonalityConfig.CLIENT.toggleSitting.set(value);
		PersonalityConfig.CLIENT_SPEC.save();
	});

	public static final KeyMapping CRAWL = new ToggleKeyMapping("key.personality.crawl", GLFW.GLFW_KEY_C, "key.categories.movement", TOGGLE_CRAWL::get);
	public static final KeyMapping SIT = new ToggleKeyMapping("key.personality.sit", GLFW.GLFW_KEY_Z, "key.categories.movement", TOGGLE_SIT::get);

	public static void setupToggleSettings() {
		TOGGLE_CRAWL.set(PersonalityConfig.CLIENT.toggleCrawl.get());
		TOGGLE_SIT.set(PersonalityConfig.CLIENT.toggleSitting.get());
	}

	@SubscribeEvent
	public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
		CRAWL.setKeyConflictContext(KeyConflictContext.IN_GAME);
		SIT.setKeyConflictContext(KeyConflictContext.IN_GAME);
		event.register(CRAWL);
		event.register(SIT);
	}
}
