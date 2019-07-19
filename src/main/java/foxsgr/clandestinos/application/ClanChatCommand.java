package foxsgr.clandestinos.application;

import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClanChatCommand implements CommandExecutor {

    @Override
    @SuppressWarnings("squid:S3516") // Must always return true
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!PermissionsManager.hasAndWarn(sender, "clanchat") || args.length == 0) {
            return true;
        }

        ClanPlayer clanPlayer = Finder.fromSenderInClan(sender);
        if (clanPlayer == null) {
            return true;
        }

        ClanTag clanTag = clanPlayer.clan();
        String message = String.join(" ", args);
        for (Player player : Bukkit.getOnlinePlayers()) {
            ClanPlayer otherPlayer = Finder.findPlayer(player);
            if (otherPlayer == null) {
                continue;
            }

            ClanTag otherPlayerClan = otherPlayer.clan();
            if (otherPlayerClan != null && otherPlayerClan.equals(clanTag)) {
                player.sendMessage(message);
            }
        }

        return true;
    }
}
