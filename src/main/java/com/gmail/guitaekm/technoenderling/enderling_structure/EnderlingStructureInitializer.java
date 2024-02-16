package com.gmail.guitaekm.technoenderling.enderling_structure;

public class EnderlingStructureInitializer {
    public static ArbitraryStructureRegistry arbitraryStructureRegistry = new ArbitraryStructureRegistry();
    public static EnderlingStructureRegistry enderlingStructureRegistry = new EnderlingStructureRegistry();

    public static void register() {
        EnderlingStructureInitializer.arbitraryStructureRegistry.register();
        EnderlingStructureInitializer.enderlingStructureRegistry.register();
    }
}
