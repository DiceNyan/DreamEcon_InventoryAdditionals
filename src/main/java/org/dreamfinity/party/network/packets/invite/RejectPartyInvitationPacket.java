package org.dreamfinity.party.network.packets.invite;

import org.dreamfinity.party.utils.Localization;
import org.dreamfinity.party.network.packets.AbstractPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class RejectPartyInvitationPacket extends AbstractPacket{

	private int partyId;
	private String suggester;
	
	public RejectPartyInvitationPacket() {}
	
	public RejectPartyInvitationPacket(int id, String suggester){
		this.partyId = id;
		this.suggester = suggester;
	}
	@Override
	public void encodeInto(ChannelHandlerContext var1, ByteBuf var2) {
		String toSend = suggester+"###"+partyId;
		var2.writeBytes(toSend.getBytes());

	}

	@Override
	public void decodeInto(ChannelHandlerContext var1, ByteBuf var2) {
		String toReceive = new String(var2.array()).substring(1);
		this.suggester = toReceive.split("###")[0];
		this.partyId = Integer.parseInt(toReceive.split("###")[1]);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {}

	@Override
	public void handleServerSide(EntityPlayer var1){
		EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(suggester);
		if(player != null){
			player.addChatComponentMessage(new ChatComponentText(String.format(Localization.REJECTED_YOUR_SUGGESTION, var1.getDisplayName())));
		}
	}
}
