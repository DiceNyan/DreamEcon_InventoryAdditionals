package org.dreamfinity.party.network.packets.invite;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.dreamfinity.party.client.gui.GuiSuggestJoinParty;
import org.dreamfinity.party.network.packets.AbstractPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SuggestJoinPartyPacket extends AbstractPacket {

	private int partyId;
	private String suggester;
	
	public SuggestJoinPartyPacket() {}
	
	public SuggestJoinPartyPacket(int id, EntityPlayer suggester){
		this.partyId = id;
		this.suggester = suggester.getDisplayName();
		System.out.println(this.suggester + "$" + partyId);
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

	@SideOnly(Side.CLIENT)
	@Override
	public void handleClientSide(EntityPlayer player) {
		System.out.println("Suggested join party with id " + partyId);
		Minecraft.getMinecraft().displayGuiScreen(new GuiSuggestJoinParty(partyId, suggester));
	}

	@Override
	public void handleServerSide(EntityPlayer var1){}

}
