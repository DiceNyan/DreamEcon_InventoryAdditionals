package org.dreamfinity.party.client.gui;

import org.dreamfinity.party.Core;
import org.dreamfinity.party.network.packets.invite.AddPlayerToPartyPacket;
import org.dreamfinity.party.utils.Localization;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

public class PartyAddPlayerGui extends GuiScreen {

	private GuiTextField nicknameField;
	private GuiButton doneButton;
	private GuiButton closeButton;
	
	@Override
	public void initGui(){
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		nicknameField = new GuiTextField(fontRendererObj, this.width / 2 - 98, this.height / 2, 200, 20);
		nicknameField.setFocused(true);
		doneButton = new GuiButton(0, this.width / 2 - 100, this.height /2 + 25, 100, 20, Localization.INVITE_BUTTON);
		closeButton = new GuiButton(1, this.width /2 + 5, this.height /2 + 25, 100, 20, Localization.CLOSE_BUTTON);
		this.buttonList.add(doneButton);
		this.buttonList.add(closeButton);
	}
	
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		this.nicknameField.mouseClicked(par1, par2, par3);
	}
	
    protected void keyTyped(char par1, int par2)
    {
    	this.nicknameField.textboxKeyTyped(par1, par2);
    }
	
	@Override
	public void drawScreen(int x, int y, float f) {
		this.nicknameField.drawTextBox();
		super.drawScreen(x, y, f);
	}

	
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == doneButton.id) {
			Core.getPacketPipeline().sendToServer(new AddPlayerToPartyPacket(nicknameField.getText()));
			mc.setIngameFocus();
		} else if(button.id == closeButton.id){
			mc.setIngameFocus();
		}
	}

	@Override
	public void updateScreen() {
		this.nicknameField.updateCursorCounter();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
