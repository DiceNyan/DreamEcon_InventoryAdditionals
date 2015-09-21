package org.dreamfinity.party.client.gui;

import org.dreamfinity.party.Core;
import org.dreamfinity.party.network.packets.invite.AcceptPartyInvitationPacket;
import org.dreamfinity.party.network.packets.invite.RejectPartyInvitationPacket;
import org.dreamfinity.party.utils.Localization;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;

public class GuiSuggestJoinParty extends GuiScreen {

	private int partyId;
	private String suggester;
	private GuiButton acceptButton;
	private GuiButton denyButton;
	
	public GuiSuggestJoinParty(int id, String suggester){
		this.partyId = id;
		this.suggester = suggester;
	}
	
	@Override
	public void initGui(){
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		acceptButton = new GuiButton(0, this.width / 2 - 100, this.height / 2, 100, 20, Localization.ACCEPT_BUTTON);
		denyButton = new GuiButton(1, this.width /2 + 2, this.height /2, 100, 20, Localization.REJECT_BUTTON);
		this.buttonList.add(acceptButton);
		this.buttonList.add(denyButton);
	}
	
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		this.drawCenteredString(fontRendererObj, String.format(Localization.PLAYER_INVITE_YOU_TO_PARTY, suggester), this.width / 2, this.height /2 - 20, 0xFFFFFF);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.id == acceptButton.id){
			Core.getPacketPipeline().sendToServer(new AcceptPartyInvitationPacket(partyId,suggester));
			mc.setIngameFocus();
		} else if(button.id == denyButton.id){
			Core.getPacketPipeline().sendToServer(new RejectPartyInvitationPacket(partyId, suggester));
			mc.setIngameFocus();
		}
	}

    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        if (p_73869_2_ == 1)
        {
        	Core.getPacketPipeline().sendToServer(new RejectPartyInvitationPacket(partyId, suggester));
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
    }
	
	@Override
	public void updateScreen() {}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}
