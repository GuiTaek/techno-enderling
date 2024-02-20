package com.gmail.guitaekm.technoenderling.mixin;

import com.gmail.guitaekm.technoenderling.access.IMouseMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Mouse.class)
public class MouseMixin implements IMouseMixin {
    protected boolean keepScreen;

    @Override
    public void setKeepScreen() {
        this.keepScreen = true;
    }

    @Override
    public void unsetKeepScreen() {
        this.keepScreen = false;
    }

    @Redirect(method = "lockCursor",
    at=@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void keepScreen(MinecraftClient client, Screen screen) {
        if (!this.keepScreen) {
            client.setScreen(null);
        }
    }
}
