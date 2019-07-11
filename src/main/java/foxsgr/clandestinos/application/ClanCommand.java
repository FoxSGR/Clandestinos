package foxsgr.clandestinos.application;

import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ClanCommand implements CommandExecutor {

    /**
     * The plugin that the command belongs to.
     */
    private JavaPlugin plugin;

    private static final String CREATE_COMMAND = "create";
    private static final String INVITE_COMMAND = "invite";
    private static final String RELOAD_COMMAND = "reload";

    ClanCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sendSubCommandList(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase(CREATE_COMMAND)) {
            new CreateClanHandler().createClan(sender, args);
        } else if (args[0].equalsIgnoreCase(RELOAD_COMMAND)) {
            if (PermissionsManager.hasForSubCommandWarn(sender, RELOAD_COMMAND)) {
                ConfigManager.init(plugin);
                LanguageManager.init(plugin);
                PersistenceContext.init(plugin);
                sender.sendMessage(ChatColor.AQUA + "Reloaded config and language.");
            }
        } else if (args[0].equalsIgnoreCase(INVITE_COMMAND)) {
            new InvitePlayerHandler().invitePlayer(sender, args);
        }

        return true;
    }

    private static void sendSubCommandList(CommandSender sender) {
        StringBuilder builder = new StringBuilder();
        builder.append(LanguageManager.get(LanguageManager.COMMANDS_HEADER));
        appendSubCommand(sender, builder, CREATE_COMMAND, LanguageManager.CREATE_USAGE);
        appendSubCommand(sender, builder, RELOAD_COMMAND, LanguageManager.RELOAD_USAGE);
        sender.sendMessage(builder.toString());
    }

    private static void appendSubCommand(CommandSender sender, StringBuilder builder, String command, String commandDescriptionId) {
        if (PermissionsManager.hasForSubCommand(sender, command)) {
            builder.append('\n').append(LanguageManager.get(commandDescriptionId));
        }
    }
}
