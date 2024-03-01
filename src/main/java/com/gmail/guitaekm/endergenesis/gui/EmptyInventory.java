package com.gmail.guitaekm.endergenesis.gui;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class EmptyInventory extends PlayerInventory {
    public EmptyInventory() {
        super(null);
    }

    @Override
    public Text getDisplayName() {
        return Text.of("");
    }
}
