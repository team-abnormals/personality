package com.teamabnormals.personality.core.other;

import com.teamabnormals.personality.common.extension.SittableModel;
import com.teamabnormals.personality.common.network.CrawlPayload;
import com.teamabnormals.personality.common.network.SitPayload;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Personality.MOD_ID, value = Dist.CLIENT)
public class PersonalityClientEvents {
	public static boolean crawling;
	public static boolean sitting;

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Pre event) {
		Player player = Minecraft.getInstance().player;
		if (player == null)
			return;

		if (PersonalityKeyBindings.CRAWL.isDown() && !sitting && PersonalityEvents.testCrawl(player)) {
			if (!crawling) {
				crawling = true;
				player.setForcedPose(Pose.SWIMMING);
				PacketDistributor.sendToServer(new CrawlPayload(true));
			}
		} else if (crawling) {
			crawling = false;
			player.setForcedPose(null);
			PacketDistributor.sendToServer(new CrawlPayload(false));
		}

		Vec3 motion = player.getDeltaMovement();
		if (PersonalityKeyBindings.SIT.isDown() && !crawling && Math.abs(motion.x()) <= 0.008 && Math.abs(motion.z()) <= 0.008 && PersonalityEvents.testSit(player)) {
			if (!sitting) {
				sitting = true;
				Personality.SYNCED_SITTING_PLAYERS.add(player.getUUID());
				player.refreshDimensions();
				PacketDistributor.sendToServer(new SitPayload(true));
			}
		} else if (sitting) {
			sitting = false;
			Personality.SYNCED_SITTING_PLAYERS.remove(player.getUUID());
			player.refreshDimensions();
			PacketDistributor.sendToServer(new SitPayload(false));
		}
	}

	@SubscribeEvent
	public static void onEntitySize(EntityEvent.Size event) {
		if (!(event.getEntity() instanceof Player player))
			return;

		if (sitting && !crawling && !PersonalityEvents.testSit(player)) {
			EntityDimensions size = Player.STANDING_DIMENSIONS;
			event.setNewSize(new EntityDimensions(size.width(), size.height() - 0.5F, size.eyeHeight() - 0.5F, size.attachments(), size.fixed()));
		}
	}

	@SubscribeEvent
	public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
		((SittableModel) event.getRenderer().getModel()).setForcedSitting(Personality.SYNCED_SITTING_PLAYERS.contains(event.getEntity().getUUID()));
	}
}