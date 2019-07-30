package foxsgr.clandestinos.application;

import foxsgr.clandestinos.application.listeners.ChatManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.util.Pair;
import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClanChatCommand implements CommandExecutor {

    private String format;
    private static List<String> blacklist = new ArrayList<>();

    @Override
    @SuppressWarnings("squid:S3516") // Must always return true
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!PermissionsManager.hasAndWarn(sender, "clanchat") || args.length == 0) {
            return true;
        }

        Pair<Clan, ClanPlayer> clanPlayer = Finder.fromSenderInClan(sender);
        if (clanPlayer == null) {
            return true;
        }

        String message = formatMessage(sender, args, clanPlayer.first.tag());
        sender.getServer().getConsoleSender().sendMessage(message);

        for (Player player : Bukkit.getOnlinePlayers()) {
            ClanPlayer otherPlayer = Finder.findPlayer(player);
            if (otherPlayer == null) {
                spySend(player, message);
                continue;
            }

            Optional<ClanTag> otherPlayerClan = otherPlayer.clan();
            if (otherPlayerClan.isPresent() && otherPlayerClan.get().equalsIgnoreColor(clanPlayer.first.tag())) {
                player.sendMessage(message);
            } else {
                spySend(player, message);
            }
        }

        return true;
    }

    static void toggleSpyBlacklist(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("The console will always receive the clan messages.");
        }

        String name = sender.getName();
        if (blacklist.contains(name)) {
            LanguageManager.send(sender, LanguageManager.SPY_ENABLED);
            blacklist.remove(name);
        } else {
            LanguageManager.send(sender, LanguageManager.SPY_DISABLED);
            blacklist.add(name);
        }
    }

    void setup() {
        ConfigManager configManager = ConfigManager.getInstance();
        format = configManager.getString(ConfigManager.CLAN_CHAT_FORMAT);
    }

    private void spySend(Player player, String message) {
        if (!blacklist.contains(player.getName()) && PermissionsManager.has(player, "spy")) {
            player.sendMessage("SPY " + message);
        }
    }

    private String formatMessage(CommandSender sender, String[] args, ClanTag clanTag) {
        Player player = (Player) sender;

        String messageFormat = format.replace(ChatManager.PLAYER_PLACEHOLDER, player.getDisplayName())
                .replace(ChatManager.COLORED_CLAN_TAG_PLACEHOLDER, clanTag.value());
        messageFormat = TextUtil.translateColoredText(messageFormat);

        String content = String.join(" ", args);
        return messageFormat.replace(ChatManager.CONTENT_PLACEHOLDER, content);
    }
}
