package com.gmail.guitaekm.technoenderling.features;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.OceanRuinFeature;

public class ModFeatures {
    public static final Identifier ENDERLING_STRUCTURE_FEATURE_ID = new Identifier(TechnoEnderling.MOD_ID, "enderling_structure");
    public static final EnderlingStructureFeature ENDERLING_STRUCTURE_FEATURE = new EnderlingStructureFeature(EnderlingStructureFeatureConfig.CODEC);
    public static void register() {
        Registry.register(Registry.FEATURE, ENDERLING_STRUCTURE_FEATURE_ID, ENDERLING_STRUCTURE_FEATURE);
        // unfortunately it seems that this is the only way to change the overworld without completely redefining
        // so no datapack hack
        // todo on update: use the #minecraft:is_forest tag
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.SURFACE_STRUCTURES,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(TechnoEnderling.MOD_ID, "enderworld_portal_overworld"))
        );
    }
}
