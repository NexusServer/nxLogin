package nxlogin.commands;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import nxlogin.Main;
import nxlogin.data.UserData;

public class LoginCommand extends Command {
	public LoginCommand() {
		super("로그인", "로그인 명령어", "/로그인  <비밀번호>", new String[] { "login" });
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			/**
			 * serialVersionUID waring
			 */
			private static final long serialVersionUID = 5572221817236667426L;

			{
				put("로그인", new CommandParameter[] {
						new CommandParameter("비밀번호:password", CommandParameter.ARG_TYPE_RAW_TEXT, true)
				});
			}
		});
		this.setPermission("");
		this.commandData.permission = "";
		
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		
		if (!Main.unLogins.contains((Player)sender)) {
			sender.sendMessage(Main.alert("당신은 이미 로그인 되어있습니다"));
			return true;
		}

		else if (!(args.length <= 1)) {
			sender.sendMessage(Main.alert("비밀번호를 입력하여 주세요"));
			return true;
		}

		else if (UserData.getInstance().login(sender.getName(), args[0], "")) {
			Main.unLogins.remove((Player)sender);
			sender.sendMessage(Main.success("정상적으로 로그인 되었습니다"));
			return true;
		}

		return false;

	}
}
