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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class PersonalityClient {
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

    private static final Logger LOGGER = LogManager.getLogger();

    public static void registerKeyBinds() {
        CRAWL.setKeyConflictContext(KeyConflictContext.IN_GAME);
        SIT.setKeyConflictContext(KeyConflictContext.IN_GAME);
        ClientRegistry.registerKeyBinding(CRAWL);
        ClientRegistry.registerKeyBinding(SIT);
    }

    public static void addAccessibilityOptions() {
        try {
            AccessibilityScreen.OPTIONS = addButtons(AccessibilityScreen.OPTIONS, 10, PersonalityClient.CRAWL_OPTION, PersonalityClient.SIT_OPTION);
        } catch (Exception e) {
            LOGGER.error("Error adding options to AccessibilityScreen", e);
        }
    }

    private static AbstractOption[] addButtons(AbstractOption[] src, int startingIndex, AbstractOption... options) {
        int index = startingIndex;
        for (AbstractOption option : options) {
            AbstractOption[] tmp = new AbstractOption[src.length + 1];

            System.arraycopy(src, 0, tmp, 0, index);
            tmp[index] = option;
            System.arraycopy(src, index, tmp, index + 1, src.length - index);
            src = tmp;
            index++;
        }
        return src;
    }
}
