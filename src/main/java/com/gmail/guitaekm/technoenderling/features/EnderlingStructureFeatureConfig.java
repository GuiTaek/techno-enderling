package com.gmail.guitaekm.technoenderling.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.feature.FeatureConfig;

public record EnderlingStructureFeatureConfig(Identifier structureId, Vec3i offset) implements FeatureConfig {
    public static final Codec<EnderlingStructureFeatureConfig> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("structure_id").forGetter(EnderlingStructureFeatureConfig::structureId),
                    Vec3i.CODEC.fieldOf("offset").forGetter(EnderlingStructureFeatureConfig::offset)
            ).apply(instance, EnderlingStructureFeatureConfig::new)
    );
}
