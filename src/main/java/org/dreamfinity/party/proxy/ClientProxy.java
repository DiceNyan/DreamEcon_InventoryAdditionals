package org.dreamfinity.party.proxy;

import org.dreamfinity.party.client.gui.GuiInGame;
import org.dreamfinity.party.client.handlers.KeyHandler;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends ServerProxy{
	
	@Override
	public void initProxy(){
		FMLCommonHandler.instance().bus().register(new KeyHandler());
		MinecraftForge.EVENT_BUS.register(new GuiInGame());
	}
	
}
