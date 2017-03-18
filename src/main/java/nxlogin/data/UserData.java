package nxlogin.data;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.nukkit.Server;
import cn.nukkit.metadata.MetadataStore;
import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import nxlogin.Main;

public class UserData {
	private static UserData instance = null;
	public static Map<String, Object> password = new HashMap<>();
	public static Map<String, Object> address = new HashMap<>();
	private static Main plugin = null;

	public UserData(Main plugin) {
		plugin.getDataFolder().mkdirs();
		Config password = new Config(new File(plugin.getDataFolder(), "password.json"), Config.JSON);
		Config address = new Config(new File(plugin.getDataFolder(), "address.json"), Config.JSON);

		UserData.password =password.getAll();
		UserData.address = address.getAll();
		UserData.plugin = plugin;
		instance = this;
	}

//	public LinkedHashMap<String, Object> configSet(Config config) {
//		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
//		map = (LinkedHashMap<String, Object>) config.getAll();
//		return map;
//	}

	public void save() {
		Config password = new Config(new File(Main.getInstance().getDataFolder(), "password.json"), Config.JSON);
		Config address = new Config(new File(Main.getInstance().getDataFolder(), "address.json"), Config.JSON);
		password.setAll((LinkedHashMap<String, Object>) UserData.password);
		address.setAll((LinkedHashMap<String, Object>) UserData.address);
		password.save();
		address.save();
	}

	public static UserData getInstance() {
		return instance;
	}


	public void register(String user, String password, String ip) {
		UserData.address.put(user.toLowerCase(), ip);
		UserData.password.put(user.toLowerCase(), password);
	}

	public boolean login(String user, String password, String ip) {
		if (address.get(user.toLowerCase()).equals(ip)) {
			Server.getInstance().getPlayer(user).namedTag.putBoolean("login", true);
			return true;
		}
		if (UserData.password.get(user.toLowerCase()).equals(password.toLowerCase())) {
			address.replace(user.toLowerCase(), ip);
			return true;
		}
		return false;
	}

	public void unregister(String user) {
		password.remove(user.toLowerCase());
		address.remove(user.toLowerCase());
	}

	public boolean isRegister(String user) {
		return password.containsKey(user.toLowerCase());
	}

	public String getLastIp(String user) {
		return (address.get(user.toLowerCase()) + "");
	}

	public boolean isLastIp(String user, String address) {
		if (getLastIp(user.toLowerCase()).equals(address)) {
			return true;
		}
		return false;
	}

}
