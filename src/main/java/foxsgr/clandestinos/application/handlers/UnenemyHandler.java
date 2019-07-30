package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.NeutralityRequest;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.NeutralityRequestRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class UnenemyHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final NeutralityRequestRepository neutralityRequestRepository = PersistenceContext.repositories()
            .neutralityRequests();

    public void requestNeutrality(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanLeader = CommandValidator.validateClanLeader(sender, args, 2,
                LanguageManager.WRONG_UNENEMY_USAGE);
        if (clanLeader == null) {
            return;
        }

        Clan requester = clanLeader.first;
        Clan requestee = Finder.clanByTag(sender, args[1]);
        if (requestee == null) {
            return;
        }

        if (!requester.isEnemy(requestee)) {
            LanguageManager.send(sender, LanguageManager.NOT_YOUR_ENEMY);
            return;
        }

        String requesterTag = requester.simpleTag();
        String requesteeTag = requestee.simpleTag();
        if (neutralityRequestRepository.find(requesterTag, requesteeTag) != null) {
            LanguageManager.send(sender, LanguageManager.ALREADY_REQUESTED_NEUTRALITY);
            return;
        }

        NeutralityRequest neutralityRequest = neutralityRequestRepository.find(requesteeTag, requesterTag);
        if (neutralityRequest != null) {
            declareNeutral(sender, neutralityRequest, requester, requestee);
            return;
        }

        neutralityRequest = new NeutralityRequest(requester.tag().withoutColor(),
                requestee.tag().withoutColor());
        neutralityRequestRepository.save(neutralityRequest);
        LanguageManager.broadcast(sender.getServer(), LanguageManager.CLAN_WANTS_NEUTRAL, requester.tag(),
                requestee.tag());
    }

    private void declareNeutral(CommandSender sender, NeutralityRequest neutralityRequest, Clan requester, Clan requestee) {
        neutralityRequestRepository.remove(neutralityRequest);
        LanguageManager.broadcast(sender.getServer(), LanguageManager.CLANS_NOW_NEUTRAL, requester.tag(),
                requestee.tag());

        requester.removeEnemy(requestee);
        clanRepository.update(requester);

        requestee.removeEnemy(requester);
        clanRepository.update(requestee);
    }
}
