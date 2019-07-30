package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.application.PermissionsManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.*;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class DisbandHandler {

    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();
    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();
    private final NeutralityRequestRepository neutralityRequestRepository = PersistenceContext.repositories()
            .neutralityRequests();

    public void disbandClan(CommandSender sender, String[] args) {
        if (!PermissionsManager.hasAndWarn(sender, args[0])) {
            return;
        }

        Pair<Clan, ClanPlayer> clanOwner = Finder.findClanOwner(sender);
        if (clanOwner == null) {
            return;
        }

        playerRepository.leaveFromClan(clanOwner.first);
        clanRepository.remove(clanOwner.first);
        inviteRepository.removeAllFrom(clanOwner.first);
        neutralityRequestRepository.removeAllFrom(clanOwner.first);

        LanguageManager.broadcast(sender.getServer(), LanguageManager.CLAN_DISBANDED, clanOwner.first.tag());
    }
}
