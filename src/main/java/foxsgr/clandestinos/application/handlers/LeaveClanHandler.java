package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveClanHandler {

    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();
    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();

    public void leaveClan(CommandSender sender) {
        ClanPlayer clanPlayer = Finder.fromSenderInClan(sender);
        if (clanPlayer == null) {
            return;
        }

        Clan clan = Finder.findClanEnsureExists(clanPlayer);
        if (clan.isOwner(clanPlayer)) {
            LanguageManager.send(sender, LanguageManager.OWNER_CANT_LEAVE);
            return;
        }

        clan.remove(clanPlayer);
        clanRepository.update(clan);

        clanPlayer.leaveClan();
        playerRepository.save(clanPlayer);

        Player player = (Player) sender;
        LanguageManager.broadcast(sender.getServer(), LanguageManager.LEFT_CLAN, player.getDisplayName(),
                clan.tag().value());
    }
}
