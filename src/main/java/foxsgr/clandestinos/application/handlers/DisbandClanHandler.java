package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.command.CommandSender;

public class DisbandClanHandler {

    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();
    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();
    private final LanguageManager languageManager = LanguageManager.getInstance();

    public void disbandClan(CommandSender sender) {
        ClanPlayer owner = Finder.fromSenderInClan(sender);
        if (owner == null) {
            return;
        }

        Clan clan = clanRepository.findByTag(owner.clan().withoutColor().value());
        if (!clan.isOwner(owner)) {
            sender.sendMessage(languageManager.get(LanguageManager.MUST_BE_OWNER));
            return;
        }

        playerRepository.leaveFromClan(clan);
        clanRepository.remove(clan);
        inviteRepository.removeAllFrom(clan);

        String message = languageManager.get(LanguageManager.CLAN_DISBANDED)
                .replace(LanguageManager.placeholder(0), clan.tag().value());
        sender.getServer().broadcastMessage(TextUtil.translateColoredText(message));
    }
}
