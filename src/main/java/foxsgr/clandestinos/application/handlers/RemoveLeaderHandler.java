package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveLeaderHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();

    public void demoteToMember(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> owner = CommandValidator.validateClanOwner(sender, args, 2, LanguageManager.WRONG_DEMOTE_LEADER_USAGE);

        if (owner == null) {
            return;
        }

        ClanPlayer player = Finder.playerByName(sender, args[1]);
        if (player == null) {
            return;
        }

        if (canDemote(sender, player, owner.first)) {
            demote(sender, player, owner.first, args[1]);
        }
    }

    private boolean canDemote(CommandSender sender, ClanPlayer player, Clan clan) {
        if (!clan.allPlayers().contains(player.id())) {
            LanguageManager.send(sender, LanguageManager.NOT_IN_YOUR_CLAN);

        } else if (clan.isOwner(player)) {
            LanguageManager.send(sender, LanguageManager.CANNOT_DEMOTE_YOURSELF);
            return false;
        } else if (clan.isMember(player)) {
            LanguageManager.send(sender, LanguageManager.CANNOT_DEMOTE_MEMBER);
            return false;
        }
        return true;
    }

    private void demote(CommandSender sender, ClanPlayer player, Clan clan, String name) {
        clan.demoteLeader(player);
        clanRepository.update(clan);

        LanguageManager.send(sender, LanguageManager.SUCCESSFUL_DEMOTE, name);

        Player member = Bukkit.getPlayer(player.id());
        if (member != null) {
            LanguageManager.send(member, LanguageManager.DEMOTED);
        }

    }

}
