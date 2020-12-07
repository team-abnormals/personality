package com.minecraftabnormals.personality.client;

import com.minecraftabnormals.personality.core.PersonalityConfig;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.gui.AccessibilityScreen;
import net.minecraft.client.settings.IteratableOption;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.settings.ToggleableKeyBinding;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PersonalityClient {
	public static final Set<UUID> SITTING_PLAYERS = new HashSet<>();
	public static final KeyBinding CRAWL = new ToggleableKeyBinding("key.personality.crawl", GLFW.GLFW_KEY_C, "key.categories.gameplay", PersonalityConfig.CLIENT.keybinds.toggleCrawl::get);
	public static final KeyBinding SIT = new ToggleableKeyBinding("key.personality.sit", GLFW.GLFW_KEY_Z, "key.categories.gameplay", PersonalityConfig.CLIENT.keybinds.toggleSitting::get);

	public static final IteratableOption CRAWL_OPTION = new IteratableOption(
			PersonalityClient.CRAWL.getKeyDescription(),
			(settings, value) -> PersonalityConfig.CLIENT.keybinds.toggleCrawl.set(!PersonalityConfig.CLIENT.keybinds.toggleCrawl.get()),
			(settings, option) -> new TranslationTextComponent("options.generic_value", new TranslationTextComponent(PersonalityClient.CRAWL.getKeyDescription()), new TranslationTextComponent(PersonalityConfig.CLIENT.keybinds.toggleCrawl.get() ? "options.key.toggle" : "options.key.hold"))
	);
	public static final IteratableOption SIT_OPTION = new IteratableOption(
			PersonalityClient.SIT.getKeyDescription(),
			(settings, value) -> PersonalityConfig.CLIENT.keybinds.toggleSitting.set(!PersonalityConfig.CLIENT.keybinds.toggleSitting.get()),
			(settings, option) -> new TranslationTextComponent("options.generic_value", new TranslationTextComponent(PersonalityClient.SIT.getKeyDescription()), new TranslationTextComponent(PersonalityConfig.CLIENT.keybinds.toggleSitting.get() ? "options.key.toggle" : "options.key.hold"))
	);

	static {
		addAccessibilityOptions();
	}

	public static void registerKeyBinds() {
		CRAWL.setKeyConflictContext(KeyConflictContext.IN_GAME);
		SIT.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(CRAWL);
		ClientRegistry.registerKeyBinding(SIT);
	}

	private static void addAccessibilityOptions() {
		try {
			Field optionsField = ObfuscationReflectionHelper.findField(AccessibilityScreen.class, "field_212986_a");
			Field modifiedField = Field.class.getDeclaredField("modifiers");
			modifiedField.setAccessible(true);
			modifiedField.setInt(optionsField, optionsField.getModifiers() & ~Modifier.FINAL);

			AbstractOption[] options = (AbstractOption[]) optionsField.get(null);

			if (options == null)
				throw new NullPointerException("Accessibility options were null.");

			optionsField.set(null, addButtons(options, 10, PersonalityClient.CRAWL_OPTION, PersonalityClient.SIT_OPTION));
		} catch (Exception e) {
			throw new RuntimeException("Failed to add accessibility option.", e);
		}
	}

	private static AbstractOption[] addButtons(AbstractOption[] src, int startingIndex, AbstractOption... options) {
		int i = startingIndex;
		for (AbstractOption option : options)
			src = ArrayUtils.insert(i++, src, option);
		return src;
	}
}
