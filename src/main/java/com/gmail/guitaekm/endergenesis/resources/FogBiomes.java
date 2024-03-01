package com.gmail.guitaekm.endergenesis.resources;

import java.util.*;

public class FogBiomes {
    static protected Set<String> biomes = new HashSet<>();
    public static void addBiome(String biome) {
        FogBiomes.biomes.add(biome);
    }

    public static void removeBiome(String biome) {
        // it shall fail when trying to remove a biome that was never included
        FogBiomes.biomes.remove(biome);
    }

    public static boolean isFogBiome(String biome) {
        return FogBiomes.biomes.contains(biome);
    }

    public static void clear() {
        FogBiomes.biomes.clear();
    }
}
