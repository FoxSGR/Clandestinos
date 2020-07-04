package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.config.I18n;
import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements SubCommand {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();
    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();

    @Override
    public void run(CommandSender sender, String[] args) {
        if (!CommandValidator.validate(sender, args, 2, I18n.WRONG_JOIN_USAGE)) {
            return;
        }

        Clan clan = Finder.clanByTag(sender, args[1]);
        if (clan == null) {
            return;
        }

        Player player = (Player) sender;
        String id = Finder.idFromPlayer(player);
        ClanPlayer clanPlayer = playerRepository.find(id);
        if (clanPlayer != null && clanPlayer.inClan()) {
            I18n.send(sender, I18n.CANNOT_IN_CLAN);
            return;
        }

        Invite invite = inviteRepository.find(id, args[1]);
        if (invite == null) {
            I18n.send(sender, I18n.NOT_INVITED);
            return;
        }

        joinClan(player, clan);
        inviteRepository.remove(invite);

        I18n.broadcast(sender.getServer(), I18n.JOINED_MESSAGE, player.getDisplayName(),
                clan.tag().value());
    }

    private void joinClan(Player player, Clan clan) {
        ClanPlayer clanPlayer = Finder.getPlayer(player);
        clan.addMember(clanPlayer);
        clanRepository.update(clan);

        clanPlayer.joinClan(clan);
        playerRepository.save(clanPlayer);
    }
}
