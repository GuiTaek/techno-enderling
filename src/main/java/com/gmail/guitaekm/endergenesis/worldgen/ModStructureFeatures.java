package com.gmail.guitaekm.endergenesis.worldgen;

import com.gmail.guitaekm.endergenesis.EnderGenesis;
import com.gmail.guitaekm.endergenesis.worldgen.structures.StandardStructure;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class ModStructureFeatures {

    /**
    /**
     * Registers the structure itself and sets what its path is. In this case, the
     * structure will have the Identifier of structure_tutorial:run_down_house.
     * It is always a good idea to register your Structures so that other mods and datapacks can
     * use them too directly from the registries. It great for mod/datapacks compatibility.
     */
    public static StructureFeature<StructurePoolFeatureConfig> COMMON_POCKET_PORTAL = new StandardStructure(
            StructurePoolFeatureConfig.CODEC,
            new Identifier(
                    EnderGenesis.MOD_ID,
                    "pocket_portal"
            ),
            new Identifier(
                    EnderGenesis.MOD_ID,
                    "pocket_portal"
            )
    );
    public static StructureFeature<StructurePoolFeatureConfig> RARE_POCKET_PORTAL = new StandardStructure(
            StructurePoolFeatureConfig.CODEC,
            new Identifier(
                    EnderGenesis.MOD_ID,
                    "pocket_portal"
            ),
            new Identifier(
                    EnderGenesis.MOD_ID,
                    "pocket_portal"
            )
    );

    public static StructureFeature<StructurePoolFeatureConfig> ENDERWORLD_PORTAL = new StandardStructure(
            StructurePoolFeatureConfig.CODEC,
            new Identifier(
                    EnderGenesis.MOD_ID,
                    "enderworld_portal"
            ),
            new Identifier(
                    EnderGenesis.MOD_ID,
                    "enderworld_portal"
            )
    );
    public static StructureFeature<StructurePoolFeatureConfig> ENDERMAN_ENDERWORLD = new StandardStructure(
            StructurePoolFeatureConfig.CODEC,
            new Identifier(
                    EnderGenesis.MOD_ID,
                    "enderman_enderworld"
            ),
            new Identifier(
                    EnderGenesis.MOD_ID,
                    "enderman_enderworld"
            )
    );
    public static StructureFeature<StructurePoolFeatureConfig> ONE_WAY_PORTAL = new StandardStructure(
            StructurePoolFeatureConfig.CODEC,
            new Identifier(
                    EnderGenesis.MOD_ID,
                    "one_way_portal"
            ),
            new Identifier(
                    EnderGenesis.MOD_ID,
                    "one_way_portal"
            )
    );
    /**
     * This is where we use Fabric API's structure API to setup the StructureFeature
     * See the comments in below for more details.
     */
    public static void register() {

        // This is Fabric API's builder for structures.
        // It has many options to make sure your structure will spawn and work properly.
        // Give it your structure and the identifier you want for it.
        FabricStructureBuilder.create(new Identifier(EnderGenesis.MOD_ID, "common_pocket_portal"), COMMON_POCKET_PORTAL)

                /* Generation stage for when to generate the structure. there are 10 stages you can pick from!
                   This surface structure stage places the structure before plants and ores are generated. */
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)

                .defaultConfig(new StructureConfig(
                        3, /* average distance apart in chunks between spawn attempts */
                        2, /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE */
                        399117345 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */))

                /*
                 * Whether surrounding land will be modified automatically to conform to the bottom of the structure.
                 * Basically, it adds land at the base of the structure like it does for Villages and Outposts.
                 * Doesn't work well on structure that have pieces stacked vertically or change in heights.
                 *
                 * Note: The air space this method will create will be filled with water if the structure is below sealevel.
                 * This means this is best for structure above sealevel so keep that in mind.
                 */
                .adjustsSurface()

                /* Finally! Now we register our structure and everything above will take effect. */
                .register();



        // Add more structures here and so on
        // This is Fabric API's builder for structures.
        // It has many options to make sure your structure will spawn and work properly.
        // Give it your structure and the identifier you want for it.
        FabricStructureBuilder.create(new Identifier(EnderGenesis.MOD_ID, "rare_pocket_portal"), RARE_POCKET_PORTAL)

                /* Generation stage for when to generate the structure. there are 10 stages you can pick from!
                   This surface structure stage places the structure before plants and ores are generated. */
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)

                .defaultConfig(new StructureConfig(
                        17, /* average distance apart in chunks between spawn attempts */
                        12, /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE */
                        564452706 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */))

                /*
                 * Whether surrounding land will be modified automatically to conform to the bottom of the structure.
                 * Basically, it adds land at the base of the structure like it does for Villages and Outposts.
                 * Doesn't work well on structure that have pieces stacked vertically or change in heights.
                 *
                 * Note: The air space this method will create will be filled with water if the structure is below sealevel.
                 * This means this is best for structure above sealevel so keep that in mind.
                 */
                .adjustsSurface()

                /* Finally! Now we register our structure and everything above will take effect. */
                .register();


        // Add more structures here and so on
        // This is Fabric API's builder for structures.
        // It has many options to make sure your structure will spawn and work properly.
        // Give it your structure and the identifier you want for it.
        FabricStructureBuilder.create(new Identifier(EnderGenesis.MOD_ID, "enderworld_portal"), ENDERWORLD_PORTAL)

                /* Generation stage for when to generate the structure. there are 10 stages you can pick from!
                   This surface structure stage places the structure before plants and ores are generated. */
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)

                .defaultConfig(new StructureConfig(
                        7, /* average distance apart in chunks between spawn attempts */
                        5, /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE */
                        534324730 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */))

                /*
                 * Whether surrounding land will be modified automatically to conform to the bottom of the structure.
                 * Basically, it adds land at the base of the structure like it does for Villages and Outposts.
                 * Doesn't work well on structure that have pieces stacked vertically or change in heights.
                 *
                 * Note: The air space this method will create will be filled with water if the structure is below sealevel.
                 * This means this is best for structure above sealevel so keep that in mind.
                 */
                .adjustsSurface()

                /* Finally! Now we register our structure and everything above will take effect. */
                .register();

        // Add more structures here and so on
        // This is Fabric API's builder for structures.
        // It has many options to make sure your structure will spawn and work properly.
        // Give it your structure and the identifier you want for it.
        FabricStructureBuilder.create(new Identifier(EnderGenesis.MOD_ID, "enderman_enderworld"), ENDERMAN_ENDERWORLD)

                /* Generation stage for when to generate the structure. there are 10 stages you can pick from!
                   This surface structure stage places the structure before plants and ores are generated. */
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)

                .defaultConfig(new StructureConfig(
                        12, /* average distance apart in chunks between spawn attempts */
                        8, /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE */
                        791970925 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */))

                /*
                 * Whether surrounding land will be modified automatically to conform to the bottom of the structure.
                 * Basically, it adds land at the base of the structure like it does for Villages and Outposts.
                 * Doesn't work well on structure that have pieces stacked vertically or change in heights.
                 *
                 * Note: The air space this method will create will be filled with water if the structure is below sealevel.
                 * This means this is best for structure above sealevel so keep that in mind.
                 */
                .adjustsSurface()

                /* Finally! Now we register our structure and everything above will take effect. */
                .register();

        // Add more structures here and so on
        // This is Fabric API's builder for structures.
        // It has many options to make sure your structure will spawn and work properly.
        // Give it your structure and the identifier you want for it.
        FabricStructureBuilder.create(new Identifier(EnderGenesis.MOD_ID, "one_way_portal"), ONE_WAY_PORTAL)

                /* Generation stage for when to generate the structure. there are 10 stages you can pick from!
                   This surface structure stage places the structure before plants and ores are generated. */
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)

                .defaultConfig(new StructureConfig(
                        12, /* average distance apart in chunks between spawn attempts */
                        8, /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE */
                        791970925 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */))

                /*
                 * Whether surrounding land will be modified automatically to conform to the bottom of the structure.
                 * Basically, it adds land at the base of the structure like it does for Villages and Outposts.
                 * Doesn't work well on structure that have pieces stacked vertically or change in heights.
                 *
                 * Note: The air space this method will create will be filled with water if the structure is below sealevel.
                 * This means this is best for structure above sealevel so keep that in mind.
                 */
                .adjustsSurface()

                /* Finally! Now we register our structure and everything above will take effect. */
                .register();

        // Add more structures here and so on
    }
}
