package skymine.redenergy.core.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import skymine.redenergy.core.Core;
import skymine.redenergy.core.SideController;

public class ClientController extends SideController{

	@Override
	public void preInit(FMLPreInitializationEvent event) {}

	@Override
	public void init(FMLInitializationEvent event) {}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		Core.getKeyboardHandler().initialise();
	}

}
