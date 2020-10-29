package com.minecraftabnormals.personality.common;

import com.minecraftabnormals.personality.core.Personality;
import com.minecraftabnormals.personality.core.event.PlayerSizeEvent;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.IDataManager;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent.EyeHeight;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Personality.MODID)
public class CommonEvents {

	@SubscribeEvent
	public static void onEvent(TickEvent.PlayerTickEvent event) {
		PlayerEntity player = event.player;
		IDataManager data = (IDataManager) player;

		if (data == null || event.phase != TickEvent.Phase.END)
			return;

		if (data.getValue(Personality.CRAWLING) && !data.getValue(Personality.SITTING) && player.getPose() != Pose.SWIMMING && !player.isPassenger()) {
			player.setPose(Pose.SWIMMING);
		}

		player.recalculateSize();
	}
	
	public static float getTotalMotion(Vector3d motion) {
		double x = motion.getX();
		double y = motion.getY();
		double z = motion.getZ();
		return (float) Math.cbrt(x * x + y * y + z * z);
	}

	@SubscribeEvent
	public static void onEvent(EyeHeight event) {
		if (event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			IDataManager data = (IDataManager) player;

			if (data.getValue(Personality.SITTING) && !data.getValue(Personality.CRAWLING) && !player.isPassenger()) {
				event.setNewHeight(event.getOldHeight() - 0.55F);
			}
		}
	}
	
	@SubscribeEvent
	public static void onEvent(PlayerSizeEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			IDataManager data = (IDataManager) player;

			if (data.getValue(Personality.SITTING) && !data.getValue(Personality.CRAWLING) && !player.isPassenger()) {
				event.setSize(new EntitySize(event.getSize().width, event.getSize().height - 0.55F, event.getSize().fixed));
			}
		}
	}
}
