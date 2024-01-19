package com.gmail.guitaekm.technoenderling.resources;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;


public class ModResourcesServer {
    public static void registerResources() {
        FogDatapackLoader.register();
        EnderlingStructureDatapackLoader.register();
    }
}
