package skymine.redenergy.core.utils;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * 
 * @author RedEnergy
 */
@FunctionalInterface
public interface OpenGuiRunnable {
	
	public Object open(Side side, World world, int posX, int posY, int posZ, EntityPlayer player);
		
}
