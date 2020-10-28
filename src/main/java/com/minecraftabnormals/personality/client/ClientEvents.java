package com.minecraftabnormals.personality.client;

import com.minecraftabnormals.personality.common.network.MessageC2SCrawl;
import com.minecraftabnormals.personality.common.network.MessageC2SSit;
import com.minecraftabnormals.personality.core.Personality;
import com.minecraftabnormals.personality.core.PersonalityConfig;
import com.minecraftabnormals.personality.core.accessor.EntityRendererAccessor;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AccessibilityScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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

	@SubscribeEvent
	public static void onEvent(RenderNameplateEvent event) {
		MatrixStack stack = event.getMatrixStack();
		IRenderTypeBuffer buffer = event.getRenderTypeBuffer();
		EntityRenderer<?> renderer = event.getEntityRenderer();
		ITextComponent content = event.getContent();
		int packedLight = event.getPackedLight();

		Entity entity = event.getEntity();
		if (!(entity instanceof PlayerEntity))
			return;

		PlayerEntity player = (PlayerEntity) entity;
		IDataManager data = (IDataManager) player;

		Personality.PlayerState state = data.getValue(Personality.PLAYER_STATE);

		FontRenderer fontrenderer = renderer.getFontRendererFromRenderManager();
		double d0 = renderer.getRenderManager().squareDistanceTo(entity);
		if (d0 > 4096.0D || !((EntityRendererAccessor) renderer).getCanRenderName(entity))
			return;

		boolean flag = !entity.isDiscrete();
		float f = entity.getHeight() + 0.5F;
		int i = -8 + ("deadmau5".equals(content.toString()) ? -10 : 0);

		stack.push();
		{
			stack.translate(0.0D, f, 0.0D);
			stack.rotate(renderer.getRenderManager().getCameraOrientation());
			stack.scale(-0.05F, -0.05F, 0.05F);
			Matrix4f matrix = stack.getLast().getMatrix();

			if (PersonalityConfig.CLIENT.simpleStatus.get() && state.getSimpleText() != null) {
				float f2 = (float) (-fontrenderer.func_238414_a_(state.getSimpleText()) / 2);
				fontrenderer.func_238416_a_(state.getSimpleText(), f2, i, 553648127, true, matrix, buffer, flag, 0, packedLight);
				if (flag)
					fontrenderer.func_238416_a_(state.getSimpleText(), f2, i, -1, true, matrix, buffer, false, 0, packedLight);
			} else if (state.getIcon() != null) {
				// TODO: Render Icon
			}
		}
		stack.pop();
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