package io.github.confuser2188.modchecker;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("modchecker")) {
            String prefix = "&7[&aModChecker&7] &2";
            String defaultMessage = "This server is protected by &cModChecker";

            if (args.length == 0)
                send(sender, prefix + defaultMessage);
            else if (args[0].equalsIgnoreCase("version")) {
                send(sender, prefix + "Current version: §c" + Main.plugin.getDescription().getVersion());
            }
            return true;
        }
        return false;
    }

    private void send(CommandSender sender, String msg){
        sender.sendMessage(Main.applyColor(msg));
    }
}
