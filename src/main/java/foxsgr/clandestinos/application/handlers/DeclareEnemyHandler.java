package foxsgr.clandestinos.application.handlers;

import foxsgr.clandestinos.application.CommandValidator;
import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.LanguageManager;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Pair;
import org.bukkit.command.CommandSender;

public class DeclareEnemyHandler {

    private final ClanRepository clanRepository = PersistenceContext.repositories().clans();

    public void declareEnemy(CommandSender sender, String[] args) {
        Pair<Clan, ClanPlayer> clanLeader = CommandValidator.validateClanLeader(sender, args, 2,
                LanguageManager.WRONG_ENEMY_USAGE);
        if (clanLeader == null) {
            return;
        }

        Clan otherClan = Finder.clanByTag(sender, args[1]);
        if (otherClan == null) {
            return;
        }

        if (!clanLeader.first.addEnemy(otherClan) || !otherClan.addEnemy(clanLeader.first)) {
            LanguageManager.send(sender, LanguageManager.ALREADY_YOUR_ENEMY, otherClan.tag());
            return;
        }

        clanRepository.update(clanLeader.first);
        clanRepository.update(otherClan);
        LanguageManager.broadcast(sender.getServer(), LanguageManager.ENEMY_DECLARATION, clanLeader.first.tag(),
                otherClan.tag());
    }
}
