package com.teamabnormals.personality.core.other;

import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.personality.client.ClimbAnimation;
import com.teamabnormals.personality.common.network.MessageS2CSyncCrawl;
import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

@EventBusSubscriber(modid = Personality.MOD_ID)
public class PersonalityEvents {

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.side == LogicalSide.CLIENT && event.phase != TickEvent.Phase.END)
			return;

		Player player = event.player;
		if (!(player instanceof ServerPlayer))
			return;

		UUID uuid = player.getUUID();
		setBesideClimbableBlock(player, player.onClimbable() && (player.yOld != player.getY() || (player.isShiftKeyDown())));
		if ((Personality.SITTING_PLAYERS.contains(player.getUUID()) || Personality.SYNCED_SITTING_PLAYERS.contains(player.getUUID())) && !testCrawl(player)) {
			Personality.SITTING_PLAYERS.remove(uuid);
			Personality.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new MessageS2CSyncCrawl(player.getUUID(), false));
		}
	}

	@SubscribeEvent
	public static void onStartTrackingPlayer(PlayerEvent.StartTracking event) {
		Entity entity = event.getTarget();
		if (entity instanceof Player player) {
			Personality.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new MessageS2CSyncCrawl(player.getUUID(), player.getForcedPose() == Pose.SWIMMING));
		}
	}

	@SubscribeEvent
	public static void onStopTrackingPlayer(PlayerEvent.StopTracking event) {
		Entity entity = event.getTarget();
		if (entity instanceof Player player) {
			Personality.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new MessageS2CSyncCrawl(player.getUUID(), false));
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

			event.setNewSize(new EntityDimensions(size.width, size.height - 0.5F, size.fixed));
			event.setNewEyeHeight(player.getStandingEyeHeight(Pose.STANDING, size) - 0.5F);
		}
	}

	public static boolean testSit(Player player) {
		return (player.getPose() == Pose.STANDING || player.getPose() == Pose.CROUCHING) && !player.isPassenger() && player.isOnGround();
	}

	public static boolean testCrawl(Player player) {
		return !(Personality.SITTING_PLAYERS.contains(player.getUUID()) || Personality.SYNCED_SITTING_PLAYERS.contains(player.getUUID())) && !player.isPassenger();
	}

	public static boolean isClimbing(Player player) {
		return !player.isOnGround() && isBesideClimbableBlock(player) && PersonalityConfig.CLIENT.climbingAnimation.get();
	}

	public static boolean isBesideClimbableBlock(Player player) {
		IDataManager data = (IDataManager) player;
		return (data.getValue(Personality.CLIMBING) & 1) != 0;
	}

	public static void setBesideClimbableBlock(Player player, boolean climbing) {
		IDataManager data = (IDataManager) player;
		byte b0 = data.getValue(Personality.CLIMBING);
		if (climbing) {
			b0 = (byte) (b0 | 1);
		} else {
			b0 = (byte) (b0 & -2);
		}
		data.setValue(Personality.CLIMBING, b0);
	}

	@OnlyIn(Dist.CLIENT)
	public static float getClimbingAnimationScale(Player player, float partialTicks) {
		return Mth.lerp(partialTicks, ((ClimbAnimation) player).getPrevClimbAnim(), ((ClimbAnimation) player).getClimbAnim()) / 4.0F;
	}
}
