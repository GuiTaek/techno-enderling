package com.gmail.guitaekm.technoenderling.resources;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.TechnoEnderlingServer;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructure;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructureRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.StringWriter;
import java.text.MessageFormat;

public class EnderlingStructureDatapackLoader {
    public static class Listener implements SimpleSynchronousResourceReloadListener {

        // not sure what this method does but when two Listeners return the same value,
        // one overrides the other
        @Override
        public Identifier getFabricId() {
            return new Identifier("technoenderling", "enderling_structures");
        }

        @Override
        public void reload(ResourceManager manager) {
            EnderlingStructureRegistry.instance().clear();
            for(Identifier id : manager.findResources("enderling_structures", path -> path.endsWith(".json"))) {
                try {
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(manager.getResource(id).getInputStream(), writer, "UTF-8");
                    JsonElement json = JsonParser.parseString(writer.toString());
                    EnderlingStructure structure = new EnderlingStructure(json);
                    EnderlingStructureRegistry.instance().register(structure);
                } catch(Exception e) {
                    TechnoEnderling.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
                }

            }
        }
    }
    public static void register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new EnderlingStructureDatapackLoader.Listener());
    }
}
