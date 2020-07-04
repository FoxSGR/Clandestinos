package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.config.I18n;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class KickCommand implements SubCommand {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();

    @Override
    public void run(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanLeader = CommandValidator.validateClanLeader(sender, args, 2,
                I18n.WRONG_KICK_USAGE);
        if (clanLeader == null) {
            return;
        }

        ClanPlayer kicked = Finder.playerByName(sender, args[1]);

        if (clanLeader.second.equals(kicked)) {
            I18n.send(sender, I18n.CANNOT_KICK_YOURSELF);
            return;
        }

        if (canKick(sender, clanLeader.second, kicked, clanLeader.first)) {
            kick(sender, kicked, clanLeader.first);
        }
    }

    private void kick(CommandSender sender, ClanPlayer player, Clan clan) {
        clan.kick(player);
        player.leaveClan();
        clanRepository.update(clan);
        playerRepository.save(player);

        I18n.broadcast(sender.getServer(), I18n.PLAYER_KICKED, Finder.nameFromId(player.id()),
                clan.tag().value());
    }

    private static boolean canKick(CommandSender sender, ClanPlayer kicker, ClanPlayer kicked, Clan clan) {
        if (kicked == null) {
            return false;
        }

        if (clan.isLeader(kicked)) {
            if (clan.isOwner(kicker)) {
                return true;
            } else {
                I18n.send(sender, I18n.ONLY_OWNER_KICK_LEADER);
                return false;
            }
        } else if (clan.isMember(kicked)) {
            return true;
        } else if (kicker == kicked) {
            I18n.send(sender, I18n.CANNOT_KICK_YOURSELF);
            return false;
        } else {
            I18n.send(sender, I18n.NOT_IN_YOUR_CLAN);
            return false;
        }
    }
}
