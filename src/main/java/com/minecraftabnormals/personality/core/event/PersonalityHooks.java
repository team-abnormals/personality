package com.minecraftabnormals.personality.core.event;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;

public class PersonalityHooks {

	public static EntitySize getPlayerSize(EntitySize size, PlayerEntity entity, Pose pose) {
		PlayerSizeEvent sizeEvent = new PlayerSizeEvent(entity, pose, size);
		MinecraftForge.EVENT_BUS.post(sizeEvent);
		return sizeEvent.getSize();
	}
}
