package skymine.redenergy.core.restful;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserInfo {

    private int bonus;
    private int money;
    private String username;
    private String prefix;
    private Map<DonateStatus, Long> groups;

    public UserInfo() {
        groups = new HashMap<DonateStatus, Long>();
    }

    public UserInfo(String prefix, int bonus, int money, Map groups) {
        this.prefix = prefix;
        this.bonus = bonus;
        this.money = money;
        this.groups = groups;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getBonus() {
        return bonus;
    }

    public int getMoney() {
        return money;
    }

    public Map<DonateStatus, Long> getGroups() {
        return groups;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public void setUsername(String username){
    	this.username = username;
    }

    public void setGroups(Map<DonateStatus, Long> groups) {
        this.groups = groups;
    }
    
    @Override
    public String toString(){
    	return String.format("%s(prefix=%s, money=%d, bonus=%d, groups=%s)", username, prefix, money, bonus, Arrays.toString(this.groups.values().toArray()));
    }

    public static enum DonateStatus {
        vip, premium, deluxe;
    }
}