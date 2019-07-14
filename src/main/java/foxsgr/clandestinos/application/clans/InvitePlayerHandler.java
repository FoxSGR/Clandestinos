package foxsgr.clandestinos.application;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.invite.Invite;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class InvitePlayerHandler {

    private final InviteRepository inviteRepository = PersistenceContext.repositories().invites();

    void invitePlayer(CommandSender sender, String[] args) {
        if (!PlayerCommandValidator.validate(sender, args, 2, LanguageManager.WRONG_INVITE_USAGE)) {
            return;
        }

        Player inviter = (Player) sender;
        ClanPlayer inviterClanPlayer = ClanPlayerFinder.find(inviter);
        if (!canInvite(sender, inviterClanPlayer)) {
            return;
        }

        Player invited = Bukkit.getPlayerExact(args[1]);
        if (invited == null) {
            inviter.sendMessage(LanguageManager.get(LanguageManager.PLAYER_NOT_ONLINE));
            return;
        }

        ClanPlayer invitedClanPlayer = ClanPlayerFinder.find(invited);
        inviteAndSave(inviter, inviterClanPlayer, invited, invitedClanPlayer);
    }

    private void inviteAndSave(Player inviter, ClanPlayer inviterClanPlayer, Player invited, ClanPlayer invitedClanPlayer) {
        Clan clan = inviterClanPlayer.clan();
        if (inviteRepository.findPending(invitedClanPlayer.id(), clan.tag().value()) != null) {
            inviter.sendMessage(LanguageManager.get(LanguageManager.ALREADY_INVITED));
            return;
        }

        Invite invite = new Invite(clan, invitedClanPlayer);
        inviteRepository.save(invite);

        String inviterMessage = LanguageManager.get(LanguageManager.PLAYER_INVITED)
                .replace(LanguageManager.placeholder(0), invited.getName());
        inviter.sendMessage(inviterMessage);

        ClanTag clanTag = clan.tag();
        String invitedMessage = LanguageManager.get(LanguageManager.RECEIVED_INVITE)
                .replace(LanguageManager.placeholder(0), clanTag.value())
                .replace(LanguageManager.placeholder(1), clanTag.withoutColor().value());
        invited.sendMessage(invitedMessage);
    }

    private static boolean canInvite(CommandSender sender, ClanPlayer inviterClanPlayer) {
        if (!inviterClanPlayer.inClan()) {
            sender.sendMessage(LanguageManager.get(LanguageManager.MUST_BE_IN_CLAN));
            return false;
        }

        if (inviterClanPlayer.clan().isLeader(inviterClanPlayer)) {
            sender.sendMessage(LanguageManager.get(LanguageManager.MUST_BE_LEADER));
            return false;
        }

        return true;
    }
}
