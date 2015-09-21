package skymine.redenergy.core.player;

import java.io.Serializable;
import java.util.UUID;

/**
 * 
 * @author RedEnergy
 */
public abstract class ExtendedPlayerDataHandler implements Serializable{
	
	/**
	 * Serialize object to byte array
	 * @return Serialized byte array
	 */
	public abstract byte[] serialize();
	
	/**
	 * Converts byte array into ExtendedPlayerDataHandler object
	 * @param bytes Serialized object
	 * @return Deserialized object
	 */
	public abstract ExtendedPlayerDataHandler deserialize(byte[] bytes);
	
	/**
	 * @return Unique identifier of this data handler
	 */
	public abstract UUID getUUID();
}
