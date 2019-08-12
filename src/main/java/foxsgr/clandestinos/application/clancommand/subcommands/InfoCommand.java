package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.config.LanguageManager;
import foxsgr.clandestinos.application.PermissionsManager;
import foxsgr.clandestinos.domain.model.KDR;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanName;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.domain.services.CalculateClanKDRService;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Set;

public class InfoCommand implements SubCommand {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();
    private final LanguageManager languageManager = LanguageManager.getInstance();

    private static final String CLAN_TYPE = "clan";
    private static final String PLAYER_TYPE = "player";

    @Override
    public void run(CommandSender sender, String[] args) {
        if (!PermissionsManager.hasAndWarn(sender, args[0])) {
            return;
        }

        switch (args.length) {
            case 1:
                // Must be a player
                if (CommandValidator.playerFromSender(sender) != null) {
                    specific(sender, PLAYER_TYPE, sender.getName());
                }
                break;
            case 2:  // info, tag/name
                trialAndError(sender, args[1]);
                break;
            case 3:  //info, clan/player, tag/name
                specific(sender, args[1], args[2]);
                break;
            default:
                LanguageManager.send(sender, LanguageManager.WRONG_INFO_USAGE);
                break;
        }
    }

    private void specific(CommandSender sender, String type, String identifier) {
        if (type.equalsIgnoreCase(CLAN_TYPE)) {
            Clan clan = Finder.clanByTag(sender, identifier);
            if (clan == null) {
                return;
            }

            showClanInfo(sender, clan);
        } else if (type.equalsIgnoreCase(PLAYER_TYPE)) {
            ClanPlayer clanPlayer = Finder.playerByName(sender, identifier);
            if (clanPlayer == null) {
                return;
            }

            showPlayerInfo(sender, identifier, clanPlayer);
        } else {
            LanguageManager.send(sender, LanguageManager.WRONG_INFO_USAGE);
        }
    }

    private void trialAndError(CommandSender sender, String identifier) {
        Clan clan = clanRepository.find(identifier);
        if (clan != null) {
            showClanInfo(sender, clan);
            return;
        }

        ClanPlayer clanPlayer = playerRepository.find(Finder.idFromName(identifier));
        if (clanPlayer != null) {
            showPlayerInfo(sender, identifier, clanPlayer);
        } else {
            LanguageManager.send(sender, LanguageManager.UNKNOWN_PLAYER_CLAN);
        }
    }

    private void showClanInfo(CommandSender sender, Clan clan) {
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append(languageManager.get(LanguageManager.INFO)).append(' ').append(clan.tag()).append('\n');

        ClanName clanName = clan.name();
        if (!clanName.value().isEmpty()) {
            infoBuilder.append(languageManager.get(LanguageManager.NAME)).append(clan.name()).append('\n');
        }

        infoBuilder.append(languageManager.get(LanguageManager.OWNER)).append(' ').append(clan.owner()).append('\n')
                .append(languageManager.get(LanguageManager.LEADERS)).append('\n');

        for (String id : clan.leaders()) {
            String name = Finder.nameFromId(id);
            infoBuilder.append("- ").append(name).append('\n');
        }

        infoBuilder.append(languageManager.get(LanguageManager.MEMBERS)).append(" (").append(clan.allPlayers().size())
                .append("):\n");
        List<String> allPlayers = clan.allPlayers();
        for (int i = 0; i < allPlayers.size(); i++) {
            infoBuilder.append(allPlayers.get(i));
            if (i != allPlayers.size() - 1) {
                infoBuilder.append(", ");
            }
        }

        List<String> enemies = clan.enemyClans();
        if (!enemies.isEmpty()) {
            infoBuilder.append('\n').append(languageManager.get(LanguageManager.ENEMIES)).append('\n');
        }

        for (int i = 0; i < enemies.size(); i++) {
            infoBuilder.append(enemies.get(i).toUpperCase());
            if (i != enemies.size() - 1) {
                infoBuilder.append(", ");
            }
        }

        KDR kdr = clan.kdr();
        if (kdr == null) {
            Set<ClanPlayer> clanPlayers = Finder.playersInClan(clan);
            CalculateClanKDRService calculateClanKDRService = new CalculateClanKDRService();
            calculateClanKDRService.calculateClanKDR(clan, clanPlayers);
            kdr = clan.kdr();
        }

        assert kdr != null;
        infoBuilder.append('\n').append(languageManager.get(LanguageManager.KDR)).append(' ')
                .append(kdr).append('\n')
                .append(languageManager.get(LanguageManager.KILLS)).append(' ').append(kdr.kills()).append('\n')
                .append(languageManager.get(LanguageManager.DEATHS)).append(' ').append(kdr.deaths());
        sender.sendMessage(TextUtil.translateColoredText(infoBuilder.toString()));
    }

    private void showPlayerInfo(CommandSender sender, String name, ClanPlayer clanPlayer) {
        KDR kdr = clanPlayer.kdr();
        String clan = "";
        if (clanPlayer.inClan()) {
            clan = Finder.findClanEnsureExists(clanPlayer).tag().value();
        }

        String info = languageManager.get(LanguageManager.PLAYER) + ' ' + name + '\n'
                + languageManager.get(LanguageManager.CLAN) + ' ' + clan + '\n'
                + languageManager.get(LanguageManager.KDR) + ' ' + kdr + '\n'
                + languageManager.get(LanguageManager.KILLS) + ' ' + kdr.kills() + '\n'
                + languageManager.get(LanguageManager.DEATHS) + ' ' + kdr.deaths();
        sender.sendMessage(TextUtil.translateColoredText(info));
    }
}
