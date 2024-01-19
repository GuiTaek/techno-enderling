package com.gmail.guitaekm.technoenderling;

import com.gmail.guitaekm.technoenderling.blocks.ModBlocks;
import com.gmail.guitaekm.technoenderling.features.ConvertibleDatapackStructure;
import com.gmail.guitaekm.technoenderling.features.EnderlingStructureRegistry;
import com.gmail.guitaekm.technoenderling.resources.ModResourcesServer;
import com.gmail.guitaekm.technoenderling.event.ModEventsServer;
import com.gmail.guitaekm.technoenderling.networking.ModNetworking;
import net.fabricmc.api.DedicatedServerModInitializer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechnoEnderlingServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		ModBlocks.register();
		ModEventsServer.registerEvents();
		ModResourcesServer.registerResources();
		ModNetworking.registerNetworkingServer();
		EnderlingStructureRegistry.registerClass();
	}
}