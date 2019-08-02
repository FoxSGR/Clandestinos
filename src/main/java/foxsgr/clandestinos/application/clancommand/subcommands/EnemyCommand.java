package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.config.LanguageManager;
import foxsgr.clandestinos.domain.model.NeutralityRequest;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.NeutralityRequestRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class EnemyCommand implements SubCommand {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final NeutralityRequestRepository neutralityRequestRepository = PersistenceContext.repositories()
            .neutralityRequests();

    @Override
    public void run(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanLeader = CommandValidator.validateClanLeader(sender, args, 2,
                LanguageManager.WRONG_ENEMY_USAGE);
        if (clanLeader == null) {
            return;
        }

        Clan otherClan = Finder.clanByTag(sender, args[1]);
        if (otherClan == null) {
            return;
        }

        Clan playerClan = clanLeader.first;
        if (playerClan.equals(otherClan)) {
            LanguageManager.send(sender, LanguageManager.THATS_YOUR_CLAN, playerClan.tag());
            return;
        }

        if (neutralityRequestExists(sender, playerClan, otherClan)) {
            return;
        }

        if (!playerClan.addEnemy(otherClan) || !otherClan.addEnemy(playerClan)) {
            LanguageManager.send(sender, LanguageManager.ALREADY_YOUR_ENEMY, otherClan.tag());
        } else {
            LanguageManager.broadcast(sender.getServer(), LanguageManager.ENEMY_DECLARATION, playerClan.tag(),
                    otherClan.tag());
        }

        clanRepository.update(playerClan);
        clanRepository.update(otherClan);
    }

    private boolean neutralityRequestExists(CommandSender sender, Clan playerClan, Clan otherClan) {
        NeutralityRequest neutralityRequest = neutralityRequestRepository.find(playerClan.simpleTag(),
                otherClan.simpleTag());
        if (neutralityRequest != null) {
            neutralityRequestRepository.remove(neutralityRequest);
            LanguageManager.send(sender, LanguageManager.NEUTRAL_CANCELED, otherClan.tag());
            return true;
        }

        neutralityRequest = neutralityRequestRepository.find(otherClan.simpleTag(), playerClan.simpleTag());
        if (neutralityRequest != null) {
            neutralityRequestRepository.remove(neutralityRequest);
            LanguageManager.broadcast(sender.getServer(), LanguageManager.OTHERS_NEUTRAL_CANCELED, playerClan.tag(),
                    otherClan.tag());
            return true;
        }

        return false;
    }
}
