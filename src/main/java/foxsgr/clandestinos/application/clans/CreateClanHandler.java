package foxsgr.clandestinos.application;

import foxsgr.clandestinos.domain.exceptions.NonLetterInTagException;
import foxsgr.clandestinos.domain.exceptions.WrongNameSizeException;
import foxsgr.clandestinos.domain.exceptions.WrongTagSizeException;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CreateClanHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final ClanPlayerRepository clanPlayerRepository = PersistenceContext.repositories().players();

    void createClan(CommandSender sender, String[] args) {
        if (!validate(sender, args)) {
            return;
        }

        Player player = (Player) sender;
        ClanPlayer clanPlayer = ClanPlayerFinder.find(player);
        if (clanPlayer.inClan()) {
            player.sendMessage(LanguageManager.get(LanguageManager.CANNOT_IN_CLAN));
            return;
        }

        try {
            createAndSaveClan(player, clanPlayer, args);
        } catch (WrongTagSizeException e) {
            player.sendMessage(LanguageManager.get(LanguageManager.WRONG_SIZE_TAG));
        } catch (WrongNameSizeException e) {
            player.sendMessage(LanguageManager.get(LanguageManager.WRONG_SIZE_NAME));
        } catch (NonLetterInTagException e) {
            player.sendMessage(LanguageManager.get(LanguageManager.ONLY_LETTERS_TAG));
        }
    }

    private void createAndSaveClan(Player player, ClanPlayer clanPlayer, String[] args) {
        String clanName = clanNameFromArgs(args);
        if (clanRepository.findByTag(args[1]) != null) {
            player.sendMessage(LanguageManager.get(LanguageManager.TAG_ALREADY_EXISTS));
            return;
        }

        Clan clan = new Clan(args[1], clanName, clanPlayer);
        clan = clanRepository.save(clan);

        clanPlayer.joinClan(clan);
        clanPlayerRepository.save(clanPlayer);

        String message = LanguageManager.get(LanguageManager.CLAN_CREATED).replace("{0}", args[1]);
        player.getServer().broadcastMessage(message);
    }

    private static String clanNameFromArgs(String[] args) {
        if (args.length > 2) {
            String[] clanNameParts = new String[args.length - 2]; // Without 'create' and the tag positions (minus 2)
            System.arraycopy(args, 2, clanNameParts, 0, args.length - 2);
            return String.join(" ", clanNameParts);
        } else {
            return "";
        }
    }

    private static boolean validate(CommandSender sender, String[] args) {
        if (!PlayerCommandValidator.validate(sender, args, 2, LanguageManager.WRONG_CREATE_USAGE)) {
            return false;
        }

        if (ConfigManager.getStringList(ConfigManager.FORBIDDEN_TAGS).contains(args[1])) {
            sender.sendMessage(LanguageManager.get(LanguageManager.FORBIDDEN_TAG));
            return false;
        }

        return true;
    }
}
