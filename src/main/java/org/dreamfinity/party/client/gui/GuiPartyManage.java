package org.dreamfinity.party.client.gui;

import java.util.HashMap;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import org.dreamfinity.party.Core;
import org.dreamfinity.party.client.ClientData;
import org.dreamfinity.party.client.gui.elements.GuiPartyPlayerPanel;
import org.dreamfinity.party.network.packets.manage.DisbandGroupPacket;
import org.dreamfinity.party.network.packets.manage.LeaveGroupPacket;
import org.dreamfinity.party.utils.Localization;

public class GuiPartyManage extends GuiScreen{

	private static ResourceLocation background = new ResourceLocation("party", "textures/gui/party-manage-bg-1.png");
	private static int backgroundWidth = 176;
	private static int backgroundHeight = 166;
	private static final String GUI_TITLE = Localization.MANAGE_GUI_TITLE;
	private int backgroundX;
	private int backgroundY;
	private HashMap<String, GuiPartyPlayerPanel> playerPanels = new HashMap();
	private GuiButton disbandButton, leaveGroupButton, closeButton;
	
	@Override
	public void drawScreen(int x, int y, float f) {
		this.drawBackground();
		this.drawString(fontRendererObj, GUI_TITLE, this.width / 2 - fontRendererObj.getStringWidth(GUI_TITLE) / 2, this.height / 2 - 100, 0xFFFFFF);
		this.drawPlayersPanel(x, y, f);
		super.drawScreen(x, y, f);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.id == disbandButton.id){
			//TODO
			Core.getPacketPipeline().sendToServer(new DisbandGroupPacket());
		} else if(button.id == leaveGroupButton.id){
			//TODO
			Core.getPacketPipeline().sendToServer(new LeaveGroupPacket());
		} else if(button.id == closeButton.id){
			this.mc.setIngameFocus();
		}
		super.actionPerformed(button);
	}

	@Override
	public void initGui() {
		backgroundX = (this.width - backgroundWidth) / 2;
		backgroundY = (this.height - backgroundHeight) / 2;
		playerPanels.clear();
		this.initPlayersPanels();
		//Note: we must start button's ids from 2 because 0 and 1 already occupied by player's panels elements
		this.disbandButton = new GuiButton(2, this.width / 2 - 80, this.height / 2 + 55, 80, 20, Localization.DISBAND_BUTTON);
		this.leaveGroupButton = new GuiButton(3, this.width / 2 - 80, this.height / 2 + 55, 80, 20, Localization.LEAVE_BUTTON);
		this.closeButton = new GuiButton(4, this.width / 2 + 2, this.height / 2 + 55, 80, 20, Localization.CLOSE_BUTTON);
		this.buttonList.add(disbandButton);
		this.buttonList.add(leaveGroupButton);
		this.buttonList.add(closeButton);
		super.initGui();
	}

	@Override
	public void updateScreen() {
		this.updatePlayersPanels();
		if(ClientData.clientParty != null){
			if(ClientData.clientParty.getLeader().getDisplayName().equals(mc.thePlayer.getDisplayName())){
				this.setLeaveButtonStatus(false);
				this.setDisbandButtonStatus(true);
			} else {
				this.setLeaveButtonStatus(true);
				this.setDisbandButtonStatus(false);
			}
		} else {
			this.setLeaveButtonStatus(false);
			this.setDisbandButtonStatus(false);
		}
		super.updateScreen();
	}

	private void setDisbandButtonStatus(boolean status){
		this.disbandButton.enabled = status;
		this.disbandButton.visible = status;
	}
	
	private void setLeaveButtonStatus(boolean status){
		this.leaveGroupButton.enabled = status;
		this.leaveGroupButton.visible = status;
	}
	@Override
	protected void mouseClicked(int x, int y, int p_73864_3_)
    {
		this.mouseClickedInPlayersPanels(x, y, p_73864_3_);
		super.mouseClicked(x, y, p_73864_3_);
    }
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public void drawBackground() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawDefaultBackground();
		this.mc.getTextureManager().bindTexture(background);
		this.drawTexturedModalRect(this.backgroundX, this.backgroundY, 0, 0, backgroundWidth, backgroundHeight);
	}
	
	private void mouseClickedInPlayersPanels(int x, int y, int par3int){
		if(!playerPanels.isEmpty()){
			for(GuiPartyPlayerPanel panel: this.playerPanels.values()){
				panel.mouseClicked(x, y, par3int);
			}
		}
	}
	
	private void updatePlayersPanels(){
		if(!playerPanels.isEmpty()){
			for(GuiPartyPlayerPanel panel: this.playerPanels.values()){
				panel.onUpdate();
			}
		}
	}
	
	private void drawPlayersPanel(int x, int y, float f){
		if(!playerPanels.isEmpty()){
			for(GuiPartyPlayerPanel panel: this.playerPanels.values()){
				panel.drawPanel(x, y, f);
			}
		}
	}
	
	private void initPlayersPanels(){
		if(ClientData.clientParty != null){
			int additionalY = 0;
			for(EntityPlayer player: ClientData.clientParty.getPlayers()){
				playerPanels.put(player.getDisplayName(), 
						new GuiPartyPlayerPanel(player, this.width / 2 - 70, this.height /2 - 59 + additionalY, this.width, this.height, 130, 40));
				additionalY += 23;
			}
		}
	}
	
	
}
