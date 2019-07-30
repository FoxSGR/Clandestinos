package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.ConfigManager;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.exceptions.ChangeMoreThanColorsException;
import foxsgr.clandestinos.domain.exceptions.ChangeToSameTagException;
import foxsgr.clandestinos.domain.exceptions.NonLetterInTagException;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Pair;
import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.command.CommandSender;

public class ModTagHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final ConfigManager configManager = ConfigManager.getInstance();

    public void modifyTag(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanOwner = CommandValidator.validateClanOwner(sender, args, 2,
                LanguageManager.WRONG_MODTAG_USAGE);
        if (clanOwner == null) {
            return;
        }

        String newTagValue = args[1];
        if (!TextUtil.containsColorCodes(newTagValue)) {
            newTagValue = configManager.getString(ConfigManager.DEFAULT_TAG_COLOR) + newTagValue;
        }

        String oldTag = clanOwner.first.tag().value();
        try {
            clanOwner.first.changeTag(newTagValue);
            clanRepository.update(clanOwner.first);

            String newTag = clanOwner.first.tag().value();
            LanguageManager.broadcast(sender.getServer(), LanguageManager.NEW_TAG_INFO, oldTag, newTag);
        } catch (ChangeMoreThanColorsException e) {
            LanguageManager.send(sender, LanguageManager.ONLY_CHANGE_COLORS);
        } catch (NonLetterInTagException e) {
            LanguageManager.send(sender, LanguageManager.ONLY_LETTERS_TAG);
        } catch (ChangeToSameTagException e) {
            LanguageManager.send(sender, LanguageManager.CHANGE_SAME_TAG, clanOwner.first.tag().value());
        }
    }
}
