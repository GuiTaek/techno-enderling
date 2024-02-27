package com.gmail.guitaekm.technoenderling.gui;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.impl.screenhandler.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegisterGui {
    public static final ExtendedScreenHandlerType<TeleportScreenHandler> TELEPORT_SCREEN_HANDLER_TYPE = new ExtendedScreenHandlerType<>(
            TeleportScreenHandler::new
    );
    public static final ScreenHandlerType<RenamingScreenHandler> RENAMING_SCREEN_SCREEN_HANDLER_TYPE = new ExtendedScreenHandlerType<>(
            RenamingScreenHandler::new
    );
    public static void registerServer() {
        Registry.register(
                Registry.SCREEN_HANDLER,
                new Identifier(TechnoEnderling.MOD_ID, "teleport_screen"),
                TELEPORT_SCREEN_HANDLER_TYPE
        );
        Registry.register(
                Registry.SCREEN_HANDLER,
                new Identifier(TechnoEnderling.MOD_ID, "renaming_screen"),
                RENAMING_SCREEN_SCREEN_HANDLER_TYPE
        );
    }
    public static void registerClient() {
        ScreenRegistry.register(TELEPORT_SCREEN_HANDLER_TYPE, TeleportScreen::new);
        ScreenRegistry.register(RENAMING_SCREEN_SCREEN_HANDLER_TYPE, RenamingScreen::new);
        NetherTeleportHandler.registerClient();
    }
}
