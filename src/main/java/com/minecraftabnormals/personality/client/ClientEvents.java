package com.minecraftabnormals.personality.client;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.ArrayUtils;

import com.minecraftabnormals.personality.common.network.MessageC2SCrawl;
import com.minecraftabnormals.personality.common.network.MessageC2SSit;
import com.minecraftabnormals.personality.core.Personality;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.IDataManager;

import net.minecraft.client.AbstractOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AccessibilityScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@Mod.EventBusSubscriber(modid = Personality.MODID, value = Dist.CLIENT)
public class ClientEvents {

	static {
		addAccessibilityOptions();
	}

	@SubscribeEvent
	public static void onEvent(InputEvent.KeyInputEvent event) {
		PlayerEntity player = Minecraft.getInstance().player;
		IDataManager data = (IDataManager) player;

		if (data == null)
			return;

		boolean crawling = data.getValue(Personality.CRAWLING);
		boolean sitting = data.getValue(Personality.SITTING);

		if (PersonalityKeyBindings.CRAWL.isKeyDown()) {
			if (!crawling)
				Personality.CHANNEL.sendToServer(new MessageC2SCrawl(true));
		} else if (crawling) {
			Personality.CHANNEL.sendToServer(new MessageC2SCrawl(false));
		}

		if (PersonalityKeyBindings.SIT.isKeyDown()) {
			if (!sitting)
				Personality.CHANNEL.sendToServer(new MessageC2SSit(true));
		} else if (sitting) {
			Personality.CHANNEL.sendToServer(new MessageC2SSit(false));
		}
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

			optionsField.set(null, addButtons(options, PersonalityKeyBindings.CRAWL_OPTION, PersonalityKeyBindings.SIT_OPTION));
		} catch (Exception e) {
			throw new RuntimeException("Failed to add accessibility option.", e);
		}
	}

	private static AbstractOption[] addButtons(AbstractOption[] src, AbstractOption... options) {
		for (AbstractOption option : options)
			src = ArrayUtils.add(src, option);
		return src;
	}
	
	@SubscribeEvent
	public static void onEvent(RenderPlayerEvent.Pre event) {
		PlayerEntity player = event.getPlayer();
		IDataManager data = (IDataManager) player;

		if (data.getValue(Personality.SITTING) && !data.getValue(Personality.CRAWLING) && !player.isPassenger())
			event.getMatrixStack().translate(0.0F, -0.55F, 0.0F);
	}
}