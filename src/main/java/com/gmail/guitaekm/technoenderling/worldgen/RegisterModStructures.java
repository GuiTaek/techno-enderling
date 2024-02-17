package com.gmail.guitaekm.technoenderling.worldgen;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.List;

public class RegisterModStructures {

    public static List<Identifier> rarePocketPortalBiomes = List.of(
            new Identifier(TechnoEnderling.MOD_ID, "enderworld_wastes"),
            new Identifier(TechnoEnderling.MOD_ID, "enderworld_forest")
    );

    public static List<Identifier> commonPocketPortalBiomes = List.of(
            new Identifier(TechnoEnderling.MOD_ID, "enderworld_foggy_forest")
    );

    public static List<Identifier> enderworldBiomes = List.of(
            new Identifier(TechnoEnderling.MOD_ID, "enderworld_wastes"),
            new Identifier(TechnoEnderling.MOD_ID, "enderworld_forest"),
            new Identifier(TechnoEnderling.MOD_ID, "enderworld_foggy_forest")
    );

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
                context -> isInBiomeList(context, RegisterModStructures.commonPocketPortalBiomes),
                // The registry key of our ConfiguredStructure so BiomeModification API can grab it
                // later to tell the game which biomes that your structure can spawn within.
                RegistryKey.of(
                        Registry.CONFIGURED_STRUCTURE_FEATURE_KEY,
                        new Identifier(TechnoEnderling.MOD_ID, "common_pocket_portal")
                )
        );

        /*
         * This is the API you will use to add anything to any biome.
         * This includes spawns, changing the biome's looks, messing with its temperature,
         * adding carvers, spawning new features... etc
         */
        BiomeModifications.addStructure(
                // Add our structure to all biomes that have any of these biome categories. This includes modded biomes.
                // You can filter to certain biomes based on stuff like temperature, scale, precipitation, mod id, etc.
                // See BiomeSelectors's methods for more options or write your own by doing `(context) -> context.whatever() == condition`
                context -> isInBiomeList(context, RegisterModStructures.rarePocketPortalBiomes),
                // The registry key of our ConfiguredStructure so BiomeModification API can grab it
                // later to tell the game which biomes that your structure can spawn within.
                RegistryKey.of(
                        Registry.CONFIGURED_STRUCTURE_FEATURE_KEY,
                        new Identifier(TechnoEnderling.MOD_ID, "rare_pocket_portal")
                )
        );

        /*
         * This is the API you will use to add anything to any biome.
         * This includes spawns, changing the biome's looks, messing with its temperature,
         * adding carvers, spawning new features... etc
         */
        BiomeModifications.addStructure(
                // Add our structure to all biomes that have any of these biome categories. This includes modded biomes.
                // You can filter to certain biomes based on stuff like temperature, scale, precipitation, mod id, etc.
                // See BiomeSelectors's methods for more options or write your own by doing `(context) -> context.whatever() == condition`
                BiomeSelectors.foundInOverworld(),
                // The registry key of our ConfiguredStructure so BiomeModification API can grab it
                // later to tell the game which biomes that your structure can spawn within.
                RegistryKey.of(
                        Registry.CONFIGURED_STRUCTURE_FEATURE_KEY,
                        new Identifier(TechnoEnderling.MOD_ID, "enderworld_portal")
                )
        );

        /*
         * This is the API you will use to add anything to any biome.
         * This includes spawns, changing the biome's looks, messing with its temperature,
         * adding carvers, spawning new features... etc
         */
        BiomeModifications.addStructure(
                // Add our structure to all biomes that have any of these biome categories. This includes modded biomes.
                // You can filter to certain biomes based on stuff like temperature, scale, precipitation, mod id, etc.
                // See BiomeSelectors's methods for more options or write your own by doing `(context) -> context.whatever() == condition`
                context -> isInBiomeList(context, enderworldBiomes),
                // The registry key of our ConfiguredStructure so BiomeModification API can grab it
                // later to tell the game which biomes that your structure can spawn within.
                RegistryKey.of(
                        Registry.CONFIGURED_STRUCTURE_FEATURE_KEY,
                        new Identifier(TechnoEnderling.MOD_ID, "enderman_enderworld")
                )
        );
    }

    public static boolean isInBiomeList(BiomeSelectionContext context, List<Identifier> biomeList) {
        for (Identifier biome : biomeList) {
            if (context
                    .getBiomeKey()
                    .getValue()
                    .equals(biome)
            ) {
                return true;
            }
        }
        return false;
    }
}
