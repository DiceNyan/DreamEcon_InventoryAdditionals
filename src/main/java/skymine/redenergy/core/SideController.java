package skymine.redenergy.core;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Abstract class for client and server controllers
 * @author RedEnergy
 */
public abstract class SideController {

	/**
	 * Should be called in pre initialization event of mode
	 * @param event pre init event
	 */
	public abstract void preInit(FMLPreInitializationEvent event);
	
	/**
	 * Should be called in initialization event of mode
	 * @param event init event
	 */
	public abstract void init(FMLInitializationEvent event);
	
	/**
	 * Should be called in post initializetion event of mode
	 * @param event post init event
	 */
	public abstract void postInit(FMLPostInitializationEvent event);

}
