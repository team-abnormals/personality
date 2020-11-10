package com.minecraftabnormals.personality.core.event;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class PlayerSizeEvent extends PlayerEvent {
	private final Pose pose;
	private final EntitySize prevSize;
	private EntitySize size;

	public PlayerSizeEvent(PlayerEntity entity, Pose pose, EntitySize size) {
		super(entity);
		this.pose = pose;
		this.prevSize = size;
		this.size = size;
	}

	public Pose getPose() {
		return pose;
	}

	public EntitySize getPreviousSize() {
		return prevSize;
	}

	public EntitySize getSize() {
		return size;
	}

	public void setSize(EntitySize size) {
		this.size = size;
	}
}
