package com.minecraftabnormals.personality.client.renderer;

import com.minecraftabnormals.personality.common.entity.SeatEntity;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class SeatRenderer extends EntityRenderer<SeatEntity> {
	public SeatRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public boolean shouldRender(SeatEntity entity, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
		return false;
	}

	@Override
	public ResourceLocation getEntityTexture(SeatEntity entity) {
		return null;
	}
}
