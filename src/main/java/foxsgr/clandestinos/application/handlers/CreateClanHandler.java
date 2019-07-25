package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.*;
import foxsgr.clandestinos.domain.exceptions.NonLetterInTagException;
import foxsgr.clandestinos.domain.exceptions.WrongNameSizeException;
import foxsgr.clandestinos.domain.exceptions.WrongTagSizeException;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateClanHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();
    private final ConfigManager configManager = ConfigManager.getInstance();
    private final EconomyManager economyManager = EconomyManager.getInstance();

    public void createClan(CommandSender sender, String[] args) {
        if (!validate(sender, args)) {
            return;
        }

        Player player = (Player) sender;
        ClanPlayer clanPlayer = canCreate(player);
        if (clanPlayer == null) {
            return;
        }

        try {
            createAndSaveClan(player, clanPlayer, args);
        } catch (WrongTagSizeException e) {
            LanguageManager.send(player, LanguageManager.WRONG_SIZE_TAG);
        } catch (WrongNameSizeException e) {
            LanguageManager.send(player, LanguageManager.WRONG_SIZE_NAME);
        } catch (NonLetterInTagException e) {
            LanguageManager.send(player, LanguageManager.ONLY_LETTERS_TAG);
        }
    }

    private void createAndSaveClan(Player player, ClanPlayer clanPlayer, String[] args) {
        String clanName = clanNameFromArgs(args);
        String clanTag = args[1];
        if (!TextUtil.containsColorCodes(clanTag)) {
            clanTag = configManager.getString(ConfigManager.DEFAULT_TAG_COLOR) + clanTag;
        }

        Clan clan = new Clan(clanTag, clanName, clanPlayer);
        if (!clanRepository.add(clan)) {
            LanguageManager.send(player, LanguageManager.TAG_ALREADY_EXISTS);
            return;
        }

        clanPlayer.joinClan(clan);
        playerRepository.save(clanPlayer);

        if (!economyManager.take(player, configManager.getDouble(ConfigManager.CREATE_CLAN_COST))) {
            throw new IllegalStateException("Could not take money from player after checking that they have enough.");
        }

        LanguageManager.broadcast(player.getServer(), LanguageManager.CLAN_CREATED, clan.tag().value());
    }

    private ClanPlayer canCreate(Player player) {
        ClanPlayer clanPlayer = Finder.getPlayer(player);
        if (clanPlayer.inClan()) {
            LanguageManager.send(player, LanguageManager.CANNOT_IN_CLAN);
            return null;
        }

        double cost = configManager.getDouble(ConfigManager.CREATE_CLAN_COST);
        if (!economyManager.hasEnough(player, cost)) {
            String formattedCost = economyManager.format(cost);
            LanguageManager.send(player, LanguageManager.NO_MONEY_CREATE, formattedCost);
            return null;
        }

        return clanPlayer;
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

    private boolean validate(CommandSender sender, String[] args) {
        if (!CommandValidator.validate(sender, args, 2, LanguageManager.WRONG_CREATE_USAGE)) {
            return false;
        }

        String rawTag = TextUtil.stripColorAndFormatting(args[1]).toLowerCase();
        if (configManager.getStringList(ConfigManager.FORBIDDEN_TAGS).contains(rawTag)) {
            LanguageManager.send(sender, LanguageManager.FORBIDDEN_TAG);
            return false;
        }

        return true;
    }
}
