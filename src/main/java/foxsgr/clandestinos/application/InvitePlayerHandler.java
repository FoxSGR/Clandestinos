package foxsgr.clandestinos.application;

import foxsgr.clandestinos.domain.model.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class InvitePlayerHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final ClanPlayerRepository clanPlayerRepository = PersistenceContext.repositories().players();

    void invitePlayer(CommandSender sender, String[] args) {
        if (!PlayerCommandValidator.validate(sender, args, 2, LanguageManager.WRONG_INVITE_USAGE)) {
            return;
        }

        Player inviter = (Player) sender;
        ClanPlayer inviterClanPlayer = ClanPlayerFinder.find(inviter);
        if (!inviterClanPlayer.inClan()) {
            sender.sendMessage(LanguageManager.get(LanguageManager.MUST_BE_IN_CLAN));
            return;
        }

        Player invited = Bukkit.getPlayerExact(args[1]);
        if (Bukkit.getPlayerExact(args[1]) == null) {
            inviter.sendMessage(LanguageManager.get(LanguageManager.PLAYER_NOT_ONLINE));
            return;
        }

        ClanPlayer invitedClanPlayer = ClanPlayerFinder.find(invited);
        inviteAndSave(inviter, inviterClanPlayer, invited, invitedClanPlayer, args);
    }

    private void inviteAndSave(Player inviter, ClanPlayer inviterClanPlayer, Player invited, ClanPlayer invitedClanPlayer, String[] args) {

    }
}
