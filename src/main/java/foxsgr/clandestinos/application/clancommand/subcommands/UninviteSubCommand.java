package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.config.LanguageManager;
import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UninviteSubCommand implements SubCommand {

    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();

    @Override
    public void run(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanLeader = CommandValidator.validateClanLeader(sender, args, 2,
                LanguageManager.WRONG_UNINVITE_USAGE);
        if (clanLeader == null) {
            return;
        }

        Invite invite = clanLeader.second.clan()
                .map(tag -> inviteRepository.find(args[1], tag.withoutColor().value())).orElse(null);
        if (invite == null) {
            LanguageManager.send(sender, LanguageManager.NO_INVITE_PENDING);
            return;
        }

        inviteRepository.remove(invite);
        LanguageManager.send(sender, LanguageManager.PLAYER_UNINVITED, args[1]);

        Player uninvited = Bukkit.getPlayerExact(args[1]);
        if (uninvited != null) {
            LanguageManager.send(uninvited, LanguageManager.YOU_WERE_UNINVITED, clanLeader.first.tag().value());
        }
    }
}