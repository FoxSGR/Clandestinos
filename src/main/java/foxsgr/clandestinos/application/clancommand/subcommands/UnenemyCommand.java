package foxsgr.clandestinos.application.clancommand.subcommands;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.config.I18n;
import foxsgr.clandestinos.domain.model.NeutralityRequest;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.NeutralityRequestRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnenemyCommand implements SubCommand {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();
    private final NeutralityRequestRepository neutralityRequestRepository = PersistenceContext.repositories()
            .neutralityRequests();

    @Override
    public void run(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanLeader = CommandValidator.validateClanLeader(sender, args, 2,
                I18n.WRONG_UNENEMY_USAGE);
        if (clanLeader == null) {
            return;
        }

        Clan requester = clanLeader.first;
        Clan requestee = Finder.clanByTag(sender, args[1]);
        if (requestee == null) {
            return;
        }

        if (!requester.isEnemy(requestee)) {
            I18n.send(sender, I18n.NOT_YOUR_ENEMY);
            return;
        }

        String requesterTag = requester.simpleTag();
        String requesteeTag = requestee.simpleTag();
        if (neutralityRequestRepository.find(requesterTag, requesteeTag) != null) {
            I18n.send(sender, I18n.ALREADY_REQUESTED_NEUTRALITY);
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
        I18n.broadcast(sender.getServer(), I18n.CLAN_WANTS_NEUTRAL, requester.tag(),
                requestee.tag());

        warnRequesteeLeaders(sender, requestee, requester);
    }

    private void declareNeutral(CommandSender sender, NeutralityRequest neutralityRequest, Clan requester, Clan requestee) {
        neutralityRequestRepository.remove(neutralityRequest);
        I18n.broadcast(sender.getServer(), I18n.CLANS_NOW_NEUTRAL, requester.tag(),
                requestee.tag());

        requester.removeEnemy(requestee);
        clanRepository.update(requester);

        requestee.removeEnemy(requester);
        clanRepository.update(requestee);
    }

    @SuppressWarnings("squid:S135")
    private void warnRequesteeLeaders(CommandSender sender, Clan requestee, Clan requester) {
        for (Player player : sender.getServer().getOnlinePlayers()) {
            ClanPlayer clanPlayer = Finder.findPlayer(player);
            if (clanPlayer == null) {
                continue;
            }

            Clan clan = Finder.clanFromPlayer(clanPlayer);
            if (clan == null) {
                continue;
            }

            if (clan.equals(requestee) && clan.isLeader(clanPlayer)) {
                I18n.send(player, I18n.ACCEPT_NEUTRAL_HELP, requester.tag().withoutColor());
            }
        }
    }
}
