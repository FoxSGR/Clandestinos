package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.config.ConfigManager;
import foxsgr.clandestinos.application.config.I18n;
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

public class ModTagCommand implements SubCommand {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final ConfigManager configManager = ConfigManager.getInstance();

    @Override
    public void run(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanOwner = CommandValidator.validateClanOwner(sender, args, 2,
                I18n.WRONG_MODTAG_USAGE);
        if (clanOwner == null) {
            return;
        }

        String newTagValue = args[1];
        if (!TextUtil.containsColorCodes(newTagValue)) {
            newTagValue = configManager.getString(ConfigManager.DEFAULT_TAG_COLOR) + newTagValue;
        }

        Clan clan = clanOwner.first;
        String oldTag = clanOwner.first.tag().value();
        try {
            clan.changeTag(newTagValue);
            clanRepository.update(clan);

            String newTag = clan.tag().value();
            I18n.broadcast(sender.getServer(), I18n.NEW_TAG_INFO, oldTag, newTag);
        } catch (ChangeMoreThanColorsException e) {
            I18n.send(sender, I18n.ONLY_CHANGE_COLORS);
        } catch (NonLetterInTagException e) {
            I18n.send(sender, I18n.ONLY_LETTERS_TAG);
        } catch (ChangeToSameTagException e) {
            I18n.send(sender, I18n.CHANGE_SAME_TAG, clan.tag().value());
        }
    }
}
