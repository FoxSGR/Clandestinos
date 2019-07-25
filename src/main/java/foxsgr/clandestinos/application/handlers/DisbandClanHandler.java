package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class DisbandClanHandler {

    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();
    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();

    public void disbandClan(CommandSender sender) {
        Pair<Clan, ClanPlayer> clanOwner = Finder.findClanOwner(sender);
        if (clanOwner == null) {
            return;
        }

        playerRepository.leaveFromClan(clanOwner.first);
        clanRepository.remove(clanOwner.first);
        inviteRepository.removeAllFrom(clanOwner.first);

        LanguageManager.broadcast(sender.getServer(), LanguageManager.CLAN_DISBANDED, clanOwner.first.tag());
    }
}
