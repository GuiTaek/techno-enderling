package com.gmail.guitaekm.technoenderling.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FogBiomes {
    static protected Map<String, FogBiomeSetting> biomes = new HashMap<>();
    public static void addBiome(String biome, float minDistance, float maxDistance) {
        FogBiomes.biomes.put(biome, new FogBiomeSetting(minDistance, maxDistance));
    }

    public static void removeBiome(String biome) {
        // it shall fail when trying to remove a biome that was never included
        FogBiomes.biomes.remove(biome);
    }

    public static boolean isFogBiome(String biome) {
        return FogBiomes.biomes.containsKey(biome);
    }

    public static Optional<FogBiomeSetting> getBiomeSetting(String biome) {
        if (!FogBiomes.biomes.containsKey(biome)) {
            return Optional.empty();
        }
        return Optional.of(FogBiomes.biomes.get(biome));
    }
    public static Optional<Float> getMinDistance(String biome) {
        if (!FogBiomes.isFogBiome(biome)) {
            return Optional.empty();
        }
        return Optional.of(FogBiomes.biomes.get(biome).minDistance());
    }

    public static Optional<Float> getMaxDistance(String biome) {
        if (!FogBiomes.isFogBiome(biome)) {
            return Optional.empty();
        }
        return Optional.of(FogBiomes.biomes.get(biome).maxDistance());
    }
}
