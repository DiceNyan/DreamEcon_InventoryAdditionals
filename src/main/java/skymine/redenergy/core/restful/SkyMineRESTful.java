package skymine.redenergy.core.restful;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import skymine.redenergy.core.utils.HttpUtils;

public class SkyMineRESTful {
	
	private static final String API_URL = "http://91.121.222.39:8081";
	private String apikey;
	
	public SkyMineRESTful(){}
	
	public SkyMineRESTful(String apikey){
		this.apikey = apikey;
	}
	
	public List<GameServer> getServersList(){
		String response;
		try {
			response = HttpUtils.getRequest(API_URL + "/public/server/all" + "?" + "apikey=" + apikey);
			System.out.println(response);
			return new Gson().fromJson(response, new TypeToken<ArrayList<GameServer>>(){}.getType());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<GameServer>();
	}
	
	public GameServer getServer(String serverName){
		String response;
		try{
			response = HttpUtils.getRequest(API_URL + "/public/server/" + "?" + "apikey=" + apikey + "&" + "server=" + serverName);
			JsonElement parsedResponse = new JsonParser().parse(response);
			if(parsedResponse.isJsonObject() && parsedResponse.getAsJsonObject().get("error") != null){
				return null;
			} else {
				return new Gson().fromJson(parsedResponse, GameServer.class);
			}
		} catch(IOException ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public GameServer getCurrentGameServer() throws NoSuchElementException{
		List<GameServer> allServers = getServersList();
		String currentIp = MinecraftServer.getServer().getServerHostname();
		int currentPort = MinecraftServer.getServer().getPort();
		return allServers.stream()
				.filter(server -> server.getIp().equals(currentIp) && server.getPort() == currentPort)
				.findFirst()
				.orElseThrow(() -> new NoSuchElementException("Current server not registered"));
	}
	
	/**
	 * @param username
	 * @param server - can be empty
	 * @return
	 */
	public UserInfo getUserInfo(String userName, String serverName){
		String response;
		try{
			response = HttpUtils.getRequest(API_URL + "/public/user" + "?" + "apikey=" + apikey + "&" + "server=" + serverName + "&" + "name=" + userName);
			JsonElement parsedResponse = new JsonParser().parse(response);
			if(parsedResponse.isJsonObject() && parsedResponse.getAsJsonObject().get("error") != null){
				return null;
			} else {
				UserInfo ret = new Gson().fromJson(parsedResponse, UserInfo.class);
				ret.setUsername(userName);
				return ret;
			}
		} catch(IOException ex){
			ex.printStackTrace();
		}
		return null;
	}
}
