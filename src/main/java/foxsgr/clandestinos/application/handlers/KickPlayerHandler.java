package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class KickPlayerHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();

    public void kickPlayer(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanLeader = CommandValidator.validateClanLeader(sender, args, 2,
                LanguageManager.WRONG_KICK_USAGE);
        if (clanLeader == null) {
            return;
        }

        ClanPlayer kicked = Finder.playerByName(sender, args[1]);

        if (clanLeader.second.equals(kicked)) {
            LanguageManager.send(sender, LanguageManager.CANNOT_KICK_YOURSELF);
            return;
        }

        if (canKick(sender, clanLeader.second, kicked, clanLeader.first)) {
            kick(sender, kicked, clanLeader.first);
        }
    }

    private void kick(CommandSender sender, ClanPlayer player, Clan clan) {
        clan.remove(player);
        player.leaveClan();
        clanRepository.update(clan);
        playerRepository.save(player);

        LanguageManager.broadcast(sender.getServer(), LanguageManager.PLAYER_KICKED, Finder.nameFromId(player.id()),
                clan.tag().value());
    }

    private static boolean canKick(CommandSender sender, ClanPlayer kicker, ClanPlayer kicked, Clan clan) {
        if (clan.isLeader(kicked)) {
            if (clan.isOwner(kicker)) {
                return true;
            } else {
                LanguageManager.send(sender, LanguageManager.ONLY_OWNER_KICK_LEADER);
                return false;
            }
        } else if (clan.isMember(kicked)) {
            return true;
        } else if (kicker == kicked) {
            LanguageManager.send(sender, LanguageManager.CANNOT_KICK_YOURSELF);
            return false;
        } else {
            LanguageManager.send(sender, LanguageManager.NOT_IN_YOUR_CLAN);
            return false;
        }
    }
}
