package com.gmail.guitaekm.technoenderling;

import com.gmail.guitaekm.technoenderling.particle.ModParticles;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TechnoEnderling implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static String MOD_ID = "technoenderling";
    public static final Logger LOGGER = LoggerFactory.getLogger(TechnoEnderling.MOD_ID);

	@Override
	public void onInitialize() {
		ModParticles.registerParticles();
	}
}