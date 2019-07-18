package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
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

public class InvitePlayerHandler {

    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();
    private final LanguageManager languageManager = LanguageManager.getInstance();

    public void invitePlayer(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanLeader = CommandValidator.validateClanLeader(sender, args, 2,
                LanguageManager.WRONG_INVITE_USAGE);
        if (clanLeader == null) {
            return;
        }

        Player invited = Bukkit.getPlayerExact(args[1]);
        if (invited == null) {
            sender.sendMessage(languageManager.get(LanguageManager.PLAYER_NOT_ONLINE));
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

        String inviterMessage = languageManager.get(LanguageManager.PLAYER_INVITED)
                .replace(LanguageManager.placeholder(0), invited.getName());
        sender.sendMessage(inviterMessage);

        ClanTag clanTag = clan.tag();
        String invitedMessage = languageManager.get(LanguageManager.RECEIVED_INVITE)
                .replace(LanguageManager.placeholder(0), clanTag.value())
                .replace(LanguageManager.placeholder(1), clanTag.withoutColor().value());
        invited.sendMessage(invitedMessage);
    }

    private ClanPlayer canBeInvited(CommandSender sender, Player invited, Clan clan) {
        ClanPlayer invitedClanPlayer = Finder.findPlayer(invited);
        if (invitedClanPlayer == null) {
            return new ClanPlayer(Finder.idFromPlayer(invited));
        }

        if (!invitedClanPlayer.inClan()) {
            return invitedClanPlayer;
        }

        if (invitedClanPlayer.clan().equalsIgnoreColor(clan.tag())) {
            sender.sendMessage(languageManager.get(LanguageManager.ALREADY_IN_YOUR_CLAN));
            return null;
        }

        if (inviteRepository.find(invitedClanPlayer.id(), clan.tag().withoutColor().value()) != null) {
            sender.sendMessage(languageManager.get(LanguageManager.ALREADY_INVITED));
            return null;
        }

        return invitedClanPlayer;
    }
}
