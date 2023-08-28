package com.gustav.gbot;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.Display;

//~candyshop 28-aug-2023

public class bot {

    public bot() {net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);}


        public void runBot () {
            if (checks.isPlayerInGameWorld()) {
                Display.setTitle("Gustav's bot b2 | Connected");
                //can put something here when connected
            }
        }

    @SubscribeEvent
   public void onChat (ClientChatEvent event){
           String message = event.getMessage();if ("#go".equals(message)) {event.setCanceled(true);Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("[GUSTAV] Baritone test."));

                //bot code
                double targetX = Minecraft.getMinecraft().player.posX + 2;
                double targetZ = Minecraft.getMinecraft().player.posZ + 2;
                BaritoneAPI.getSettings().allowSprint.value = true;
                BaritoneAPI.getSettings().primaryTimeoutMS.value = 2000L;
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoal(new GoalBlock((int) targetX, (int) Minecraft.getMinecraft().player.posY, (int) targetZ));

            }
        }
    }
