package skymine.redenergy.core.client;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import skymine.redenergy.core.Core;
import skymine.redenergy.core.logging.SkyMineLogger;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		SkyMineLogger.debug("getServerGuiElement called with id %s, at %d, %d, %d", ID, x, y, z);
		return Core.getGuiElement(Side.SERVER, ID, player, world, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		SkyMineLogger.debug("getClientGuiElement called with id %s, at %d, %d, %d", ID, x, y, z);
		return Core.getGuiElement(Side.CLIENT, ID, player, world, x, y, z);
	}

}
