package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.application.PermissionsManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveHandler {

    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();
    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();

    public void leaveClan(CommandSender sender, String[] args) {
        if (!PermissionsManager.hasAndWarn(sender, args[0])) {
            return;
        }

        Pair<Clan, ClanPlayer> clanPlayer = Finder.fromSenderInClan(sender);
        if (clanPlayer == null) {
            return;
        }

        Clan clan = clanPlayer.first;
        ClanPlayer player = clanPlayer.second;
        if (clan.isOwner(player)) {
            LanguageManager.send(sender, LanguageManager.OWNER_CANT_LEAVE);
            return;
        }

        clan.remove(player);
        clanRepository.update(clan);

        player.leaveClan();
        playerRepository.save(player);

        Player p = (Player) sender;
        LanguageManager.broadcast(sender.getServer(), LanguageManager.LEFT_CLAN, p.getDisplayName(),
                clan.tag().value());
    }
}
