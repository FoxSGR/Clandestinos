package foxsgr.clandestinos.application.clans;

import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        Player player = (Player) sender;
        String message = languageManager.get(LanguageManager.LEFT_CLAN)
                .replace(LanguageManager.placeholder(0), player.getDisplayName())
                .replace(LanguageManager.placeholder(1), clan.tag().value());
        sender.getServer().broadcastMessage(TextUtil.translateColoredText(message));
    }
}
