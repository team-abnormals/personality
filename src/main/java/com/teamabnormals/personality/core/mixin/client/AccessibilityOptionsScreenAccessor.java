package com.teamabnormals.personality.core.mixin.client;

import net.minecraft.client.Option;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AccessibilityOptionsScreen.class)
public interface AccessibilityOptionsScreenAccessor {

    @Mutable
    @Accessor("OPTIONS")
    static Option[] getOptions() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("OPTIONS")
    static void setOptions(Option[] options) {
        throw new AssertionError();
    }
}
