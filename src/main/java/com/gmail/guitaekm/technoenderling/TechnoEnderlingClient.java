package com.gmail.guitaekm.technoenderling;

import com.gmail.guitaekm.technoenderling.particle.ModParticles;
import com.gmail.guitaekm.technoenderling.particle.custom.EnderworldParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class TechnoEnderlingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.ENDERWORLD_PARTICLE, EnderworldParticle.Factory::new);
    }
}
