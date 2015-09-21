package skymine.redenergy.core.network.packets;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import skymine.redenergy.core.api.annotations.Packet;
import skymine.redenergy.core.network.AbstractPacket;
import skymine.redenergy.core.player.ExtendedPlayer;

@Packet
public class SyncPlayerPropsMessage extends AbstractPacket{
	
	private NBTTagCompound data;
	public SyncPlayerPropsMessage() {}

	public SyncPlayerPropsMessage(EntityPlayer player) {
		data = new NBTTagCompound();
		ExtendedPlayer.get(player).saveNBTData(data);
	}

	@Override
	public void encodeInto(ChannelHandlerContext var1, ByteBuf var2) {
		ByteBufUtils.writeTag(var2, data);
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext var1, ByteBuf var2) {
		data = ByteBufUtils.readTag(var2);		
	}

	
	@Override
	public void handleClientSide(EntityPlayer var1) {
		ExtendedPlayer.get(var1).loadNBTData(this.data);		
	}

	@Override
	public void handleServerSide(EntityPlayer var1) {}
}