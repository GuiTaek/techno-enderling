package com.gmail.guitaekm.technoenderling.features;

import com.gmail.guitaekm.technoenderling.event.OnStructureGenerate;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Optional;
import java.util.Random;

public class EnderlingStructureFeature extends Feature<EnderlingStructureFeatureConfig> {
    public static EnderlingStructureFeatureConfig config;

    public EnderlingStructureFeature(Codec<EnderlingStructureFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<EnderlingStructureFeatureConfig> context) {
        // scraped from https://fabricmc.net/wiki/tutorial:features and refactored and adjusted to fit my needs
        StructureWorldAccess world = context.getWorld();
        // the origin is the place where the game starts trying to place the feature
        BlockPos origin = context.getOrigin();
        // we won't use the random here, but we could if we wanted to
        EnderlingStructureFeatureConfig config = context.getConfig();
        EnderlingStructureFeature.config = config;

        // don't worry about where these come from-- we'll implement these methods soon
        Identifier structureId = config.structureId();
        Vec3i offset = config.offset();
        Optional<EnderlingStructure> structureOptional = EnderlingStructureRegistry.instance().get(structureId);
        if (structureOptional.isEmpty()) {
            return false;
        }
        EnderlingStructure structure = structureOptional.get();
        // -2 so the first layer is on the ground
        Vec3i sizeOffset = structure.getPlaceable().size().add(new Vec3i(-1, -2, -1));
        BlockPos toPlace = origin
                .add(offset)  // I need this config for the reactors in ender villages
                .add(sizeOffset); // so the structures get placed on the ground
        if (!OnStructureGenerate.getInstance().callListeners(world, structure, toPlace)) {
            return false;
        }
        structure.getPlaceable().generate(world, toPlace.subtract(offset), Block.REDRAW_ON_MAIN_THREAD);
        return true;
    }
}
