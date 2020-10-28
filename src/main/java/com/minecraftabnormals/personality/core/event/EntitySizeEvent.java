package com.minecraftabnormals.personality.core.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraftforge.event.entity.EntityEvent;

public class EntitySizeEvent extends EntityEvent {
	private final Pose pose;
	private final EntitySize oldSize;
	private EntitySize newSize;

	public EntitySizeEvent(Entity entity, Pose pose, EntitySize size) {
		super(entity);
		this.pose = pose;
		this.oldSize = size;
		this.newSize = size;
	}

	public Pose getPose() {
		return pose;
	}

	public EntitySize getOldSize() {
		return oldSize;
	}

	public EntitySize getNewSize() {
		return newSize;
	}

	public void setSize(EntitySize size) {
		this.newSize = size;
	}
}
