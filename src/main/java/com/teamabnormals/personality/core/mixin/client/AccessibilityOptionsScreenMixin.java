package com.teamabnormals.personality.core.mixin.client;

import com.teamabnormals.personality.client.PersonalityClient;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(AccessibilityOptionsScreen.class)
public abstract class AccessibilityOptionsScreenMixin {

	@Inject(method = "options", at = @At("RETURN"), cancellable = true)
	private static void stopCrouch(Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
		ArrayList<OptionInstance<?>> newOptions = new ArrayList<>(Arrays.stream(cir.getReturnValue()).toList());
		newOptions.add(10, PersonalityClient.TOGGLE_CRAWL);
		newOptions.add(11, PersonalityClient.TOGGLE_SIT);
		cir.setReturnValue(newOptions.toArray(OptionInstance[]::new));
	}
}
