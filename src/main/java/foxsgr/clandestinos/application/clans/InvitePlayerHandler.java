package foxsgr.clandestinos.application.clans;

import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class InvitePlayerHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();
    private final LanguageManager languageManager = LanguageManager.getInstance();

    void invitePlayer(CommandSender sender, String[] args) {
        if (!PlayerCommandValidator.validate(sender, args, 2, LanguageManager.WRONG_INVITE_USAGE)) {
            return;
        }

        Player inviter = (Player) sender;
        ClanPlayer inviterClanPlayer = ClanPlayerFinder.get(inviter);
        Clan clan = canInvite(sender, inviterClanPlayer);
        if (clan == null) {
            return;
        }

        Player invited = Bukkit.getPlayerExact(args[1]);
        if (invited == null) {
            inviter.sendMessage(languageManager.get(LanguageManager.PLAYER_NOT_ONLINE));
            return;
        }

        inviteAndSave(inviter, invited, clan);
    }

    private void inviteAndSave(Player inviter, Player invited, Clan clan) {
        ClanPlayer invitedClanPlayer = canBeInvited(inviter, invited, clan);
        if (invitedClanPlayer == null) {
            return;
        }

        Invite invite = new Invite(clan, invitedClanPlayer);
        inviteRepository.add(invite);

        String inviterMessage = languageManager.get(LanguageManager.PLAYER_INVITED)
                .replace(LanguageManager.placeholder(0), invited.getName());
        inviter.sendMessage(inviterMessage);

        ClanTag clanTag = clan.tag();
        String invitedMessage = languageManager.get(LanguageManager.RECEIVED_INVITE)
                .replace(LanguageManager.placeholder(0), clanTag.value())
                .replace(LanguageManager.placeholder(1), clanTag.withoutColor().value());
        invited.sendMessage(invitedMessage);
    }

    private Clan canInvite(CommandSender sender, ClanPlayer inviterClanPlayer) {
        if (!inviterClanPlayer.inClan()) {
            sender.sendMessage(languageManager.get(LanguageManager.MUST_BE_IN_CLAN));
            return null;
        }

        Clan clan = clanRepository.findByTag(inviterClanPlayer.clan().withoutColor().value());
        if (!clan.isLeader(inviterClanPlayer)) {
            sender.sendMessage(languageManager.get(LanguageManager.MUST_BE_LEADER));
            return null;
        }

        return clan;
    }

    private ClanPlayer canBeInvited(CommandSender inviter, Player invited, Clan clan) {
        ClanPlayer invitedClanPlayer = ClanPlayerFinder.get(invited);
        if (!invitedClanPlayer.inClan()) {
            return invitedClanPlayer;
        }

        if (invitedClanPlayer.clan().equalsIgnoreColor(clan.tag())) {
            inviter.sendMessage(languageManager.get(LanguageManager.ALREADY_IN_YOUR_CLAN));
            return null;
        }

        if (inviteRepository.find(invitedClanPlayer.id(), clan.tag().withoutColor().value()) != null) {
            inviter.sendMessage(languageManager.get(LanguageManager.ALREADY_INVITED));
            return null;
        }

        return invitedClanPlayer;
    }
}
