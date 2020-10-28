package com.minecraftabnormals.personality.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SeatEntity extends Entity {
	public SeatEntity(EntityType<? extends SeatEntity> type, World world) {
		super(type, world);
		this.noClip = true;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.getPassengers().isEmpty())
			this.remove();
	}

	@Override
	protected void registerData() {
	}

	@Override
	protected void readAdditional(CompoundNBT tag) {
	}

	@Override
	protected void writeAdditional(CompoundNBT tag) {
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
