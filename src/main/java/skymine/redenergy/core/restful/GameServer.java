package skymine.redenergy.core.restful;

public class GameServer {

    private String serverTitle;
    private String ip;
    private int port;
    private int onlinePlayer;
    private int maxPlayer;
    private boolean reachable;

    public String getServerTitle() {
        return serverTitle;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public boolean isReachable() {
        return reachable;
    }

    public int getOnlinePlayer() {
        return onlinePlayer;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }
    
    @Override
    public String toString(){
    	return String.format("%s(ip=%s, port=%d, online=%d, maxOnline=%d, reachable=%b)", serverTitle, ip, port, onlinePlayer, maxPlayer, reachable);
    }

}