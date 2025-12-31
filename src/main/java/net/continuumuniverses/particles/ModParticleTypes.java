package net.continuumuniverses.particles;

import net.continuumuniverses.ContinuumUniverses;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModParticleTypes {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(
                    Registries.PARTICLE_TYPE,
                    ContinuumUniverses.MODID
            );

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType>
            PLASMA =
            PARTICLES.register(
                    "plasma",
                    () -> new SimpleParticleType(false)
            );
}
