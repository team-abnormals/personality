package com.teamabnormals.personality.core.other;

import com.teamabnormals.personality.common.extension.ClimbAnimation;
import com.teamabnormals.personality.common.network.SyncCrawlPayload;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

@EventBusSubscriber(modid = Personality.MOD_ID)
public class PersonalityEvents {

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (!(player instanceof ServerPlayer))
			return;

		UUID uuid = player.getUUID();
		if ((Personality.SITTING_PLAYERS.contains(player.getUUID()) || Personality.SYNCED_SITTING_PLAYERS.contains(player.getUUID())) && !testCrawl(player)) {
			Personality.SITTING_PLAYERS.remove(uuid);
			PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncCrawlPayload(player.getUUID(), false));
		}
	}

	@SubscribeEvent
	public static void onStartTrackingPlayer(PlayerEvent.StartTracking event) {
		Entity entity = event.getTarget();
		if (entity instanceof Player player) {
			PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new SyncCrawlPayload(player.getUUID(), player.getForcedPose() == Pose.SWIMMING));
		}
	}

	@SubscribeEvent
	public static void onStopTrackingPlayer(PlayerEvent.StopTracking event) {
		Entity entity = event.getTarget();
		if (entity instanceof Player player) {
			PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new SyncCrawlPayload(player.getUUID(), false));
		}
	}

	@SubscribeEvent
	public static void onEntitySize(EntityEvent.Size event) {
		Entity entity = event.getEntity();
		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) entity;
		if ((Personality.SITTING_PLAYERS.contains(player.getUUID()) || Personality.SYNCED_SITTING_PLAYERS.contains(player.getUUID())) && testSit(player)) {
			EntityDimensions size = Player.STANDING_DIMENSIONS;
			event.setNewSize(new EntityDimensions(size.width(), size.height() - 0.5F, size.eyeHeight() - 0.5F, size.attachments(), size.fixed()));
		}
	}

	public static boolean testSit(Player player) {
		return (player.getPose() == Pose.STANDING || player.getPose() == Pose.CROUCHING) && !player.isPassenger() && player.onGround();
	}

	public static boolean testCrawl(Player player) {
		return !(Personality.SITTING_PLAYERS.contains(player.getUUID()) || Personality.SYNCED_SITTING_PLAYERS.contains(player.getUUID())) && !player.isPassenger();
	}

	public static boolean isClimbing(Player player) {
		return !player.onGround() && player.onClimbable() && (player.yOld != player.getY() || (player.isCrouching()));
	}

	public static float getClimbingAnimationScale(Player player, float partialTicks) {
		return Mth.lerp(partialTicks, ((ClimbAnimation) player).getPrevClimbAnim(), ((ClimbAnimation) player).getClimbAnim()) / 4.0F;
	}
}
