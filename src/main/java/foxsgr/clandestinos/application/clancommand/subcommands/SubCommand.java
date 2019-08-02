package foxsgr.clandestinos.application.clancommand.subcommands;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    void run(CommandSender sender, String[] args);
}
