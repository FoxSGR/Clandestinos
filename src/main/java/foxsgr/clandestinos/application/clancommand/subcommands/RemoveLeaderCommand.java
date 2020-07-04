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

public class RemoveLeaderCommand implements SubCommand {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();

    @Override
    public void run(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanOwner = CommandValidator.validateClanOwner(sender, args, 2, I18n.WRONG_DEMOTE_LEADER_USAGE);

        if (clanOwner == null) {
            return;
        }

        ClanPlayer player = Finder.playerByName(sender, args[1]);
        if (player == null) {
            return;
        }

        if (canDemote(sender, player, clanOwner.first)) {
            demote(sender, player, clanOwner.first, args[1]);
        }
    }

    private boolean canDemote(CommandSender sender, ClanPlayer player, Clan clan) {
        if (!clan.allPlayers().contains(player.id())) {
            I18n.send(sender, I18n.NOT_IN_YOUR_CLAN);
        } else if (clan.isOwner(player)) {
            I18n.send(sender, I18n.CANNOT_DEMOTE_YOURSELF);
            return false;
        } else if (clan.isMember(player)) {
            I18n.send(sender, I18n.CANNOT_DEMOTE_MEMBER);
            return false;
        }

        return true;
    }

    private void demote(CommandSender sender, ClanPlayer player, Clan clan, String name) {
        clan.demoteLeader(player);
        clanRepository.update(clan);

        I18n.send(sender, I18n.SUCCESSFUL_DEMOTE, name);

        Player member = Bukkit.getPlayer(player.id());
        if (member != null) {
            I18n.send(member, I18n.DEMOTED);
        }
    }
}
