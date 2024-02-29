package com.gmail.guitaekm.technoenderling;

import com.gmail.guitaekm.technoenderling.blocks.ModBlocksClient;
import com.gmail.guitaekm.technoenderling.event.ModEventsClient;
import com.gmail.guitaekm.technoenderling.gui.RegisterGui;
import com.gmail.guitaekm.technoenderling.keybinds.ModKeybindsClient;
import com.gmail.guitaekm.technoenderling.keybinds.RegisterKeyBinds;
import com.gmail.guitaekm.technoenderling.networking.ModNetworking;
import com.gmail.guitaekm.technoenderling.particle.ModParticles;
import com.gmail.guitaekm.technoenderling.particle.custom.EnderworldParticle;
import com.gmail.guitaekm.technoenderling.resources.ModResourcesClient;
import com.gmail.guitaekm.technoenderling.teleport.RegisterUtils;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class TechnoEnderlingClient {
    public void onInitializeClient() {
        ModBlocksClient.register();
        ParticleFactoryRegistry.getInstance().register(ModParticles.ENDERWORLD_PARTICLE, EnderworldParticle.Factory::new);
        RegisterKeyBinds.registerKeyBinds();
        ModNetworking.registerNetworkingClient();
        ModParticles.registerParticles();
        ModEventsClient.registerEvents();
        ModResourcesClient.register();
        ModKeybindsClient.register();
        RegisterUtils.registerClient();
        RegisterGui.registerClient();
    }
}
