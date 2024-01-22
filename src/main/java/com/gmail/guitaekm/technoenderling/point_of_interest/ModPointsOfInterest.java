package com.gmail.guitaekm.technoenderling.point_of_interest;

import com.gmail.guitaekm.technoenderling.blocks.ModBlocks;
import com.gmail.guitaekm.technoenderling.mixin.PointOfInterestTypeMixin;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.impl.biome.modification.BuiltInRegistryKeys;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModPointsOfInterest {

    public static PointOfInterestType ENDERWORLD_PORTAL_1;
    public static PointOfInterestType ENDERWORLD_PORTAL_2;
    public static PointOfInterestType ENDERWORLD_PORTAL_3;
    public static void registerClass() {
        /*
        ModPointsOfInterest.ENDERWORLD_PORTAL_1 = PointOfInterestHelper.register(
                "enderworld_portal_block_1",
                PointOfInterestTypeMixin.getAllStatesOf(ModBlocks.ENDERWORLD_PORTAL_BLOCK_1),
                0,
                1);
        ModPointsOfInterest.ENDERWORLD_PORTAL_2 = PointOfInterestTypeMixin.register(
                "enderworld_portal_block_2",
                PointOfInterestTypeMixin.getAllStatesOf(ModBlocks.ENDERWORLD_PORTAL_BLOCK_2),
                0,
                1);
        ModPointsOfInterest.ENDERWORLD_PORTAL_3 = PointOfInterestTypeMixin.register(
                "enderworld_portal_block_3",
                PointOfInterestTypeMixin.getAllStatesOf(ModBlocks.ENDERWORLD_PORTAL_BLOCK_3),
                0,
                6);
         */
        ModPointsOfInterest.ENDERWORLD_PORTAL_1 = PointOfInterestHelper.register(
                new Identifier("technoenderling", "enderworld_portal_block_1"),
                1,
                1,
                List.of(ModBlocks.ENDERWORLD_PORTAL_BLOCK_1.getDefaultState())
        );
        ModPointsOfInterest.ENDERWORLD_PORTAL_2 = PointOfInterestHelper.register(
                new Identifier("technoenderling", "enderworld_portal_block_2"),
                1,
                1,
                List.of(ModBlocks.ENDERWORLD_PORTAL_BLOCK_2.getDefaultState())
        );
        ModPointsOfInterest.ENDERWORLD_PORTAL_3 = PointOfInterestHelper.register(
                new Identifier("technoenderling", "enderworld_portal_block_3"),
                1,
                1,
                ModBlocks.ENDERWORLD_PORTAL_BLOCK_3
        );

    }
}
