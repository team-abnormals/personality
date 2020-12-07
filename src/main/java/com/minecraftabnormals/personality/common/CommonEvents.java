package com.minecraftabnormals.personality.common;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.personality.client.ClimbAnimation;
import com.minecraftabnormals.personality.common.network.MessageS2CSyncCrawl;
import com.minecraftabnormals.personality.core.Personality;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Personality.MOD_ID)
public class CommonEvents {

	@SubscribeEvent
	public static void onEvent(TickEvent.PlayerTickEvent event) {
		if (event.side == LogicalSide.CLIENT && event.phase != TickEvent.Phase.END)
			return;

		PlayerEntity player = event.player;
		if (!(player instanceof ServerPlayerEntity))
			return;

		UUID uuid = player.getUniqueID();
		setBesideClimbableBlock(player, player.isOnLadder());
		if ((Personality.SITTING_PLAYERS.contains(player.getUniqueID()) || Personality.SYNCED_SITTING_PLAYERS.contains(player.getUniqueID())) && !testCrawl(player)) {
			Personality.SITTING_PLAYERS.remove(uuid);
			Personality.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new MessageS2CSyncCrawl(player.getUniqueID(), false));
		}
	}

	@SubscribeEvent
	public static void onEvent(PlayerEvent.StartTracking event) {
		Entity entity = event.getTarget();
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			Personality.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new MessageS2CSyncCrawl(player.getUniqueID(), player.getForcedPose() == Pose.SWIMMING));
		}
	}

	@SubscribeEvent
	public static void onEvent(PlayerEvent.StopTracking event) {
		Entity entity = event.getTarget();
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			Personality.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new MessageS2CSyncCrawl(player.getUniqueID(), false));
		}
	}

	@SubscribeEvent
	public static void onEvent(EntityEvent.Size event) {
		Entity entity = event.getEntity();
		if (!(event.getEntity() instanceof PlayerEntity))
			return;

		PlayerEntity player = (PlayerEntity) entity;
		if ((Personality.SITTING_PLAYERS.contains(player.getUniqueID()) || Personality.SYNCED_SITTING_PLAYERS.contains(player.getUniqueID())) && testSit(player)) {
			EntitySize size = PlayerEntity.STANDING_SIZE;

			event.setNewSize(new EntitySize(size.width, size.height - 0.5F, size.fixed));
			event.setNewEyeHeight(player.getStandingEyeHeight(Pose.STANDING, size) - 0.5F);
		}
	}

	public static boolean testSit(PlayerEntity player) {
		return (player.getPose() == Pose.STANDING || player.getPose() == Pose.CROUCHING) && !player.isPassenger() && player.isOnGround();
	}

	public static boolean testCrawl(PlayerEntity player) {
		return !(Personality.SITTING_PLAYERS.contains(player.getUniqueID()) || Personality.SYNCED_SITTING_PLAYERS.contains(player.getUniqueID())) && !player.isPassenger();
	}

	public static boolean isClimbing(PlayerEntity player) {
		return !player.isOnGround() && isBesideClimbableBlock(player);
	}

	public static boolean isBesideClimbableBlock(PlayerEntity player) {
		IDataManager data = (IDataManager) player;
		return (data.getValue(Personality.CLIMBING) & 1) != 0;
	}

	public static void setBesideClimbableBlock(PlayerEntity player, boolean climbing) {
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
	public static float getClimbingAnimationScale(PlayerEntity player, float partialTicks) {
		return MathHelper.lerp(partialTicks, ((ClimbAnimation) player).getPrevClimbAnim(), ((ClimbAnimation) player).getClimbAnim()) / 4.0F;
	}
}
