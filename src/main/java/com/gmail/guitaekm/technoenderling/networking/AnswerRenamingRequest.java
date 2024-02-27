package com.gmail.guitaekm.technoenderling.networking;

import net.minecraft.network.PacketByteBuf;

public class AnswerRenamingRequest {
    public enum ButtonPressed {
        OK, FORGET, CANCEL
    }
    public ButtonPressed button;
    public String newName;
    public AnswerRenamingRequest(String newName, ButtonPressed button) {
        this.newName = newName;
        this.button = button;
    }
    public AnswerRenamingRequest(PacketByteBuf packet) {
        this.newName = packet.readString();
        this.button = packet.readEnumConstant(ButtonPressed.class);
    }
    public void writePacket(PacketByteBuf packet) {
        packet.writeString(this.newName);
        packet.writeEnumConstant(this.button);
    }
}
