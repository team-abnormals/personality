package com.minecraftabnormals.personality.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecraftabnormals.personality.common.CommonEvents;
import com.minecraftabnormals.personality.core.Personality;
import com.teamabnormals.abnormals_core.common.world.storage.tracking.IDataManager;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.vector.Vector3d;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "move", at = @At("HEAD"))
    public void move(MoverType typeIn, Vector3d pos, CallbackInfo info) {
    	IDataManager data = ((IDataManager)((Object)this));
        if (data.getValue(Personality.SITTING) && CommonEvents.getTotalMotion(pos) >= 0.185F) {
        	data.setValue(Personality.SITTING, false);
        }
    }
}
