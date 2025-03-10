package com.teamabnormals.personality.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

public class SheepFurLayer extends RenderLayer<Sheep, SheepModel<Sheep>> {
	private static final ResourceLocation SHEEP_FUR_LOCATION = ResourceLocation.fromNamespaceAndPath(Personality.MOD_ID, "textures/entity/sheep/sheep_fur_overlay.png");

	public SheepFurLayer(RenderLayerParent<Sheep, SheepModel<Sheep>> parent) {
		super(parent);
	}

	@Override
	public void render(PoseStack stack, MultiBufferSource buffer, int packedLightIn, Sheep sheep, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!sheep.isInvisible() && PersonalityConfig.CLIENT.sheepFurOverlay.get()) {
			int i;
			if (sheep.hasCustomName() && "jeb_".equals(sheep.getName().getString())) {
				int k = sheep.tickCount / 25 + sheep.getId();
				int l = DyeColor.values().length;
				float f = ((float) (sheep.tickCount % 25) + partialTicks) / 25.0F;
				int k1 = Sheep.getColor(DyeColor.byId(k % l));
				int l1 = Sheep.getColor(DyeColor.byId((k + 1) % l));
				i = FastColor.ARGB32.lerp(f, k1, l1);
			} else {
				i = Sheep.getColor(sheep.getColor());
			}

			coloredCutoutModelCopyLayerRender(this.getParentModel(), this.getParentModel(), SHEEP_FUR_LOCATION, stack, buffer, packedLightIn, sheep, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, i);
		}
	}
}