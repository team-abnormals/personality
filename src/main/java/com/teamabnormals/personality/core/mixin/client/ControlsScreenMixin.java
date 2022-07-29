package com.teamabnormals.personality.core.mixin.client;

import com.teamabnormals.personality.client.PersonalityClient;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ControlsScreen.class)
public abstract class ControlsScreenMixin extends OptionsSubScreen {

	public ControlsScreenMixin(Screen p_96284_, Options p_96285_, Component p_96286_) {
		super(p_96284_, p_96285_, p_96286_);
	}

	@ModifyVariable(method = "init", ordinal = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/controls/ControlsScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;", ordinal = 3, shift = At.Shift.AFTER))
	public int init(int y) {
		y += 24;
		int xLeft = this.width / 2 - 155;
		int xRight = xLeft + 160;

		this.addRenderableWidget(PersonalityClient.TOGGLE_SIT.createButton(this.options, xLeft, y, 150));
		this.addRenderableWidget(PersonalityClient.TOGGLE_CRAWL.createButton(this.options, xRight, y, 150));

		return y;
	}
}
