package com.teamabnormals.personality.core.registry;

import com.mojang.serialization.Codec;
import com.teamabnormals.personality.core.Personality;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PersonalityParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Personality.MOD_ID);

    public static final RegistryObject<ParticleType<BlockParticleData>> FALLING_LEAF = PARTICLE_TYPES.register("falling_leaf", () -> new ParticleType<BlockParticleData>(false, BlockParticleData.DESERIALIZER) {
        @Override
        public Codec<BlockParticleData> func_230522_e_() {
            return BlockParticleData.func_239800_a_(this);
        }
    });
}
