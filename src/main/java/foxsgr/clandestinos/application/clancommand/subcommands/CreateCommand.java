package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.*;
import foxsgr.clandestinos.application.config.ConfigManager;
import foxsgr.clandestinos.application.config.I18n;
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

public class CreateCommand implements SubCommand {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();
    private final ConfigManager configManager = ConfigManager.getInstance();
    private final EconomyManager economyManager = EconomyManager.getInstance();

    @Override
    public void run(CommandSender sender, String[] args) {
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
            I18n.send(player, I18n.WRONG_SIZE_TAG);
        } catch (WrongNameSizeException e) {
            I18n.send(player, I18n.WRONG_SIZE_NAME);
        } catch (NonLetterInTagException e) {
            I18n.send(player, I18n.ONLY_LETTERS_TAG);
        }
    }

    private void createAndSaveClan(Player player, ClanPlayer clanPlayer, String[] args) {
        String clanName = clanNameFromArgs(args);
        String clanTag = args[1];
        if (!TextUtil.containsColorCodes(clanTag)) {
            clanTag = configManager.getString(ConfigManager.DEFAULT_TAG_COLOR) + clanTag;
        }

        Clan clan = new Clan(clanTag, clanName, clanPlayer);
        clanPlayer.joinClan(clan);

        if (!clanRepository.add(clan)) {
            I18n.send(player, I18n.TAG_ALREADY_EXISTS);
            return;
        }

        playerRepository.save(clanPlayer);

        economyManager.take(player, configManager.getDouble(ConfigManager.CREATE_CLAN_COST));
        I18n.broadcast(player.getServer(), I18n.CLAN_CREATED, clan.tag().value());
    }

    private ClanPlayer canCreate(Player player) {
        ClanPlayer clanPlayer = Finder.getPlayer(player);
        if (clanPlayer.inClan()) {
            I18n.send(player, I18n.CANNOT_IN_CLAN);
            return null;
        }

        double cost = configManager.getDouble(ConfigManager.CREATE_CLAN_COST);
        if (!economyManager.hasEnough(player, cost)) {
            String formattedCost = economyManager.format(cost);
            I18n.send(player, I18n.NO_MONEY_CREATE, formattedCost);
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
        if (!CommandValidator.validate(sender, args, 2, I18n.WRONG_CREATE_USAGE)) {
            return false;
        }

        String rawTag = TextUtil.stripColorAndFormatting(args[1]).toLowerCase();
        if (configManager.getStringList(ConfigManager.FORBIDDEN_TAGS).contains(rawTag)) {
            I18n.send(sender, I18n.FORBIDDEN_TAG);
            return false;
        }

        return true;
    }
}
