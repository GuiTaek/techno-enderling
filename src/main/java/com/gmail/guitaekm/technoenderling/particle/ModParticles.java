package com.gmail.guitaekm.technoenderling.particle;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.TechnoEnderlingServer;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModParticles {
    public static final DefaultParticleType ENDERWORLD_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(
                Registry.PARTICLE_TYPE,
                new Identifier(TechnoEnderling.MOD_ID, "enderworld"),
                ENDERWORLD_PARTICLE
        );
    }
}
