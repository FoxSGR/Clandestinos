package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.config.I18n;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class ClanFFCommand implements SubCommand {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();

    @Override
    public void run(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanLeader = Finder.findClanLeader(sender);
        if (clanLeader == null) {
            return;
        }

        Clan clan = clanLeader.first;
        boolean newState = clan.toggleFriendlyFire();
        clanRepository.update(clan);

        if (newState) {
            I18n.send(sender, I18n.CLAN_FF_ENABLED);
        } else {
            I18n.send(sender, I18n.CLAN_FF_DISABLED);
        }
    }
}
