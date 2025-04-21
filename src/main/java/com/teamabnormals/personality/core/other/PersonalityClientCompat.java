package com.teamabnormals.personality.core.other;


import com.teamabnormals.personality.client.model.FishingHookModel;
import com.teamabnormals.personality.core.Personality;
import com.teamabnormals.personality.core.PersonalityConfig;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Personality.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class PersonalityClientCompat {
	public static final ModelLayerLocation FISHING_HOOK = new ModelLayerLocation(Personality.location("fishing_hook"), "main");

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(FISHING_HOOK, FishingHookModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void modConfigEvent(ModConfigEvent event) {
		if (event.getConfig().getType() == ModConfig.Type.CLIENT) {
			if (PersonalityConfig.CLIENT.deflateArmorModel.get()) {
				LayerDefinitions.INNER_ARMOR_DEFORMATION = new CubeDeformation(PersonalityConfig.CLIENT.innerArmorDeformation.get().floatValue());
				LayerDefinitions.OUTER_ARMOR_DEFORMATION = new CubeDeformation(PersonalityConfig.CLIENT.outerArmorDeformation.get().floatValue());
			} else {
				LayerDefinitions.INNER_ARMOR_DEFORMATION = new CubeDeformation(0.5F);
				LayerDefinitions.OUTER_ARMOR_DEFORMATION = new CubeDeformation(1.0F);
			}
		}
	}
}