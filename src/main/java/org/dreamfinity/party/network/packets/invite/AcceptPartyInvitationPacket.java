package org.dreamfinity.party.network.packets.invite;

import org.dreamfinity.party.Core;
import org.dreamfinity.party.network.packets.AbstractPacket;
import org.dreamfinity.party.utils.Localization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import org.dreamfinity.party.elements.Party;

public class AcceptPartyInvitationPacket extends AbstractPacket {

	private int partyId;
	private String suggester;
	
	public AcceptPartyInvitationPacket() {}
	
	public AcceptPartyInvitationPacket(int id, String suggester){
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
		player.addChatComponentMessage(new ChatComponentText(String.format(Localization.ACCEPTED_YOUR_SUGGESTION, var1.getDisplayName())));
		Party party = Core.getPartyManager().getPartyById(partyId);
		if(party != null){
			party.addPlayer(var1);
			Core.getPartyManager().sendSyncPartyPackets(party);
		} else {
			player.addChatComponentMessage(new ChatComponentText(Localization.ERROR_HAPPEND_ADD_TO_PARTY));
		}
	}

}
