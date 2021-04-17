package com.teamabnormals.personality.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Personality.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onEvent(RenderLivingEvent<?, ?> event) {
        LivingEntity entity = event.getEntity();
        MatrixStack matrixStack = event.getMatrixStack();
        if (isFreezing(entity))
            matrixStack.rotate(Vector3f.YP.rotation((float) (Math.cos(entity.ticksExisted * 3.25D) * Math.PI * 0.003F)));
    }

    private static boolean isFreezing(LivingEntity entity) {
        return isSnowingAt(entity.world, entity.getPosition()); // TODO: config
    }

    private static boolean isSnowingAt(World world, BlockPos pos) {
        if (!world.isRaining()) {
            return false;
        } else if (!world.canSeeSky(pos)) {
            return false;
        } else if (world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos).getY() > pos.getY()) {
            return false;
        } else {
            Biome biome = world.getBiome(pos);
            return biome.getPrecipitation() == Biome.RainType.SNOW;
        }
    }
}
