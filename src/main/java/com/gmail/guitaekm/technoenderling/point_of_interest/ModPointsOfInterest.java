package com.gmail.guitaekm.technoenderling.point_of_interest;

import com.gmail.guitaekm.technoenderling.blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.List;
import java.util.function.Predicate;

public class ModPointsOfInterest {

    public static PointOfInterestType ENDERWORLD_PORTAL_1;
    public static PointOfInterestType ENDERWORLD_PORTAL_2;
    public static PointOfInterestType ENDERWORLD_PORTAL_3;

    public static final Predicate<PointOfInterestType> IS_ENDERWORLD_PORTAL = (PointOfInterestType poiType) -> {
        if(poiType.contains(ModBlocks.ENDERWORLD_PORTAL_BLOCK_1.getDefaultState())) {
            return true;
        }
        if(poiType.contains(ModBlocks.ENDERWORLD_PORTAL_BLOCK_2.getDefaultState())) {
            return true;
        }
        return poiType.contains(ModBlocks.ENDERWORLD_PORTAL_BLOCK_3.getDefaultState());
    };
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
