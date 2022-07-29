package com.teamabnormals.personality.core.other;

import com.teamabnormals.personality.client.PersonalityClient;
import com.teamabnormals.personality.client.SittableModel;
import com.teamabnormals.personality.common.network.MessageC2SCrawl;
import com.teamabnormals.personality.common.network.MessageC2SSit;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Personality.MOD_ID, value = Dist.CLIENT)
public class PersonalityClientEvents {
	public static boolean crawling;
	public static boolean sitting;

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		Player player = Minecraft.getInstance().player;
		if (player == null)
			return;

		if (PersonalityClient.CRAWL.isDown() && !sitting && PersonalityEvents.testCrawl(player)) {
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

		Vec3 motion = player.getDeltaMovement();
		if (PersonalityClient.SIT.isDown() && !crawling && Math.abs(motion.x()) <= 0.008 && Math.abs(motion.z()) <= 0.008 && PersonalityEvents.testSit(player)) {
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
	public static void onEntitySize(EntityEvent.Size event) {
		if (!(event.getEntity() instanceof Player player))
			return;

		if (sitting && !crawling && !PersonalityEvents.testSit(player)) {
			EntityDimensions size = Player.STANDING_DIMENSIONS;

			event.setNewSize(new EntityDimensions(size.width, size.height - 0.5F, size.fixed));
			event.setNewEyeHeight(event.getOldEyeHeight() - 0.5F);
		}
	}

	@SubscribeEvent
	public static void onRenderPlayer(RenderPlayerEvent event) {
		Player player = event.getEntity();
		((SittableModel) event.getRenderer().getModel()).setForcedSitting(Personality.SYNCED_SITTING_PLAYERS.contains(player.getUUID()));
	}
}