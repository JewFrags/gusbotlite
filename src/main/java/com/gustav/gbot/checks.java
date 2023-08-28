package com.gustav.gbot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public class checks {
    //to check if you're properly connected
    public static boolean isPlayerInGameWorld() {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayer player = minecraft.player;
        GuiScreen currentGui = minecraft.currentScreen;

        return minecraft.world != null && player != null && player.getHealth() > 0 && currentGui == null;
    }
}
