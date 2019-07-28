package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.NeutralityRequest;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.NeutralityRequestRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class RequestNeutralityHandler {

    private final NeutralityRequestRepository neutralityRequestRepository = PersistenceContext.repositories()
            .neutralityRequests();

    public void requestNeutrality(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanLeader = CommandValidator.validateClanLeader(sender, args, 2,
                LanguageManager.WRONG_UNENEMY_USAGE);
        if (clanLeader == null) {
            return;
        }

        Clan requestee = Finder.clanByTag(sender, args[1]);
        if (requestee == null) {
            return;
        }

        if (!clanLeader.first.isEnemy(requestee)) {
            LanguageManager.send(sender, LanguageManager.NOT_YOUR_ENEMY);
            return;
        }

        String requesterTag = clanLeader.first.simpleTag();
        String requesteeTag = requestee.simpleTag();
        if (neutralityRequestRepository.find(requesterTag, requesteeTag) != null) {
            LanguageManager.send(sender, LanguageManager.ALREADY_REQUESTED_NEUTRALITY);
            return;
        }

        NeutralityRequest neutralityRequest = neutralityRequestRepository.find(requesteeTag, requesterTag);
        if (neutralityRequest != null) {
            neutralityRequestRepository.remove(neutralityRequest);
            // TODO: broadcast that clans are now neutral
            return;
        }

        neutralityRequest = new NeutralityRequest(clanLeader.first.tag().withoutColor(),
                requestee.tag().withoutColor());
        neutralityRequestRepository.save(neutralityRequest);
        // TODO: broadcast neutrality request here
    }
}
