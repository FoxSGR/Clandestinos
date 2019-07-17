package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.application.PlayerCommandValidator;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.TextUtil;
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

        String id = Finder.idFromName(args[1]);
        ClanPlayer kicked = playerRepository.find(id);
        if (kicked == null) {
            LanguageManager.send(sender, LanguageManager.NOT_IN_YOUR_CLAN);
            return;
        }

        if (kicker.equals(kicked)) {
            LanguageManager.send(sender, LanguageManager.CANNOT_KICK_YOURSELF);
            return;
        }

        if (clan.isLeader(kicked)) {
            if (clan.isOwner(kicker)) {
                kick(sender, kicked, clan);
            } else {
                LanguageManager.send(sender, LanguageManager.ONLY_OWNER_KICK_LEADER);
            }
        } else if (clan.isMember(kicked)) {
            kick(sender, kicked, clan);
        } else {
            LanguageManager.send(sender, LanguageManager.NOT_IN_YOUR_CLAN);
        }
    }

    private void kick(CommandSender sender, ClanPlayer player, Clan clan) {
        clan.remove(player);
        player.leaveClan();
        clanRepository.update(clan);
        playerRepository.save(player);

        String message = languageManager.get(LanguageManager.PLAYER_KICKED)
                .replace(LanguageManager.placeholder(0), Finder.nameFromId(player.id()))
                .replace(LanguageManager.placeholder(1), clan.tag().value());
        sender.getServer().broadcastMessage(TextUtil.translateColoredText(message));
    }
}
