package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.config.I18n;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class FFCommand implements SubCommand {

    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();

    @Override
    public void run(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanPlayer = Finder.fromSenderInClan(sender);
        if (clanPlayer == null) {
            return;
        }

        ClanPlayer player = clanPlayer.second;
        boolean newState = player.toggleFriendlyFire();
        playerRepository.save(player);

        if (newState) {
            I18n.send(sender, I18n.FF_ENABLED);
        } else {
            I18n.send(sender, I18n.FF_DISABLED);
        }
    }
}
