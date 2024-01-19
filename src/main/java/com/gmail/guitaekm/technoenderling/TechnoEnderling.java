package com.gmail.guitaekm.technoenderling;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechnoEnderling implements ModInitializer {
    public static String MOD_ID = "technoenderling";
    public static final Logger LOGGER = LoggerFactory.getLogger(TechnoEnderling.MOD_ID);
    @Override
    public void onInitialize() {
        //
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            // here, we are at a physical client, that is different to a logical client
            // see https://fabricmc.net/wiki/tutorial:side
            new TechnoEnderlingServer().onInitializeServer();
            new TechnoEnderlingClient().onInitializeClient();
        } else {
            new TechnoEnderlingServer().onInitializeServer();
        }
    }
}
