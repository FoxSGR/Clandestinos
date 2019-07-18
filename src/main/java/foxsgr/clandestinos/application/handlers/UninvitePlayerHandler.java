package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class UninvitePlayerHandler {

    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();

    public void uninvitePlayer(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanLeader = CommandValidator.validateClanLeader(sender, args, 2,
                LanguageManager.WRONG_UNINVITE_USAGE);
        if (clanLeader == null) {
            return;
        }

        Invite invite = inviteRepository.find(args[1], clanLeader.second.clan().withoutColor().value());
        if (invite == null) {
            // send no invite pending
            return;
        }

        inviteRepository.remove(invite);
    }
}
