package com.gmail.guitaekm.technoenderling.config;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class ModResources {
    public static void registerResources() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new FogDatapackLoader.Listener());
    }
}
