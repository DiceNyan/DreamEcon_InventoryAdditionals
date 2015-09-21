package org.dreamfinity.party.network.packets.handshake;

import org.dreamfinity.party.client.ClientData;
import org.dreamfinity.party.network.packets.AbstractPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class PartyCleanPacket extends AbstractPacket {

	@Override
	public void encodeInto(ChannelHandlerContext var1, ByteBuf var2) {}

	@Override
	public void decodeInto(ChannelHandlerContext var1, ByteBuf var2) {}

	@Override
	public void handleClientSide(EntityPlayer var1) {
		ClientData.clientParty = null;
	}

	@Override
	public void handleServerSide(EntityPlayer var1) {}

}
