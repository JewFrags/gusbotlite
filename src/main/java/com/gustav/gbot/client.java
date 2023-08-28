package com.gustav.gbot;

//gustav
//august 2023 27

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

@Mod(modid = "gbot", name = "Gustav's Bot", version = "b2")
public class client {

    private boolean shouldReconnect = false;
    private boolean shouldConnectOnFirstTick = true;
    private long disconnectTime = 0;
    private static final long RECONNECT_DELAY = 10000;
    private boolean isBotRunning = false;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new bot());
    }

    @SubscribeEvent
    public void onGuiOpenEvent(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMainMenu || event.getGui() instanceof GuiMultiplayer) {
            Display.setTitle("Gustav's bot b2");
            isBotRunning = false;
            event.setGui(new CustomMainMenu());
        } else if (event.getGui() instanceof GuiDisconnected) {
            disconnectTime = System.currentTimeMillis();
            shouldReconnect = true;
            isBotRunning = false;
            event.setGui(new CustomMainMenu());
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if (shouldConnectOnFirstTick) {
            connectToServer();
            shouldConnectOnFirstTick = false;
        }

        if (Minecraft.getMinecraft().currentScreen instanceof CustomMainMenu && shouldReconnect && System.currentTimeMillis() - disconnectTime >= RECONNECT_DELAY) {
            connectToServer();
            shouldReconnect = false;
        }

        if (checks.isPlayerInGameWorld() && !isBotRunning) {
            bot myBot = new bot();
            myBot.runBot();
            isBotRunning = true;
        }
    }

    private void connectToServer() {
        ServerData serverData = new ServerData("test-server", "127.0.0.1:25565", false);
        Minecraft.getMinecraft().displayGuiScreen(new GuiConnecting(new CustomMainMenu(), Minecraft.getMinecraft(), serverData));
    }

    public class CustomMainMenu extends GuiScreen {

        @Override
        public void initGui() {
            this.buttonList.clear();
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 - 10, "www.gustav.lol"));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 20, "Connect to test server (127.0.0.1:25565)"));
            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + 50, I18n.format("menu.options")));
        }

        boolean ReconnectDebug = true;

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.drawDefaultBackground();
            GL11.glPushMatrix();
            GL11.glScalef(2.5F, 2.5F, 2.5F);
            this.drawCenteredString(this.fontRenderer, "Gustav's Bot", (int)(this.width / 2 / 2.5F), (int)((this.height / 2 - 40) / 2.5F), 0x0000FF);
            GL11.glPopMatrix();
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.drawString(this.fontRenderer, "Minecraft 1.12.2 [Gustav's bot b2]", 10, this.height - 20, 0xFFFFFF);
            if (shouldReconnect) {
                int remainingTime = (int) Math.max(0, (RECONNECT_DELAY - (System.currentTimeMillis() - disconnectTime)) / 1000);
                String countdown = "Reconnecting in: " + remainingTime + " seconds";
                this.drawString(this.fontRenderer, countdown, 10, 10, 0xFFFFFF);
                if (remainingTime == 0 && ReconnectDebug == true) {
                    Display.setTitle("Gustav's bot b2 | Reconnecting");
                    ReconnectDebug = false;
                }
            }
        }

        @Override
        protected void actionPerformed(GuiButton button) {
            switch (button.id) {
                case 0:
                    try {
                        java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.gustav.lol"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    connectToServer();
                    break;
                case 2:
                    mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                    break;
            }
        }
    }
}
