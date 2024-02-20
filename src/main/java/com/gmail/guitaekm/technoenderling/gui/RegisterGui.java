package com.gmail.guitaekm.technoenderling.gui;

import com.gmail.guitaekm.technoenderling.TechnoEnderling;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.impl.screenhandler.ExtendedScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegisterGui {
    public static final ExtendedScreenHandlerType<TeleportScreenHandler> TELEPORT_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(
            TeleportScreenHandler::new
    );
    public static void registerServer() {
        Registry.register(
                Registry.SCREEN_HANDLER,
                new Identifier(TechnoEnderling.MOD_ID, "my_test_screen_handler"),
                TELEPORT_SCREEN_HANDLER
        );
    }
    public static void registerClient() {
        ScreenRegistry.register(TELEPORT_SCREEN_HANDLER, TeleportScreen::new);
        NetherTeleportHandler.registerClient();
    }
}
