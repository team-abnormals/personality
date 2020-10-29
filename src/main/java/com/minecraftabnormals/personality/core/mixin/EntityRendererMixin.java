package com.minecraftabnormals.personality.core.mixin;

import com.minecraftabnormals.personality.core.accessor.EntityRendererAccessor;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> implements EntityRendererAccessor {

	@Shadow
	protected abstract boolean canRenderName(T entity);

	@SuppressWarnings("unchecked")
	@Override
	public boolean getCanRenderName(Entity entity) {
		return this.canRenderName((T) entity);
	}
}
