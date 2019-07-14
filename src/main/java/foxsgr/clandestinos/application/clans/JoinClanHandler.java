package foxsgr.clandestinos.application.clans;

import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class JoinClanHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final ClanPlayerRepository clanPlayerRepository = PersistenceContext.repositories().players();
    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();
    private final LanguageManager languageManager = LanguageManager.getInstance();

    void joinClan(CommandSender sender, String[] args) {
        if (!PlayerCommandValidator.validate(sender, args, 2, LanguageManager.WRONG_JOIN_USAGE)) {
            return;
        }

        Clan clan = clanRepository.findByTag(args[1]);
        if (clan == null) {
            sender.sendMessage(languageManager.get(LanguageManager.CLAN_DOESNT_EXIST));
            return;
        }

        Player player = (Player) sender;
        String id = ClanPlayerFinder.idFromPlayer(player);
        Invite invite = inviteRepository.find(id, args[1]);
        if (invite == null) {
            sender.sendMessage(languageManager.get(LanguageManager.NOT_INVITED));
            return;
        }

        joinClan(player, clan);
        inviteRepository.remove(invite);

        inform(player, clan);
    }

    private void joinClan(Player player, Clan clan) {
        ClanPlayer clanPlayer = ClanPlayerFinder.get(player);
        clan.addMember(clanPlayer);
        clanRepository.update(clan);

        clanPlayer.joinClan(clan);
        clanPlayerRepository.save(clanPlayer);
    }

    private void inform(Player player, Clan clan) {
        String message = languageManager.get(LanguageManager.JOINED_MESSAGE)
                .replace(LanguageManager.placeholder(0), player.getDisplayName())
                .replace(LanguageManager.placeholder(1), clan.tag().value());
        player.getServer().broadcastMessage(message);
    }
}
