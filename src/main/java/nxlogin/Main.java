package nxlogin;

import java.util.ArrayList;

import cn.nukkit.Player;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.inventory.InventoryOpenEvent;

import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import nxlogin.commands.LoginCommand;
import nxlogin.commands.RegisterCommand;
import nxlogin.data.UserData;
import nxlogin.tasks.UnLoginPlayerTask;

/*
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.scheduler.Task;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.entity.Entity;

import java.net.InetSocketAddress;
import java.util.List;

import cn.nukkit.event.player.PlayerChatEvent;
*/

public class Main extends PluginBase implements Listener {
	public static ArrayList<Player> unLogins = new ArrayList<>();
	public static Main instance = null;

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getLogger().info(TextFormat.colorize("&6nxLogin Plugin is Enable"));
		this.getServer().getScheduler().scheduleRepeatingTask(new UnLoginPlayerTask(this), 40);
		this.getServer().getCommandMap().register("회원가입", new RegisterCommand());
		this.getServer().getCommandMap().register("로그인", new LoginCommand());
		new UserData(this);
		Main.instance = this;
	}

	
	public static Main getInstance() {
		return Main.instance;
	}

	@Override
	public void onDisable() {
		UserData.getInstance().save();
	}

	/*
	 * 회원가입 아이디 비밀번호 로그인 아이디 비밀번호 관리진 로그인 아이디 비밀번호 2차 비밃번호
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		String name = event.getPlayer().getName().toLowerCase();
		/*
		 * 이미 회원가입되어있는 계정인지 확인
		 */
		if (UserData.getInstance().isRegister(name)) {
			/*
			 * 마지막으로 접속한 아이피랑 같은 아이피인지 대조
			 */
			if (UserData.getInstance().isLastIp(name, event.getPlayer().getAddress())) {
				UserData.getInstance().login(name, "", event.getPlayer().getAddress());
				event.getPlayer().sendMessage(Main.success("로그인이 완료되셨습니다"));
				return;
			}
			event.getPlayer().sendMessage(Main.alert("서버를 즐기시기 전에 로그인해주세요!"));
			event.getPlayer().sendMessage(Main.alert("방법 : 채팅창에 비밀번호를 입력하시거나 /로그인 비밀번호 를 입력해주세요!"));
			event.getPlayer().sendMessage(Main.alert("해당 계정이 본인의 계정이 아닐 시 다른 닉네임으로 다시 접속을 시도 해 주세요"));
			unLogins.add(event.getPlayer());
			return;
		} else {
			event.getPlayer().sendMessage(message("회원가입 후 서버를 플레이 해주세요. 명령어: §a/회원가입 <설정할 비밀번호>"));
			unLogins.add(event.getPlayer());
			return;
		}

	}

	@EventHandler
	public void commandChat(PlayerCommandPreprocessEvent event) {
		if (unLogins.contains(event.getPlayer())) {
			String command = event.getMessage().split(" ")[0];
			command = command.substring(1, command.length());
			/*event.getPlayer()
					.sendMessage(Main.command(event.getMessage() + " => " + command + " == 로그인메세지들 ="
							+ (command.contentEquals("로그인") | command.contentEquals("회원가입")
									| command.contentEquals("login") | command.contentEquals("register"))));*/
			if (!(command.contentEquals("로그인") | command.contentEquals("회원가입")
					| command.contentEquals("login") | command.contentEquals("register"))) {
				event.getPlayer().sendMessage(Main.message("로그인 혹은 회원가입 후 서버를 즐겨주세요"));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		if (Main.unLogins.contains(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onTouch(PlayerInteractEvent event) {
		if (Main.unLogins.contains(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (Main.unLogins.contains(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void openInventory(InventoryOpenEvent event) {
		if (Main.unLogins.contains(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (unLogins.contains(event.getPlayer())) {
			unLogins.remove(event.getPlayer());
			return;
		}
	}

	public static String message(String message) {
		return new StringBuilder().append("§a§l[알림] §r§7").append(message).toString();
	}

	public static String alert(String message) {
		return new StringBuilder().append("§c§l[알림] §r§7").append(message).toString();
	}

	public static String command(String message) {
		return new StringBuilder().append("§6§l[알림] §r§7").append(message).toString();
	}

	public static String success(String message) {
		return new StringBuilder().append("§b§l[안내] §r§7").append(message).toString();
	}
}
