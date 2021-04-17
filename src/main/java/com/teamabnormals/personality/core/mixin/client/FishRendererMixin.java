package com.teamabnormals.personality.core.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teamabnormals.personality.client.render.model.FishingBobberModel;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.FishRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishRenderer.class)
public abstract class FishRendererMixin extends EntityRenderer<FishingBobberEntity> {

    @Unique
    private static final FishingBobberModel MODEL = new FishingBobberModel();
    @Unique
    private static final ResourceLocation TEXTURE = new ResourceLocation(Personality.MOD_ID, "textures/entity/fishing_bobber.png");
    @Unique
    private static final RenderType RENDER_TYPE = RenderType.getEntityCutoutNoCull(TEXTURE);

    protected FishRendererMixin(EntityRendererManager renderManager) {
        super(renderManager);
    }


    @Shadow
    private static void func_229104_a_(float p_229104_0_, float p_229104_1_, float p_229104_2_, IVertexBuilder p_229104_3_, Matrix4f p_229104_4_, float p_229104_5_) {
        throw new AssertionError("Mixin failed");
    }

    @Shadow
    private static float func_229105_a_(int p_229105_0_, int p_229105_1_) {
        throw new AssertionError("Mixin failed");
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(FishingBobberEntity bobber, float yaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, CallbackInfo ci) {
        PlayerEntity player = bobber.func_234606_i_();
        if (player != null) {
            ItemStack mainRod = player.getHeldItemMainhand();
            ItemStack offHandRod = player.getHeldItemOffhand();
            boolean enchanted = (mainRod.getItem() == Items.FISHING_ROD && mainRod.isEnchanted()) || (offHandRod.getItem() == Items.FISHING_ROD && offHandRod.isEnchanted());

            matrixStack.push();
            {
                matrixStack.push();
                {
                    matrixStack.scale(1, -1, -1);
                    matrixStack.translate(0, -1.5, 0);
                    MODEL.render(matrixStack, ItemRenderer.getEntityGlintVertexBuilder(buffer, RENDER_TYPE, false, enchanted), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1); // TODO: add glint config
                }
                matrixStack.pop();

                int hand = player.getPrimaryHand() == HandSide.RIGHT ? 1 : -1;
                if (mainRod.getItem() != Items.FISHING_ROD) {
                    hand = -hand;
                }

                float swingProgress = player.getSwingProgress(partialTicks);
                float swingProgressSin = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
                float playerYaw = MathHelper.lerp(partialTicks, player.prevRenderYawOffset, player.renderYawOffset) * ((float) Math.PI / 180F);
                double yawSin = MathHelper.sin(playerYaw);
                double yawCos = MathHelper.cos(playerYaw);
                double handPos = (double) hand * 0.35D;
                double playerPosX;
                double playerPosY;
                double playerPosZ;
                float playerEyeHeight;
                if ((this.renderManager.options == null || this.renderManager.options.getPointOfView().func_243192_a()) && player == Minecraft.getInstance().player) {
                    double d7 = this.renderManager.options.fov / 100.0D;
                    Vector3d handVec = new Vector3d((double) hand * -0.36D * d7, -0.045D * d7, 0.4D);
                    handVec = handVec.rotatePitch(-MathHelper.lerp(partialTicks, player.prevRotationPitch, player.rotationPitch) * ((float) Math.PI / 180F));
                    handVec = handVec.rotateYaw(-MathHelper.lerp(partialTicks, player.prevRotationYaw, player.rotationYaw) * ((float) Math.PI / 180F));
                    handVec = handVec.rotateYaw(swingProgressSin * 0.5F);
                    handVec = handVec.rotatePitch(-swingProgressSin * 0.7F);

                    playerPosX = MathHelper.lerp(partialTicks, player.prevPosX, player.getPosX()) + handVec.x;
                    playerPosY = MathHelper.lerp(partialTicks, player.prevPosY, player.getPosY()) + handVec.y;
                    playerPosZ = MathHelper.lerp(partialTicks, player.prevPosZ, player.getPosZ()) + handVec.z;
                    playerEyeHeight = player.getEyeHeight();
                } else {
                    playerPosX = MathHelper.lerp(partialTicks, player.prevPosX, player.getPosX()) - yawCos * handPos - yawSin * 0.8D;
                    playerPosY = player.prevPosY + (double) player.getEyeHeight() + (player.getPosY() - player.prevPosY) * (double) partialTicks - 0.45D;
                    playerPosZ = MathHelper.lerp(partialTicks, player.prevPosZ, player.getPosZ()) - yawSin * handPos + yawCos * 0.8D;
                    playerEyeHeight = player.isCrouching() ? -0.1875F : 0.0F;
                }

                double posX = MathHelper.lerp(partialTicks, bobber.prevPosX, bobber.getPosX());
                double posY = MathHelper.lerp(partialTicks, bobber.prevPosY, bobber.getPosY()) + 0.25D;
                double posZ = MathHelper.lerp(partialTicks, bobber.prevPosZ, bobber.getPosZ());
                float stringPosX = (float) (playerPosX - posX);
                float stringPosY = (float) (playerPosY - posY) + playerEyeHeight;
                float stringPosZ = (float) (playerPosZ - posZ);
                IVertexBuilder stringVertex = buffer.getBuffer(RenderType.getLines());
                Matrix4f stringMatrix = matrixStack.getLast().getMatrix();

                for (int i = 0; i < 16; ++i) {
                    func_229104_a_(stringPosX, stringPosY, stringPosZ, stringVertex, stringMatrix, func_229105_a_(i, 16));
                    func_229104_a_(stringPosX, stringPosY, stringPosZ, stringVertex, stringMatrix, func_229105_a_(i + 1, 16));
                }
            }
            matrixStack.pop();

            super.render(bobber, yaw, partialTicks, matrixStack, buffer, packedLight);
        }

        ci.cancel(); // TODO: config
    }
}
