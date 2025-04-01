package com.teamabnormals.personality.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.personality.client.model.FishingHookModel;
import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHookRenderer.class)
public abstract class FishingHookRendererMixin extends EntityRenderer<FishingHook> {

	@Unique
	private static final FishingHookModel MODEL = new FishingHookModel(FishingHookModel.createBodyLayer().bakeRoot());
	@Unique
	private static final ResourceLocation TEXTURE = Personality.location("textures/entity/fishing_bobber.png");
	@Unique
	private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);

	private FishingHookRendererMixin(EntityRendererProvider.Context renderManager) {
		super(renderManager);
	}

	@Shadow
	private static void stringVertex(float p_229104_0_, float p_229104_1_, float p_229104_2_, VertexConsumer p_229104_3_, PoseStack.Pose pose, float p_229104_4_, float p_229104_5_) {
		throw new AssertionError("Mixin failed");
	}

	@Shadow
	private static float fraction(int p_229105_0_, int p_229105_1_) {
		throw new AssertionError("Mixin failed");
	}


	@Shadow
	protected abstract Vec3 getPlayerHandPos(Player player, float p_340872_, float partialTick);

	@Inject(method = "render(Lnet/minecraft/world/entity/projectile/FishingHook;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
	public void render(FishingHook bobber, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
		if (!PersonalityConfig.CLIENT.fishingHookModel.get())
			return;

		Player player = bobber.getPlayerOwner();
		if (player != null) {
			ItemStack mainRod = player.getMainHandItem();
			ItemStack offHandRod = player.getOffhandItem();
			boolean enchanted = (mainRod.getItem() instanceof FishingRodItem && mainRod.isEnchanted()) || (offHandRod.getItem() instanceof FishingRodItem && offHandRod.isEnchanted());

			poseStack.pushPose();
			{
				poseStack.pushPose();
				{
					poseStack.scale(1, -1, -1);
					poseStack.translate(0, -1.5, 0);
					MODEL.renderToBuffer(poseStack, ItemRenderer.getFoilBufferDirect(buffer, RENDER_TYPE, false, enchanted), packedLight, OverlayTexture.NO_OVERLAY, 1);
				}
				poseStack.popPose();

				Vec3 handPos = this.getPlayerHandPos(player, Mth.sin(Mth.sqrt(player.getAttackAnim(partialTicks)) * (float) Math.PI), partialTicks);
				Vec3 bobberPos = bobber.getPosition(partialTicks).add(0.0, 0.25, 0.0);
				float stringPosX = (float) (handPos.x - bobberPos.x);
				float stringPosY = (float) (handPos.y - bobberPos.y);
				float stringPosZ = (float) (handPos.z - bobberPos.z);

				VertexConsumer stringVertex = buffer.getBuffer(RenderType.lineStrip());
				Pose stringMatrix = poseStack.last();
				for (int i = 0; i <= 16; ++i) {
					stringVertex(stringPosX, stringPosY, stringPosZ, stringVertex, stringMatrix, fraction(i, 16), fraction(i + 1, 16));
				}
			}
			poseStack.popPose();

			super.render(bobber, yaw, partialTicks, poseStack, buffer, packedLight);
		}

		ci.cancel();
	}
}