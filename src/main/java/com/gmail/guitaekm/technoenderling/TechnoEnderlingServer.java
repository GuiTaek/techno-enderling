package com.gmail.guitaekm.technoenderling;

import com.gmail.guitaekm.technoenderling.blocks.ModBlocks;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructureRegistry;
import com.gmail.guitaekm.technoenderling.features.ModFeatures;
import com.gmail.guitaekm.technoenderling.items.ModItems;
import com.gmail.guitaekm.technoenderling.point_of_interest.ModPointsOfInterest;
import com.gmail.guitaekm.technoenderling.resources.ModResourcesServer;
import com.gmail.guitaekm.technoenderling.event.ModEventsServer;
import com.gmail.guitaekm.technoenderling.networking.ModNetworking;
import net.fabricmc.api.DedicatedServerModInitializer;

public class TechnoEnderlingServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		ModBlocks.register();
		ModItems.register();
		ModEventsServer.registerEvents();
		ModResourcesServer.registerResources();
		ModNetworking.registerNetworkingServer();
		EnderlingStructureRegistry.registerClass();
		ModPointsOfInterest.registerClass();
		ModFeatures.register();
	}
}