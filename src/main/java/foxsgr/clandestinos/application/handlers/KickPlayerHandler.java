package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.application.PlayerCommandValidator;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import org.bukkit.command.CommandSender;

public class KickPlayerHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();
    private final LanguageManager languageManager = LanguageManager.getInstance();

    public void kickPlayer(CommandSender sender, String[] args) {
        if (!PlayerCommandValidator.validate(sender, args, 2, LanguageManager.WRONG_KICK_USAGE)) {
            return;
        }

        ClanPlayer kicker = Finder.fromSenderInClan(sender);
        if (kicker == null) {
            return;
        }

        Clan clan = Finder.clanFromPlayer(sender, kicker);
        if (!clan.isLeader(kicker)) {
            LanguageManager.send(sender, LanguageManager.MUST_BE_LEADER);
            return;
        }

        ClanPlayer kicked = Finder.playerByName(sender, args[1]);
        if (kicked == null) {
            LanguageManager.send(sender, LanguageManager.NOT_IN_YOUR_CLAN);
            return;
        }

        if (clan.isLeader(kicked)) {
            if (clan.isOwner(kicker)) {
                // TODO: kick
            } else {
                // TODO: send 'only the owner can kick a leader' message
            }
        } else if (clan.isMember(kicked)) {
            // TODO: kick
        } else {
            LanguageManager.send(sender, LanguageManager.NOT_IN_YOUR_CLAN);
        }
    }
}
