package foxsgr.clandestinos.application.clans;

import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.command.CommandSender;

class DisbandClanHandler {

    private final ClanPlayerRepository clanPlayerRepository = PersistenceContext.repositories().players();
    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();
    private final LanguageManager languageManager = LanguageManager.getInstance();

    void disbandClan(CommandSender sender) {
        ClanPlayer owner = ClanPlayerFinder.fromSenderInClan(sender);
        if (owner == null) {
            return;
        }

        Clan clan = clanRepository.findByTag(owner.clan().withoutColor().value());
        if (!clan.isOwner(owner.id())) {
            sender.sendMessage(languageManager.get(LanguageManager.MUST_BE_OWNER));
            return;
        }

        clanPlayerRepository.leaveFromClan(clan);
        clanRepository.remove(clan);
        inviteRepository.removeAllFrom(clan);

        String message = languageManager.get(LanguageManager.CLAN_DISBANDED)
                .replace(LanguageManager.placeholder(0), clan.tag().value());
        sender.getServer().broadcastMessage(TextUtil.translateColoredText(message));
    }
}
