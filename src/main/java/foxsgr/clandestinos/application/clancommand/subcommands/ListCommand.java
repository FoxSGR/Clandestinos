package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.Clandestinos;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.PermissionsManager;
import foxsgr.clandestinos.application.config.LanguageManager;
import foxsgr.clandestinos.domain.model.KDR;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.services.CalculateClanKDRService;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.TaskUtil;
import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListCommand implements SubCommand {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final LanguageManager languageManager = LanguageManager.getInstance();

    private static final int CLANS_PER_PAGE = 10;

    @Override
    public void run(CommandSender sender, String[] args) {
        if (!PermissionsManager.hasAndWarn(sender, args[0])) {
            return;
        }

        TaskUtil.runAsync(Clandestinos.getInstance(), () -> send(sender, args));
    }

    private void send(CommandSender sender, String[] args) {
        int pageNumber = 1;
        if (args.length > 1) {
            try {
                pageNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                LanguageManager.send(sender, LanguageManager.WRONG_LIST_USAGE);
                return;
            }
        }

        clanRepository.load();
        List<Clan> clans = findClans();
        int maxPageNumber = clans.size() / CLANS_PER_PAGE;

        // 9/10 is 0 and 10/10 is 1, but 10 clans only fills 1 page. Therefore, when amount of clans % page size is not
        // 0, a page should be added. Otherwise, it shouldn't.
        if (clans.size() % CLANS_PER_PAGE != 0) {
            maxPageNumber++;
        }

        if (pageNumber <= 0 || pageNumber > maxPageNumber) {
            LanguageManager.send(sender, LanguageManager.CLANS_LIST_INVALID_PAGE_NUMBER, maxPageNumber);
            return;
        }

        LanguageManager.send(sender, TextUtil.translateColoredText(makeMessage(clans, pageNumber)));
    }

    private List<Clan> findClans() {
        List<Clan> clans = clanRepository.findAll();
        clans.sort((clan1, clan2) -> {
            CalculateClanKDRService calculateClanKDRService = new CalculateClanKDRService();
            KDR clan1KDR = clan1.kdr();
            if (clan1KDR == null) {
                clan1KDR = calculateClanKDRService.calculateClanKDR(clan1, Finder.playersInClan(clan1));
            }

            KDR clan2KDR = clan2.kdr();
            if (clan2KDR == null) {
                clan2KDR = calculateClanKDRService.calculateClanKDR(clan2, Finder.playersInClan(clan2));
            }

            int result = clan1KDR.compareTo(clan2KDR);
            if (result != 0) {
                return result;
            }

            return clan1.tag().value().compareTo(clan2.tag().value());
        });

        return clans;
    }

    private String makeMessage(List<Clan> clans, int pageNumber) {
        if (clans.isEmpty()) {
            return languageManager.get(LanguageManager.THERE_ARE_NO_CLANS);
        }

        StringBuilder builder = new StringBuilder()
                .append(languageManager.get(LanguageManager.CLANS_LIST_HEADER)).append('\n');

        CalculateClanKDRService calculateClanKDRService = new CalculateClanKDRService();
        pageNumber--;
        int startIndex = pageNumber * CLANS_PER_PAGE;
        int endIndex = pageNumber * CLANS_PER_PAGE + CLANS_PER_PAGE;
        for (int i = startIndex; i < endIndex && i < clans.size(); i++) {
            Clan clan = clans.get(i);
            KDR kdr = clan.kdr();
            if (kdr == null) {
                kdr = calculateClanKDRService.calculateClanKDR(clan, Finder.playersInClan(clan));
            }

            builder.append(languageManager.get(LanguageManager.CLANS_LIST_INDEX, i + 1)).append(' ').append(clan.tag())
                    .append(' ').append(languageManager.get(LanguageManager.CLANS_LIST_KDR, kdr)).append('\n');
        }

        builder.append(languageManager.get(LanguageManager.CLANS_LIST_PAGE, pageNumber + 1));
        return builder.toString();
    }
}

