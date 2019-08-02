package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.config.LanguageManager;
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
        Pair<Clan, ClanPlayer> owner = CommandValidator.validateClanOwner(sender, args, 2, LanguageManager.WRONG_MAKE_LEADER_USAGE);

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
            LanguageManager.send(sender, LanguageManager.NOT_IN_YOUR_CLAN);
            return false;
        } else if (clan.isOwner(player)) {
            LanguageManager.send(sender, LanguageManager.CANNOT_PROMOTE_YOURSELF);
            return false;
        } else if (clan.isLeader(player)) {
            LanguageManager.send(sender, LanguageManager.CANNOT_PROMOTE_LEADER);
            return false;
        }
        return true;
    }

    private void promote(CommandSender sender, ClanPlayer player, Clan clan, String name) {
        clan.makeLeader(player);
        clanRepository.update(clan);

        LanguageManager.send(sender, LanguageManager.SUCCESSFUL_PROMOTE, name);

        Player leader = Bukkit.getPlayer(player.id());
        if (leader != null) {
            LanguageManager.send(leader, LanguageManager.PROMOTED);
        }

    }
}