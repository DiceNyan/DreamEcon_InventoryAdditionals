package skymine.redenergy.vaultwrapper;


import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import skymine.redenergy.core.api.annotations.SkyMineMod;
import skymine.redenergy.core.logging.SkyMineLogger;

@SkyMineMod
@Mod(modid = "smvault", acceptableRemoteVersions = "*")
public class SkyMineVault{
	
	@SideOnly(Side.SERVER)
	private static Economy economy;
	@SideOnly(Side.SERVER)
	private static Permission permission;
	@SideOnly(Side.SERVER)
	public static Chat chat;
	

	@Mod.Instance
	public SkyMineVault instance;
	
	@Mod.EventHandler
	public static void preLoad(FMLPreInitializationEvent event){
	}

	@Mod.EventHandler
	public static void load(FMLInitializationEvent event){
	
	}
	
	@SideOnly(Side.SERVER)
	public static Economy getEconomy(){
		return economy;
	}
	
	@SideOnly(Side.SERVER)
	public static Permission getPermission(){
		return permission;
	}

	@SideOnly(Side.SERVER)
	public static Chat getChat(){
		return chat;
	}
	
	@SideOnly(Side.SERVER)
	private static void setupEconomy() throws ClassNotFoundException{
		if(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null){
			SkyMineLogger.error("Cannot find Vault!");
			return;
		}
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if(rsp == null){
			SkyMineLogger.error("Registered Service Provider for Economy.class not found");
			return;
		}
		economy = rsp.getProvider();
		SkyMineLogger.info("Economy successfully hooked up");
	}
	
	@SideOnly(Side.SERVER)
	public static void setupPermissions() throws ClassNotFoundException{
		if(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null){
			SkyMineLogger.info("Cannot find Vault!");
			return;
		}
		RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if(rsp == null){
			SkyMineLogger.error("Registered Service Provider for Permission.class not found");
			return;
		}
		permission = rsp.getProvider();
		SkyMineLogger.info("Permission successfully hooked up");
		
	}
	
	@SideOnly(Side.SERVER)
	public static void setupChat() throws ClassNotFoundException{
		if(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null){
			SkyMineLogger.info("Cannot find Vault!");
			return;
		}
		RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if(rsp == null){
			SkyMineLogger.error("Registered Service Provider for Chat.class not found");
			return;
		}
		chat = rsp.getProvider();
		SkyMineLogger.info("Vault Chat successfully hooked up");
	}
	
	
	@SideOnly(Side.SERVER)
	@Mod.EventHandler
	public static void serverLoad(FMLServerStartingEvent event){
		try {
			setupEconomy();
			setupPermissions();
			setupChat();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}

