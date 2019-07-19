package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.exceptions.ChangeMoreThanColorsException;
import foxsgr.clandestinos.domain.exceptions.NonLetterInTagException;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class ModifyTagHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();

    public void modifyTag(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanOwner = CommandValidator.validateClanOwner(sender, args, 2,
                LanguageManager.WRONG_MODTAG_USAGE);
        if (clanOwner == null) {
            return;
        }

        String oldTag = clanOwner.first.tag().value();
        try {
            clanOwner.first.changeTag(args[1]);
            updateMembers(clanOwner.first);
            clanRepository.update(clanOwner.first);

            String newTag = clanOwner.first.tag().value();
            LanguageManager.broadcast(sender.getServer(), LanguageManager.NEW_TAG_INFO, oldTag, newTag);
        } catch (ChangeMoreThanColorsException e) {
            LanguageManager.send(sender, LanguageManager.ONLY_CHANGE_COLORS);
        } catch (NonLetterInTagException e) {
            LanguageManager.send(sender, LanguageManager.ONLY_LETTERS_TAG);
        }
    }

    private void updateMembers(Clan clan) {
        for (String id : clan.allPlayers()) {
            ClanPlayer player = playerRepository.find(id);
            player.updateClanTag(clan.tag());
            playerRepository.save(player);
        }
    }
}
