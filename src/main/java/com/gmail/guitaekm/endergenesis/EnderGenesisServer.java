package com.gmail.guitaekm.endergenesis;

import com.gmail.guitaekm.endergenesis.blocks.ModBlocks;
import com.gmail.guitaekm.endergenesis.enderling_structure.EnderlingStructureInitializer;
import com.gmail.guitaekm.endergenesis.gui.RegisterGui;
import com.gmail.guitaekm.endergenesis.items.ModItems;
import com.gmail.guitaekm.endergenesis.point_of_interest.ModPointsOfInterest;
import com.gmail.guitaekm.endergenesis.resources.ModResourcesServer;
import com.gmail.guitaekm.endergenesis.event.ModEventsServer;
import com.gmail.guitaekm.endergenesis.networking.ModNetworking;
import com.gmail.guitaekm.endergenesis.teleport.RegisterUtils;
import com.gmail.guitaekm.endergenesis.teleport.VehicleTeleport;
import com.gmail.guitaekm.endergenesis.worldgen.RegisterModStructures;
import net.fabricmc.api.DedicatedServerModInitializer;

public class EnderGenesisServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		ModBlocks.register();
		ModItems.register();
		ModEventsServer.registerEvents();
		ModResourcesServer.registerResources();
		ModNetworking.registerNetworkingServer();
		ModPointsOfInterest.registerClass();
		VehicleTeleport.register();
		RegisterModStructures.register();
		EnderlingStructureInitializer.register();
		RegisterGui.registerServer();
		RegisterUtils.registerServer();
	}
}