package com.minecraftabnormals.personality.client;

import com.minecraftabnormals.personality.core.PersonalityConfig;
import net.minecraft.client.settings.IteratableOption;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.settings.ToggleableKeyBinding;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class PersonalityKeyBindings {

	public static final KeyBinding SIT = new KeyBinding("key.personality.sit", GLFW.GLFW_KEY_Z, "key.categories.gameplay");
	public static final KeyBinding CRAWL = new ToggleableKeyBinding("key.personality.crawl", GLFW.GLFW_KEY_C, "key.categories.gameplay", PersonalityConfig.CLIENT.keybinds.toggleCrawl::get);

	public static final IteratableOption CRAWL_OPTION = new IteratableOption(
			PersonalityKeyBindings.CRAWL.getKeyDescription(),
			(settings, value) -> PersonalityConfig.CLIENT.keybinds.toggleCrawl.set(!PersonalityConfig.CLIENT.keybinds.toggleCrawl.get()),
			(settings, option) -> option.func_238238_a_().append(new TranslationTextComponent(PersonalityConfig.CLIENT.keybinds.toggleCrawl.get() ? "options.key.toggle" : "options.key.hold"))
	);

	public static void registerKeyBinds() {
		ClientRegistry.registerKeyBinding(SIT);
		ClientRegistry.registerKeyBinding(CRAWL);
	}
}
