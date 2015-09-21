package org.dreamfinity.party.client.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import org.dreamfinity.party.client.gui.GuiPartyManage;
import org.dreamfinity.party.client.gui.PartyAddPlayerGui;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyHandler {
	
	private Minecraft mc;
	
	public static KeyBinding partyAddPlayerKey = new KeyBinding("Add player to party", Keyboard.KEY_O, "SkyMine");
	public static KeyBinding partyOpenManageGuiKey = new KeyBinding("Opens party manage gui", Keyboard.KEY_I, "SkyMine");
	
	public KeyHandler(){	
		mc = Minecraft.getMinecraft();
		ClientRegistry.registerKeyBinding(partyAddPlayerKey);
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent e){
		EntityClientPlayerMP player = mc.thePlayer;
		if(mc.inGameHasFocus){
			if(partyAddPlayerKey.isPressed()){
				mc.displayGuiScreen(new PartyAddPlayerGui());
			}
			if(partyOpenManageGuiKey.isPressed()){
				mc.displayGuiScreen(new GuiPartyManage());
			}
		}
	}
}
