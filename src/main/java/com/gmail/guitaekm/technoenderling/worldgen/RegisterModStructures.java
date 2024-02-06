package com.gmail.guitaekm.technoenderling.worldgen;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class RegisterModStructures {

    public static void register() {

        /*
         * We setup and register our structures here.
         * You should always register your stuff to prevent mod compatibility issue down the line.
         */
        ModStructureFeatures.register();
        addToBiomes();
    }

    /**
     * used for spawning our structures in biomes.
     * You can move the BiomeModification API anywhere you prefer it to be at.
     * Just make sure you call BiomeModifications.addStructure at mod init.
     */
    public static void addToBiomes() {

        /*
         * This is the API you will use to add anything to any biome.
         * This includes spawns, changing the biome's looks, messing with its temperature,
         * adding carvers, spawning new features... etc
         */
        BiomeModifications.addStructure(
                // Add our structure to all biomes that have any of these biome categories. This includes modded biomes.
                // You can filter to certain biomes based on stuff like temperature, scale, precipitation, mod id, etc.
                // See BiomeSelectors's methods for more options or write your own by doing `(context) -> context.whatever() == condition`
                biomeSelectionContext -> biomeSelectionContext.getBiomeKey().getValue().equals(new Identifier(TechnoEnderling.MOD_ID, "enderworld_wastes")),
                // The registry key of our ConfiguredStructure so BiomeModification API can grab it
                // later to tell the game which biomes that your structure can spawn within.
                RegistryKey.of(
                        Registry.CONFIGURED_STRUCTURE_FEATURE_KEY,
                        new Identifier(TechnoEnderling.MOD_ID, "pocket_portal")
                )
        );
    }
}
