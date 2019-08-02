package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.persistence.yaml.PlayerRepositoryYAML;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DebugCommand implements SubCommand {

    private static final String PREFIX =
            ChatColor.BLUE + "[" + ChatColor.AQUA + "Clandestinos DEBUG" + ChatColor.BLUE + "] " + ChatColor.YELLOW;

    @Override
    public void run(CommandSender sender, String[] args) {
        if (!CommandValidator.validateAnySender(sender, args, 2, ChatColor.RED + "Use /clan debug (type)")) {
            return;
        }

        String id = args[1].toLowerCase();
        switch (id) {
            case "loadedplayers":
                int loadedPlayersAmount = PlayerRepositoryYAML.loadedPlayers().size();
                sender.sendMessage(PREFIX + "Loaded players: " + ChatColor.BLUE + loadedPlayersAmount);
                break;
            default:
                sender.sendMessage(PREFIX + "Unkown option. Available options: loadedplayers");
        }
    }
}
