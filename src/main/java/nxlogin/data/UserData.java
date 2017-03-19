package nxlogin.data;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import nxlogin.Main;

public class UserData {
	private static UserData instance;
	public static Map<String, Object> password = new HashMap<>();
	public static Map<String, Object> address = new HashMap<>();

	public UserData(Main plugin) {
		plugin.getDataFolder().mkdirs();
		Config password = new Config(new File(plugin.getDataFolder(), "password.json"), Config.JSON);
		Config address = new Config(new File(plugin.getDataFolder(), "address.json"), Config.JSON);

		UserData.password = password.getAll();
		UserData.address = address.getAll();
		instance = this;
	}

	/*
	 * public LinkedHashMap<String, Object> configSet(Config config) {
	 * LinkedHashMap<String, Object> map = new LinkedHashMap<>(); map =
	 * (LinkedHashMap<String, Object>) config.getAll(); return map; }
	 */

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

	public boolean isRegisterUser(String user) {
		return UserData.password.containsKey(user.toLowerCase());
	}

	public boolean register(String user, String password) {
		Player guest;
		if (!Main.unLogins.contains(guest = Server.getInstance().getPlayer(user))) {
			Server.getInstance().getPlayer(user).sendMessage(Main.command("당신은 이미 로그인 되어있습니다"));
			return false;
		} else if (password.length() < 5) {
			guest.sendMessage(Main.alert("회원가입이 정상적으로 이루어지지 않았습니다 §c사유: 비밀번호의 길이는 최소 다섯글자입니다"));
			return false;
		} else if (isRegisterUser(user)) {
			guest.sendMessage(Main.alert("당신은 이미 회원가입정보가 있습니다"));
			return false;
		}
		UserData.password.put(user.toLowerCase(), password.toLowerCase());
		UserData.address.put(user.toLowerCase(), guest.getAddress());
		guest.sendMessage(Main.success("회원가입이 성공적으로 이루어졌습니다 b:: §l{pw}").replaceAll("{pw}", password.toLowerCase()));
		return true;
	}

	public boolean ipLogin(Player user) {
		if (isRegisterUser(user.getName())) {
			if (user.getAddress().contains(getLastIp(user.getName()))) {
				user.sendMessage(Main.success("마지막으로 로그인한 아이피로 들어오셨습니다 §b:: §l자동로그인 되었습니다"));
				Main.unLogins.remove(user);
				return true;
			} else {
				user.sendMessage(Main.command("자동로그인이 성공하지 못했습니다 §6:: §l채팅창에 비밀번호를 입력하여 로그인해주세요"));
				return false;
			}
		}
		user.sendMessage(Main.command("자동로그인이 성공하지 못했습니다 §6:: §l/회원가입 <설정할 비밀번호> 명령어를 통해 회원가입해주세요"));
		return false;
	}

	public boolean passwordLogin(Player user, String password) {
		if (isRegisterUser(user.getName())) {
			if (!Main.unLogins.contains(user)) {
				user.sendMessage(Main.command("당신은 이미 로그인 되어있습니다"));
				return true;
			} else if (((String) UserData.password.get(user.getName().toLowerCase())).equals(password.toLowerCase())){
				update(user.getName(), password, user.getAddress());
				Main.unLogins.remove(user);
				user.sendMessage(Main.success("성공적으로 로그인되었습니다 §b:: §l IP: {ip}").replace("{ip}", user.getAddress()));
				return true;
			}else{
				user.sendMessage(Main.alert("로그인에 성공하지 못했습니다 §c:: §l비밀번호가 옳지 않습니다"));
				return false;
			}
		} else {
			user.sendMessage(Main.command("계정정보가 존재하지 않습니다 회원가입을 진행하여주세요§6 :: §l/회원가입 <설정할 비밀번호>"));
			return false;
		}
		
	}

	public void update(String user,String password,String ip){
		UserData.address.put(user.toLowerCase(), ip);
		UserData.password.put(user.toLowerCase(), password);
		return;
	}

	public void unregister(String user) {
		password.remove(user.toLowerCase());
		address.remove(user.toLowerCase());
	}

	public String getLastIp(String user) {
		return (address.get(user.toLowerCase()) + "");
	}


}
