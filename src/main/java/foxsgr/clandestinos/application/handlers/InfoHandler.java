package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.application.PlayerCommandValidator;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.domain.services.CalculateClanKDRService;
import foxsgr.clandestinos.domain.services.CalculatePlayerKDRService;
import foxsgr.clandestinos.domain.services.KDRDTO;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class InfoHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final PlayerRepository playerRepository = PersistenceContext.repositories().players();
    private final LanguageManager languageManager = LanguageManager.getInstance();

    private static final String CLAN_TYPE = "clan";
    private static final String PLAYER_TYPE = "player";

    public void showInfo(CommandSender sender, String[] args) {
        Player player = PlayerCommandValidator.playerFromSender(sender);
        if (player == null) {
            return;
        }

        if (args.length == 1) {
            // Must be a player
            if (PlayerCommandValidator.playerFromSender(sender) != null) {
                specific(player, sender.getName(), PLAYER_TYPE);
            }
        } else if (args.length == 2) { // info, tag/name
            trialAndError(player, args[1]);
        } else if (args.length == 3) { //info, clan/player, tag/name
            specific(player, args[2], args[1]);
        } else {
            LanguageManager.send(sender, LanguageManager.WRONG_INFO_USAGE);
        }
    }

    private void specific(CommandSender sender, String identifier, String type) {
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
        Clan clan = clanRepository.findByTag(identifier);
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
        infoBuilder.append(languageManager.get(LanguageManager.INFO)).append(' ').append(clan.tag()).append('\n')
                .append(languageManager.get(LanguageManager.NAME)).append(clan.name()).append('\n')
                .append(languageManager.get(LanguageManager.LEADERS)).append('\n');

        for (String id : clan.leaders()) {
            String name = Finder.nameFromId(id);
            infoBuilder.append("- ").append(name).append('\n');
        }

        infoBuilder.append(languageManager.get(LanguageManager.MEMBERS)).append('\n');

        List<String> allPlayers = clan.allPlayers();
        for (int i = 0; i < allPlayers.size(); i++) {
            infoBuilder.append(allPlayers.get(i));
            if (i != allPlayers.size() - 1) {
                infoBuilder.append(", ");
            }
        }

        Set<ClanPlayer> clanPlayers = Finder.playersInClan(clan);
        CalculateClanKDRService calculateClanKDRService = new CalculateClanKDRService();
        KDRDTO kdrDTO = calculateClanKDRService.calculateClanKDR(clanPlayers);

        infoBuilder.append('\n').append(languageManager.get(LanguageManager.KDR)).append(' ')
                .append(String.format("%.2f", kdrDTO.kdr)).append('\n')
                .append(languageManager.get(LanguageManager.KILLS)).append(' ').append(kdrDTO.kills).append('\n')
                .append(languageManager.get(LanguageManager.DEATHS)).append(' ').append(kdrDTO.deaths);

        sender.sendMessage(infoBuilder.toString());
    }

    private void showPlayerInfo(CommandSender sender, String name, ClanPlayer clanPlayer) {
        CalculatePlayerKDRService calculatePlayerKDRService = new CalculatePlayerKDRService();
        KDRDTO kdrDTO = calculatePlayerKDRService.calculatePlayerKDR(clanPlayer);

        String clan = "";
        if (clanPlayer.inClan()) {
            clan = clanPlayer.clan().value();
        }

        String infoBuilder = languageManager.get(LanguageManager.PLAYER) + ' ' + name + '\n'
                + languageManager.get(LanguageManager.CLAN) + ' ' + clan + '\n'
                + languageManager.get(LanguageManager.KDR) + ' '
                + String.format("%.2f", kdrDTO.kdr) + '\n'
                + languageManager.get(LanguageManager.KILLS) + ' ' + kdrDTO.kills + '\n'
                + languageManager.get(LanguageManager.DEATHS) + ' ' + kdrDTO.deaths;
        sender.sendMessage(infoBuilder);
    }
}
