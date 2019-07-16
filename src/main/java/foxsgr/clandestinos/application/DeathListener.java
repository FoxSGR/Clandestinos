package foxsgr.clandestinos.application;

import foxsgr.clandestinos.application.clans.ClanPlayerFinder;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private ClanPlayerRepository clanPlayerRepository;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent playerDeathEvent) {
        ClanPlayer killed = ClanPlayerFinder.get(playerDeathEvent.getEntity());
        Player killerPlayer = playerDeathEvent.getEntity().getKiller();
        if (killerPlayer != null) {
            ClanPlayer killer = ClanPlayerFinder.get(playerDeathEvent.getEntity().getKiller());
            killer.incKillCount();
            clanPlayerRepository.save(killer);
        }

        killed.incDeathCount();
        clanPlayerRepository.save(killed);
    }

    void setup() {
        clanPlayerRepository = PersistenceContext.repositories().players();
    }
}
