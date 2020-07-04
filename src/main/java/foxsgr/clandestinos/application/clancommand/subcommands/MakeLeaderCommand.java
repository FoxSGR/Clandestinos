package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.config.I18n;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MakeLeaderCommand implements SubCommand {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();

    @Override
    public void run(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> owner = CommandValidator.validateClanOwner(sender, args, 2, I18n.WRONG_MAKE_LEADER_USAGE);

        if (owner == null) {
            return;
        }

        ClanPlayer player = Finder.playerByName(sender, args[1]);
        if (player == null) {
            return;
        }

        if (canPromote(sender, player, owner.first)) {
            promote(sender, player, owner.first, args[1]);
        }
    }

    private boolean canPromote(CommandSender sender, ClanPlayer player, Clan clan) {
        if (!clan.allPlayers().contains(player.id())) {
            I18n.send(sender, I18n.NOT_IN_YOUR_CLAN);
            return false;
        } else if (clan.isOwner(player)) {
            I18n.send(sender, I18n.CANNOT_PROMOTE_YOURSELF);
            return false;
        } else if (clan.isLeader(player)) {
            I18n.send(sender, I18n.CANNOT_PROMOTE_LEADER);
            return false;
        }
        return true;
    }

    private void promote(CommandSender sender, ClanPlayer player, Clan clan, String name) {
        clan.makeLeader(player);
        clanRepository.update(clan);

        I18n.send(sender, I18n.SUCCESSFUL_PROMOTE, name);

        Player leader = Bukkit.getPlayer(player.id());
        if (leader != null) {
            I18n.send(leader, I18n.PROMOTED);
        }

    }
}