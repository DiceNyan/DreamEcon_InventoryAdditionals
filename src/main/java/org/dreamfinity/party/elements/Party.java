package org.dreamfinity.party.elements;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

public class Party {
	/**
	 * Unique identifier of party
	 */
	private int id;
	private ArrayList<EntityPlayer> players;
	private EntityPlayer leader;
	
	/**
	 * <b>Note: </b> id of party will me automatically generated
	 * @param List of players in party players (leader also must be here)
	 * @param EntityPlayer representation of party leader
	 */
	public Party(ArrayList<EntityPlayer> players, EntityPlayer leader){
		this.id = 100000 + new Random().nextInt(900000);
		this.players = players;
		this.leader = leader;
	}
	
	/**
	 * @param id - Unique identifier of party
	 * @param List of players in party players (leader also must be here)
	 * @param EntityPlayer representation of party leader
	 */
	public Party(int id, ArrayList<EntityPlayer> players, EntityPlayer leader){
		this.id = id;
		this.players = players;
		this.leader = leader;
	}
	
	
	public void sendMessageToMembers(IChatComponent chatComponent){
		for(EntityPlayer player: getPlayers()){
			player.addChatComponentMessage(chatComponent);
		}
	}
	
	public EntityPlayer getPlayerWithNickname(String name){
		EntityPlayer playerToReturn = null;
		for(EntityPlayer player: getPlayers()){
			if(player.getDisplayName().equals(name)){
				playerToReturn = player;
			}
		}
		return playerToReturn;
	}
	
	/**
	 * @return Unique identifier of party
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return List of players in party
	 */
	public ArrayList<EntityPlayer> getPlayers() {
		return players;
	}

	
	/**
	 * Adds given player to list of players inside the party
	 * @param player
	 */
	public void addPlayer(EntityPlayer player){
		players.add(player);
	}
	
	
	/**
	 * Removes given player from the list inside the party
	 * @param player
	 */
	public void removePlayer(EntityPlayer player){
		players.remove(player);
	}
	
	
	/**
	 * Returns EntityPlayer representation of party leaders=
	 * @return
	 */
	public EntityPlayer getLeader() {
		return leader;
	}

	
	/**
	 * Replaces party leader with the given player <br>
	 * <b>Note: </b> if leader will be replaced but it wont be added to list of player crash may happen
	 * @param leader
	 */
	public void setLeader(EntityPlayer leader) {
		this.leader = leader;
	}
	

}
