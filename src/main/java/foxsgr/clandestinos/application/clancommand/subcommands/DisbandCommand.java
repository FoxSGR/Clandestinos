package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.config.I18n;
import foxsgr.clandestinos.application.PermissionsManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.*;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class DisbandCommand implements SubCommand {

    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();
    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();
    private final NeutralityRequestRepository neutralityRequestRepository = PersistenceContext.repositories()
            .neutralityRequests();

    @Override
    public void run(CommandSender sender, String[] args) {
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

        I18n.broadcast(sender.getServer(), I18n.CLAN_DISBANDED, clanOwner.first.tag());
    }
}
