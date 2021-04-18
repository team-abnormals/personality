package com.teamabnormals.personality.core.registry;

import com.teamabnormals.personality.core.Personality;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PersonalitySounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Personality.MOD_ID);

    public static final RegistryObject<SoundEvent> ENTITY_CREEPER_CHARGED_PRIMED = register("entity.creeper.charged.primed");
    public static final RegistryObject<SoundEvent> ENTITY_CREEPER_CHARGED_EXPLODE = register("entity.creeper.charged.explode");

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(Personality.MOD_ID, name)));
    }
}
