package foxsgr.clandestinos.application;

import foxsgr.clandestinos.domain.exceptions.NonLetterInTagException;
import foxsgr.clandestinos.domain.exceptions.WrongNameSizeException;
import foxsgr.clandestinos.domain.exceptions.WrongTagSizeException;
import foxsgr.clandestinos.domain.model.Clan;
import foxsgr.clandestinos.domain.model.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateClanHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final ClanPlayerRepository clanPlayerRepository = PersistenceContext.repositories().players();

    public void createClan(CommandSender sender, String[] args) {
        if (!PermissionsManager.hasPermissionSubCommandWarn(sender, args[0]) || !validate(sender, args)) {
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
        Clan clan = new Clan(args[1], clanName, clanPlayer.id());
        if (clanRepository.save(clan)) {
            clanPlayer.changeClan(args[1]);
            clanPlayerRepository.save(clanPlayer);

            String message = LanguageManager.get(LanguageManager.CLAN_CREATED).replace("{0}", args[1]);
            player.getServer().broadcastMessage(message);
        } else {
            player.sendMessage(LanguageManager.get(LanguageManager.TAG_ALREADY_EXISTS));
        }
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
        if (!(sender instanceof Player)) {
            sender.sendMessage(LanguageManager.get(LanguageManager.MUST_BE_PLAYER));
            return false;
        }

        if (args.length < 2) { // Must be at least 2: "create" and clan tag (name is optional and can be multiple words)
            sender.sendMessage(LanguageManager.get(LanguageManager.WRONG_CREATE_USAGE));
            return false;
        }

        if (ConfigManager.getStringList(ConfigManager.FORBIDDEN_TAGS).contains(args[1])) {
            sender.sendMessage(LanguageManager.get(LanguageManager.FORBIDDEN_TAG));
            return false;
        }

        return true;
    }
}
