package foxsgr.clandestinos.application.clancommand;

import foxsgr.clandestinos.application.clanchatcommand.ClanChatCommand;
import foxsgr.clandestinos.application.PermissionsManager;
import foxsgr.clandestinos.application.config.LanguageManager;
import foxsgr.clandestinos.application.clancommand.subcommands.*;
import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClanCommand implements CommandExecutor, TabCompleter {

    /**
     * The plugin that the command belongs to.
     */
    private JavaPlugin plugin;

    private static final String HELP_COMMAND = "help";
    private static final String CREATE_COMMAND = "create";
    private static final String INVITE_COMMAND = "invite";
    private static final String UNINVITE_COMMAND = "uninvite";
    private static final String RELOAD_COMMAND = "reload";
    private static final String JOIN_COMMAND = "join";
    private static final String LEAVE_COMMAND = "leave";
    private static final String DISBAND_COMMAND = "disband";
    private static final String INFO_COMMAND = "info";
    private static final String KICK_COMMAND = "kick";
    private static final String MODTAG_COMMAND = "modtag";
    private static final String ENEMY_COMMAND = "enemy";
    private static final String UNENEMY_COMMAND = "unenemy";
    private static final String SPY_COMMAND = "spy";
    private static final String MAKE_LEADER_COMMAND = "makeleader";
    private static final String REMOVE_LEADER_COMMAND = "removeleader";
    private static final String DEBUG_COMMAND = "debug";
    private static final String LIST_COMMAND = "list";
    private static final String TOP_COMMAND = "top";
    private static final String FF_COMMAND = "ff";
    private static final String CLAN_FF_COMMAND = "clanff";

    public ClanCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase(HELP_COMMAND)) {
            sendSubCommandList(sender);
            return true;
        }

        SubCommand subCommand;
        String subCommandId = args[0].toLowerCase();
        switch (subCommandId) {
            case RELOAD_COMMAND:
                if (PermissionsManager.hasAndWarn(sender, RELOAD_COMMAND)) {
                    // Not very reliable
                    plugin.onEnable();
                    plugin.onDisable();
                    plugin.reloadConfig();
                    sender.sendMessage(ChatColor.AQUA + "Reloaded.");
                }

                return true;
            case CREATE_COMMAND:
                subCommand = new CreateCommand();
                break;
            case INVITE_COMMAND:
                subCommand = new InviteCommand();
                break;
            case UNINVITE_COMMAND:
                subCommand = new UninviteSubCommand();
                break;
            case JOIN_COMMAND:
                subCommand = new JoinCommand();
                break;
            case LEAVE_COMMAND:
                subCommand = new LeaveCommand();
                break;
            case DISBAND_COMMAND:
                subCommand = new DisbandCommand();
                break;
            case INFO_COMMAND:
                subCommand = new InfoCommand();
                break;
            case KICK_COMMAND:
                subCommand = new KickCommand();
                break;
            case MODTAG_COMMAND:
                subCommand = new ModTagCommand();
                break;
            case SPY_COMMAND:
                if (PermissionsManager.hasAndWarn(sender, "clandestinos.spy")) {
                    ClanChatCommand.toggleSpyBlacklist(sender);
                }

                return true;
            case ENEMY_COMMAND:
                subCommand = new EnemyCommand();
                break;
            case UNENEMY_COMMAND:
                subCommand = new UnenemySubCommand();
                break;
            case MAKE_LEADER_COMMAND:
                subCommand = new MakeLeaderCommand();
                break;
            case REMOVE_LEADER_COMMAND:
                subCommand = new RemoveLeaderCommand();
                break;
            case DEBUG_COMMAND:
                subCommand = new DebugCommand();
                break;
            case LIST_COMMAND:
                subCommand = new ListCommand();
                break;
            case TOP_COMMAND:
                subCommand = new ListCommand();
                args[0] = LIST_COMMAND;
                break;
            case FF_COMMAND:
                subCommand = new FFCommand();
                break;
            case CLAN_FF_COMMAND:
                subCommand = new ClanFFCommand();
                break;
            default:
                LanguageManager.send(sender, LanguageManager.UNKNOWN_COMMAND);
                return true;
        }

        subCommand.run(sender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return PermissionsManager.commandsWithPermission(sender, CREATE_COMMAND, INFO_COMMAND, INVITE_COMMAND,
                    UNINVITE_COMMAND, LEAVE_COMMAND, RELOAD_COMMAND, DISBAND_COMMAND, KICK_COMMAND, DISBAND_COMMAND,
                    ENEMY_COMMAND, JOIN_COMMAND, SPY_COMMAND, MAKE_LEADER_COMMAND, REMOVE_LEADER_COMMAND,
                    MODTAG_COMMAND, UNENEMY_COMMAND, LIST_COMMAND, TOP_COMMAND, FF_COMMAND, CLAN_FF_COMMAND)
                    .stream().filter(c -> c.toLowerCase().contains(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args[0].equalsIgnoreCase(JOIN_COMMAND)) {
            return new ArrayList<>();
        }

        return sender.getServer().getOnlinePlayers().stream().map(HumanEntity::getName)
                .filter(p -> p.toLowerCase().contains(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }

    private static void sendSubCommandList(CommandSender sender) {
        StringBuilder builder = new StringBuilder();
        builder.append(LanguageManager.getInstance().get(LanguageManager.COMMANDS_HEADER));
        appendSubCommand(sender, builder, CREATE_COMMAND, LanguageManager.CREATE_USAGE);
        appendSubCommand(sender, builder, INFO_COMMAND, LanguageManager.INFO_USAGE);
        appendSubCommand(sender, builder, LIST_COMMAND, LanguageManager.LIST_USAGE);
        appendSubCommand(sender, builder, INVITE_COMMAND, LanguageManager.INVITE_USAGE);
        appendSubCommand(sender, builder, UNINVITE_COMMAND, LanguageManager.UNINVITE_USAGE);
        appendSubCommand(sender, builder, LEAVE_COMMAND, LanguageManager.LEAVE_USAGE);
        appendSubCommand(sender, builder, ENEMY_COMMAND, LanguageManager.ENEMY_USAGE);
        appendSubCommand(sender, builder, UNENEMY_COMMAND, LanguageManager.UNENEMY_USAGE);
        appendSubCommand(sender, builder, DISBAND_COMMAND, LanguageManager.DISBAND_USAGE);
        appendSubCommand(sender, builder, KICK_COMMAND, LanguageManager.KICK_USAGE);
        appendSubCommand(sender, builder, MODTAG_COMMAND, LanguageManager.MODTAG_USAGE);
        appendSubCommand(sender, builder, MAKE_LEADER_COMMAND, LanguageManager.MAKE_LEADER_USAGE);
        appendSubCommand(sender, builder, REMOVE_LEADER_COMMAND, LanguageManager.REMOVE_LEADER_USAGE);
        appendSubCommand(sender, builder, FF_COMMAND, LanguageManager.FF_USAGE);
        appendSubCommand(sender, builder, CLAN_FF_COMMAND, LanguageManager.CLAN_FF_DISABLED);

        appendSubCommand(sender, builder, RELOAD_COMMAND, LanguageManager.RELOAD_USAGE);
        sender.sendMessage(TextUtil.translateColoredText(builder.toString()));
    }

    private static void appendSubCommand(CommandSender sender, StringBuilder builder, String command, String commandDescriptionId) {
        if (PermissionsManager.has(sender, command)) {
            builder.append('\n').append(LanguageManager.getInstance().get(commandDescriptionId));
        }
    }
}
