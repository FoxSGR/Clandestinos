package foxsgr.clandestinos.application;

import foxsgr.clandestinos.application.handlers.*;
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
    private static final String SPY_COMMAND = "spy";
    private static final String MAKE_LEADER_COMMAND = "makeleader";
    private static final String REMOVE_LEADER_COMMAND = "removeleader";

    ClanCommand(JavaPlugin plugin) {
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
            if (PermissionsManager.hasAndWarn(sender, RELOAD_COMMAND)) {
                // Not very reliable
                plugin.onEnable();
                plugin.onDisable();
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.AQUA + "Reloaded.");
            }
        } else if (subCommand.equalsIgnoreCase(INVITE_COMMAND)) {
            new InvitePlayerHandler().invitePlayer(sender, args);
        } else if (subCommand.equalsIgnoreCase(UNINVITE_COMMAND)) {
            new UninvitePlayerHandler().uninvitePlayer(sender, args);
        } else if (subCommand.equalsIgnoreCase(JOIN_COMMAND)) {
            new JoinClanHandler().joinClan(sender, args);
        } else if (subCommand.equalsIgnoreCase(LEAVE_COMMAND)) {
            new LeaveClanHandler().leaveClan(sender);
        } else if (subCommand.equalsIgnoreCase(DISBAND_COMMAND)) {
            new DisbandClanHandler().disbandClan(sender);
        } else if (subCommand.equalsIgnoreCase(INFO_COMMAND)) {
            new InfoHandler().showInfo(sender, args);
        } else if (subCommand.equalsIgnoreCase(KICK_COMMAND)) {
            new KickPlayerHandler().kickPlayer(sender, args);
        } else if (subCommand.equalsIgnoreCase(MODTAG_COMMAND)) {
            new ModifyTagHandler().modifyTag(sender, args);
        } else if (subCommand.equalsIgnoreCase(SPY_COMMAND)) {
            if (PermissionsManager.hasAndWarn(sender, "clandestinos.spy")) {
                ClanChatCommand.toggleSpyBlacklist(sender);
            }
        } else if (subCommand.equalsIgnoreCase(ENEMY_COMMAND)) {
            new DeclareEnemyHandler().declareEnemy(sender, args);
        } else if (subCommand.equalsIgnoreCase(MAKE_LEADER_COMMAND)) {
            new PromoteToLeaderHandler().promoteToLeader(sender, args);
        } else if (subCommand.equalsIgnoreCase(REMOVE_LEADER_COMMAND)) {
            new DemoteToMemberHandler().demoteToMember(sender, args);
        } else {
            LanguageManager.send(sender, LanguageManager.UNKNOWN_COMMAND);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return PermissionsManager.commandsWithPermission(sender, CREATE_COMMAND, INFO_COMMAND, INVITE_COMMAND,
                    UNINVITE_COMMAND, LEAVE_COMMAND, RELOAD_COMMAND, DISBAND_COMMAND, KICK_COMMAND, DISBAND_COMMAND,
                    ENEMY_COMMAND, JOIN_COMMAND, SPY_COMMAND, MAKE_LEADER_COMMAND, REMOVE_LEADER_COMMAND)
                    .stream().filter(c -> c.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args[0].equalsIgnoreCase(JOIN_COMMAND)) {
            return new ArrayList<>();
        }

        return sender.getServer().getOnlinePlayers().stream().map(HumanEntity::getName)
                .filter(p -> p.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }

    private static void sendSubCommandList(CommandSender sender) {
        StringBuilder builder = new StringBuilder();
        builder.append(LanguageManager.getInstance().get(LanguageManager.COMMANDS_HEADER));
        appendSubCommand(sender, builder, CREATE_COMMAND, LanguageManager.CREATE_USAGE);
        appendSubCommand(sender, builder, INFO_COMMAND, LanguageManager.INFO_USAGE);
        appendSubCommand(sender, builder, INVITE_COMMAND, LanguageManager.INVITE_USAGE);
        appendSubCommand(sender, builder, UNINVITE_COMMAND, LanguageManager.UNINVITE_USAGE);
        appendSubCommand(sender, builder, LEAVE_COMMAND, LanguageManager.LEAVE_USAGE);
        appendSubCommand(sender, builder, ENEMY_COMMAND, LanguageManager.ENEMY_USAGE);
        appendSubCommand(sender, builder, DISBAND_COMMAND, LanguageManager.DISBAND_USAGE);
        appendSubCommand(sender, builder, KICK_COMMAND, LanguageManager.KICK_USAGE);
        appendSubCommand(sender, builder, MODTAG_COMMAND, LanguageManager.MODTAG_USAGE);
        appendSubCommand(sender, builder, MAKE_LEADER_COMMAND, LanguageManager.MAKE_LEADER_USAGE);
        appendSubCommand(sender, builder, REMOVE_LEADER_COMMAND, LanguageManager.REMOVE_LEADER_USAGE);

        appendSubCommand(sender, builder, RELOAD_COMMAND, LanguageManager.RELOAD_USAGE);
        sender.sendMessage(TextUtil.translateColoredText(builder.toString()));
    }

    private static void appendSubCommand(CommandSender sender, StringBuilder builder, String command, String commandDescriptionId) {
        if (PermissionsManager.has(sender, command)) {
            builder.append('\n').append(LanguageManager.getInstance().get(commandDescriptionId));
        }
    }
}
