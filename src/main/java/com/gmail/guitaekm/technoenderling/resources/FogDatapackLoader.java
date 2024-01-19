package com.gmail.guitaekm.technoenderling.resources;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import com.gmail.guitaekm.technoenderling.TechnoEnderlingServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.StringWriter;

public class FogDatapackLoader {
    public static void saveFogBiomesFromJson(JsonElement json) {
        JsonArray array = json.getAsJsonArray();
        array.forEach((JsonElement biome) -> FogBiomes.addBiome(biome.getAsString()));
    }
    public static class Listener implements SimpleSynchronousResourceReloadListener {

        @Override
        public Identifier getFabricId() {
            return new Identifier("technoenderling", "fog_biomes");
        }

        @Override
        public void reload(ResourceManager manager) {
            FogBiomes.clear();
            for(Identifier id : manager.findResources("fog_biomes", path -> path.endsWith(".json"))) {
                try {
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(manager.getResource(id).getInputStream(), writer, "UTF-8");
                    JsonElement json = JsonParser.parseString(writer.toString());
                    FogDatapackLoader.saveFogBiomesFromJson(json);
                } catch(Exception e) {
                    TechnoEnderling.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
                }

            }
        }
    }
    public static void register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new FogDatapackLoader.Listener());
    }
}
