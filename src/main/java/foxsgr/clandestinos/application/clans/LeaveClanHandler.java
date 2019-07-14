package foxsgr.clandestinos.application.clans;

import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.command.CommandSender;

class LeaveClanHandler {

    private final ClanPlayerRepository clanPlayerRepository = PersistenceContext.repositories().players();
    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final LanguageManager languageManager = LanguageManager.getInstance();

    void leaveClan(CommandSender sender) {
        ClanPlayer clanPlayer = ClanPlayerFinder.fromSenderInClan(sender);
        if (clanPlayer == null) {
            return;
        }

        Clan clan = clanRepository.findByTag(clanPlayer.clan().withoutColor().value());
        if (clan.isOwner(clanPlayer.id())) {
            sender.sendMessage(languageManager.get(LanguageManager.OWNER_CANT_LEAVE));
            return;
        }

        clan.remove(clanPlayer);
        clanRepository.update(clan);

        clanPlayer.leaveClan();
        clanPlayerRepository.save(clanPlayer);
    }
}
