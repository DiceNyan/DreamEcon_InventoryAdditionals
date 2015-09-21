package skymine.redenergy.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.reflections.Reflections;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import skymine.redenergy.core.api.annotations.Packet;
import skymine.redenergy.core.api.annotations.SkyMineMod;
import skymine.redenergy.core.client.GuiHandler;
import skymine.redenergy.core.client.keyboard.KeyboardHandler;
import skymine.redenergy.core.config.ConfigLoader;
import skymine.redenergy.core.config.ConfigProp;
import skymine.redenergy.core.database.DatabaseConnector;
import skymine.redenergy.core.events.DataSyncingEvents;
import skymine.redenergy.core.logging.SkyMineLogger;
import skymine.redenergy.core.network.NetworkHandler;
import skymine.redenergy.core.player.ExtendedPlayerDataHandler;
import skymine.redenergy.core.restful.SkyMineRESTful;
import skymine.redenergy.core.utils.OpenGuiRunnable;

/**
 * Main library for all mods by SkyMine Minecraft Server, contains all important staff and utilities
 * @author RedEnergy
 */
@SkyMineMod
@Mod(modid = "smcore", name = "SkyMine Core ", version = "0.1.1", dependencies = "required-after:FML")
public final class Core {

	static{
		try{
			inDevEnv = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null; //In non dev enviroment this class would be obfuscated and wouldn't be found
		} catch(IOException ex){
			inDevEnv  = false;
			ex.printStackTrace();
		}
	}
	
	@Mod.Instance
	public Core instance;
	
	@ConfigProp(info = "Enables debug mod")
	public static boolean debug = true;
	
	
	private static boolean inDevEnv = true;
	
	/**
	 * An instance of pipeline for packets 
	 */
	private static NetworkHandler networkHandler;
	/**
	 * An instance of keyboard handler
	 */
	private static KeyboardHandler keyBoardHandler;
	/**
	 * An instance of database connector 
	 */
	private static DatabaseConnector databaseConnector;
	/**
	 * List of all data handler classes
	 */
	private static List<Class<? extends ExtendedPlayerDataHandler>> dataHandlers;
	public static ConfigLoader configLoader;
	/**
	 * Instance of Reflections library for "skymine.*" package
	 */
	public static Reflections reflections;
	/**
	 * List of all sky mine mods classes
	 */
	public static List<Class> skyMineMods;
	public static HashMap<Integer, OpenGuiRunnable> bothSidedGuis = new HashMap();
	
	@SidedProxy(serverSide = "skymine.redenergy.core.proxy.ServerController", clientSide = "skymine.redenergy.core.proxy.ClientController")
	public static SideController coreProxy;
	
	@Mod.EventHandler
	public static void preLoad(FMLPreInitializationEvent e){
		reflections = new Reflections("skymine.*");
		configLoader = new ConfigLoader(Core.class, e.getModConfigurationDirectory(), "SkyMineCore");
		configLoader.loadConfig();
		SkyMineLogger.setLogger(LogManager.getLogger(SkyMineLogger.PREFIX));
		SkyMineLogger.setDebugEnabled(debug);
		skyMineMods = new ArrayList<Class>();
		networkHandler = new NetworkHandler();
		networkHandler.initialise();
		databaseConnector = new DatabaseConnector();
		keyBoardHandler = new KeyboardHandler();
		dataHandlers = new ArrayList<Class<? extends ExtendedPlayerDataHandler>>();
		MinecraftForge.EVENT_BUS.register(new DataSyncingEvents());
		FMLCommonHandler.instance().bus().register(new DataSyncingEvents());
		coreProxy.preInit(e);
	}
	
	/**
	 * <b>Note:</b> all packets must be registered here <b>AND ONLY</b> here
	 */
	@Mod.EventHandler
	public static void load(FMLInitializationEvent e){
		//NetworkRegistry.INSTANCE.registerGuiHandler(Core.instance, new GuiHandler());
		registerPackets(reflections);
		regiterSkyMineMods(reflections);
		coreProxy.init(e);
	}
	
	@Mod.EventHandler 
	public static void postLoad(FMLPostInitializationEvent e){
		if(!getSkyMineMods().isEmpty()){
			SkyMineLogger.info("List of found SkyMine Mods: ");
			getSkyMineMods().forEach(c -> {
				Mod mod = ((Mod)c.getAnnotation(Mod.class));
				SkyMineLogger.info(mod.name() + ":" + mod.version());});
		}
		networkHandler.postInitialise();
		coreProxy.postInit(e);

	}
	
	/**
	 * @return <code>true</code> if mod is currently running in ForgeGradle dev environment, and <code>false</code> if it's running in obfuscated environment
	 */
	public static boolean isInDevEnvironment(){
		return inDevEnv;
	}
	
	public static NetworkHandler getNetworkHandler(){
		return networkHandler;
	}
	
	public static KeyboardHandler getKeyboardHandler(){
		return keyBoardHandler;
	}
	
	
	public static DatabaseConnector getDatabaseConnector(){
		return databaseConnector;
	}
	
	public static void registerExtendedPlayerDataHandler(Class<? extends ExtendedPlayerDataHandler> clazz){
		dataHandlers.add(clazz); 
	}
	
	public static List<Class<? extends ExtendedPlayerDataHandler>> getDataHandlers(){
		return dataHandlers;
	}
	
	public static List<Class> getSkyMineMods(){
		return skyMineMods;
	}
	
	public static void registerSkyMineMod(Class clazz){
		if(clazz.isAnnotationPresent(Mod.class)){
			skyMineMods.add(clazz);
		}
	}
	
	public static void registerBothSidedGui(int id, OpenGuiRunnable runnable){
		bothSidedGuis.put(id, runnable);
		SkyMineLogger.debug("Registered both sided gui with id %d", id);
	}
	
	public static Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z){
		OpenGuiRunnable run = bothSidedGuis.get(ID);
		if(run != null){
			Object object = run.open(side, world, x, y, z, player);
			SkyMineLogger.debug("Gui element with id %d on %s side is and instance of %s", ID, side, object instanceof Container ? "Container" : object instanceof GuiContainer ? "GuiContainer" : "" );
			return object;
		} else {
			SkyMineLogger.error("No registered gui found for id %d", ID);
			return null;
		}
	}
	
	public static void openGui(int id, EntityPlayer player, World world, int posX, int posY, int posZ){
		SkyMineLogger.debug("Trying to open gui with id %d, at %d, %d, %d", id, posX, posY, posZ);
		//player.openGui(Core.instance, id, world, posX, posY, posZ);
	}
	
	private static void registerPackets(Reflections reflections){
		reflections.getTypesAnnotatedWith(Packet.class).forEach(getNetworkHandler()::registerPacket);
	}
	
	private static void regiterSkyMineMods(Reflections reflections){
		reflections.getTypesAnnotatedWith(SkyMineMod.class).forEach(Core::registerSkyMineMod);
	}

}
