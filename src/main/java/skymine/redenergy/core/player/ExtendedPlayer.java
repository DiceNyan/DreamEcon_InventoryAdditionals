package skymine.redenergy.core.player;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import skymine.redenergy.core.Core;
import skymine.redenergy.core.proxy.ServerController;

/**
 * Handles all extended data objects
 * @author RedEnergy
 */
public class ExtendedPlayer implements IExtendedEntityProperties{

	public final EntityPlayer player;
	public static final String DATA_NAME = "SkyMineData";
	public List<ExtendedPlayerDataHandler> dataObjects;
	
	public ExtendedPlayer(EntityPlayer player){
		this.player = player;
		dataObjects = new ArrayList<ExtendedPlayerDataHandler>();
		for(Class clazz : Core.getDataHandlers()){
			try{
				Constructor[] cor = clazz.getDeclaredConstructors();
				if(cor.length == 1){
					dataObjects.add((ExtendedPlayerDataHandler) cor[0].newInstance());
				} else {
					throw new RuntimeErrorException(new Error("ExtendedPlayerDataHandler cannot have more than 1 constructor!"));
				}
			} catch(Exception e){
				e.printStackTrace();
				continue;
			}
		}
	}
	
	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(DATA_NAME, new ExtendedPlayer(player));
	}
	
	
	public static final ExtendedPlayer get(EntityPlayer player) {
		return (ExtendedPlayer) player.getExtendedProperties(DATA_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound props = new NBTTagCompound();
		for(ExtendedPlayerDataHandler handler : dataObjects){
			props.setByteArray(handler.getUUID().toString(), handler.serialize());
		}
		compound.setTag(DATA_NAME, props);
		
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) { 
		NBTTagCompound props = (NBTTagCompound) compound.getTag(DATA_NAME);
		for(int i = 0; i < dataObjects.size(); i++){
			ExtendedPlayerDataHandler handler = dataObjects.get(i);
			dataObjects.set(i, (ExtendedPlayerDataHandler) handler.deserialize(props.getByteArray(handler.getUUID().toString())));
		}
	}

	public ExtendedPlayerDataHandler getHandlerById(String uuid){
		return getHandlerById(UUID.fromString(uuid));
	}
	public ExtendedPlayerDataHandler getHandlerById(UUID uuid){
		ExtendedPlayerDataHandler retHandler = null;
		if(this.dataObjects != null && !this.dataObjects.isEmpty()){
			for(ExtendedPlayerDataHandler handler : this.dataObjects){
				if(handler.getUUID().toString().equals(uuid.toString())){
					retHandler = handler;
				}
			}
		}
		return retHandler;
	}
	@Override
	public void init(Entity entity, World world){}
	
	private static final String getSaveKey(EntityPlayer player) {
		return player.getCommandSenderName() + ":" + DATA_NAME;
	}
	
	public static final void saveProxyData(EntityPlayer player) {
		NBTTagCompound savedData = new NBTTagCompound();
		ExtendedPlayer.get(player).saveNBTData(savedData);
		ServerController.storeEntityData(getSaveKey(player), savedData);
	}
	
	public static final void loadProxyData(EntityPlayer player) {
		ExtendedPlayer playerData = ExtendedPlayer.get(player);
		NBTTagCompound savedData = ServerController.getEntityData(getSaveKey(player));
		if (savedData != null) { playerData.loadNBTData(savedData); }
		// data can by synced just by sending the appropriate packet, as everything
		// is handled internally by the packet class
	}

	public void onUpdate() {
		
	}
}
