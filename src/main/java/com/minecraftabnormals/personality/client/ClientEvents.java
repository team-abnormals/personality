package com.minecraftabnormals.personality.client;

import com.minecraftabnormals.personality.common.CommonEvents;
import com.minecraftabnormals.personality.common.network.MessageC2SCrawl;
import com.minecraftabnormals.personality.common.network.MessageC2SSit;
import com.minecraftabnormals.personality.core.Personality;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Personality.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    public static boolean crawling;
    public static boolean sitting;

    @SubscribeEvent
    public static void onEvent(TickEvent.ClientTickEvent event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null)
            return;

        if (PersonalityClient.CRAWL.isDown() && !sitting && CommonEvents.testCrawl(player)) {
            if (!crawling) {
                crawling = true;
                player.setForcedPose(Pose.SWIMMING);
                Personality.CHANNEL.sendToServer(new MessageC2SCrawl(true));
            }
        } else if (crawling) {
            crawling = false;
            player.setForcedPose(null);
            Personality.CHANNEL.sendToServer(new MessageC2SCrawl(false));
        }

        Vector3d motion = player.getDeltaMovement();
        if (PersonalityClient.SIT.isDown() && !crawling && Math.abs(motion.x()) <= 0.008 && Math.abs(motion.z()) <= 0.008 && CommonEvents.testSit(player)) {
            if (!sitting) {
                sitting = true;
                Personality.SYNCED_SITTING_PLAYERS.add(player.getUUID());
                player.refreshDimensions();
                Personality.CHANNEL.sendToServer(new MessageC2SSit(true));
            }
        } else if (sitting) {
            sitting = false;
            Personality.SYNCED_SITTING_PLAYERS.remove(player.getUUID());
            player.refreshDimensions();
            Personality.CHANNEL.sendToServer(new MessageC2SSit(false));
        }
    }

    @SubscribeEvent
    public static void onEvent(EntityEvent.Size event) {
        Entity entity = event.getEntity();
        if (!(event.getEntity() instanceof PlayerEntity))
            return;

        PlayerEntity player = (PlayerEntity) entity;
        if (sitting && !crawling && !CommonEvents.testSit(player)) {
            EntitySize size = PlayerEntity.STANDING_DIMENSIONS;

            event.setNewSize(new EntitySize(size.width, size.height - 0.5F, size.fixed));
            event.setNewEyeHeight(event.getOldEyeHeight() - 0.5F);
        }
    }

    @SubscribeEvent
    public static void onEvent(RenderPlayerEvent event) {
        PlayerEntity player = event.getPlayer();
        ((SittableModel) event.getRenderer().getModel()).setForcedSitting(Personality.SYNCED_SITTING_PLAYERS.contains(player.getUUID()));
    }
}