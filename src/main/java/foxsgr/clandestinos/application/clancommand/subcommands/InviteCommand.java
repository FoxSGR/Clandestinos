package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.config.LanguageManager;
import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class InviteCommand implements SubCommand {

    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();

    @Override
    public void run(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanLeader = CommandValidator.validateClanLeader(sender, args, 2,
                LanguageManager.WRONG_INVITE_USAGE);
        if (clanLeader == null) {
            return;
        }

        Player invited = Bukkit.getPlayerExact(args[1]);
        if (invited == null) {
            LanguageManager.send(sender, LanguageManager.PLAYER_NOT_ONLINE);
            return;
        }

        inviteAndSave(sender, invited, clanLeader.first);
    }

    private void inviteAndSave(CommandSender sender, Player invited, Clan clan) {
        ClanPlayer invitedClanPlayer = canBeInvited(sender, invited, clan);
        if (invitedClanPlayer == null) {
            return;
        }

        Invite invite = new Invite(clan, invitedClanPlayer);
        inviteRepository.add(invite);

        LanguageManager.send(sender, LanguageManager.PLAYER_INVITED, invited.getName());

        ClanTag clanTag = clan.tag();
        LanguageManager.send(invited, LanguageManager.RECEIVED_INVITE, clanTag.value(), clanTag.withoutColor().value());
    }

    private ClanPlayer canBeInvited(CommandSender sender, Player invited, Clan clan) {
        ClanPlayer invitedClanPlayer = Finder.findPlayer(invited);
        if (invitedClanPlayer == null) {
            return new ClanPlayer(Finder.idFromPlayer(invited));
        }

        Optional<ClanTag> clanTag = invitedClanPlayer.clan();
        if (!clanTag.isPresent()) {
            return invitedClanPlayer;
        }

        if (clanTag.get().equalsIgnoreColor(clan.tag())) {
            LanguageManager.send(sender, LanguageManager.ALREADY_IN_YOUR_CLAN);
            return null;
        }

        if (inviteRepository.find(invitedClanPlayer.id(), clan.simpleTag()) != null) {
            LanguageManager.send(sender, LanguageManager.ALREADY_INVITED);
            return null;
        }

        return invitedClanPlayer;
    }
}
