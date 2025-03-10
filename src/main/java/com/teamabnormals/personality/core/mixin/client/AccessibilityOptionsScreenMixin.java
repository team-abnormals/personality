package com.teamabnormals.personality.core.mixin.client;

import com.teamabnormals.personality.client.PersonalityClient;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = {
		"net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen",
		"net.minecraft.client.gui.screens.options.controls.ControlsScreen"
})
public abstract class AccessibilityOptionsScreenMixin {


	@Inject(method = "options", at = @At("RETURN"), cancellable = true)
	private static void options(Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
		List<OptionInstance<?>> optionsArray = Lists.newArrayList();
		for (OptionInstance<?> option : cir.getReturnValue()) {
			optionsArray.add(option);
			if (option == options.toggleSprint()) {
				optionsArray.add(PersonalityClient.TOGGLE_CRAWL);
				optionsArray.add(PersonalityClient.TOGGLE_SIT);
			}
		}
		cir.setReturnValue(optionsArray.toArray(new OptionInstance[0]));
	}
}
