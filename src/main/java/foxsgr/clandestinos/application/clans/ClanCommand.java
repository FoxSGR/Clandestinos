package foxsgr.clandestinos.application.clans;

import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.application.PermissionsManager;
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

    private static final String HELP_COMMAND = "help";
    private static final String CREATE_COMMAND = "create";
    private static final String INVITE_COMMAND = "invite";
    private static final String RELOAD_COMMAND = "reload";
    private static final String JOIN_COMMAND = "join";
    private static final String LEAVE_COMMAND = "leave";
    private static final String DISBAND_COMMAND = "disband";

    public ClanCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase(HELP_COMMAND)) {
            sendSubCommandList(sender);
            return true;
        }

        String subCommand = args[0];
        if (subCommand.equalsIgnoreCase(CREATE_COMMAND)) {
            new CreateClanHandler().createClan(sender, args);
        } else if (subCommand.equalsIgnoreCase(RELOAD_COMMAND)) {
            if (PermissionsManager.hasForSubCommandWarn(sender, RELOAD_COMMAND)) {
                // Not very reliable
                plugin.onEnable();
                plugin.onDisable();
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.AQUA + "Reloaded.");
            }
        } else if (subCommand.equalsIgnoreCase(INVITE_COMMAND)) {
            new InvitePlayerHandler().invitePlayer(sender, args);
        } else if (subCommand.equalsIgnoreCase(JOIN_COMMAND)) {
            new JoinClanHandler().joinClan(sender, args);
        } else if (subCommand.equalsIgnoreCase(LEAVE_COMMAND)) {
            new LeaveClanHandler().leaveClan(sender);
        } else if (subCommand.equalsIgnoreCase(DISBAND_COMMAND)) {
            new DisbandClanHandler().disbandClan(sender);
        } else {
            sender.sendMessage(LanguageManager.UNKNOWN_COMMAND);
        }

        return true;
    }

    private static void sendSubCommandList(CommandSender sender) {
        StringBuilder builder = new StringBuilder();
        builder.append(LanguageManager.getInstance().get(LanguageManager.COMMANDS_HEADER));
        appendSubCommand(sender, builder, CREATE_COMMAND, LanguageManager.CREATE_USAGE);
        appendSubCommand(sender, builder, INVITE_COMMAND, LanguageManager.INVITE_USAGE);
        appendSubCommand(sender, builder, LEAVE_COMMAND, LanguageManager.LEAVE_USAGE);
        appendSubCommand(sender, builder, RELOAD_COMMAND, LanguageManager.RELOAD_USAGE);
        appendSubCommand(sender, builder, DISBAND_COMMAND, LanguageManager.DISBAND_USAGE);
        sender.sendMessage(builder.toString());
    }

    private static void appendSubCommand(CommandSender sender, StringBuilder builder, String command, String commandDescriptionId) {
        if (PermissionsManager.hasForSubCommand(sender, command)) {
            builder.append('\n').append(LanguageManager.getInstance().get(commandDescriptionId));
        }
    }
}
