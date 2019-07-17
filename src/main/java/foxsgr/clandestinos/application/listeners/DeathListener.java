package foxsgr.clandestinos.application.listeners;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private PlayerRepository playerRepository;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent playerDeathEvent) {
        Player killerPlayer = playerDeathEvent.getEntity().getKiller();
        if (killerPlayer == null) {
            return;
        }

        ClanPlayer killed = Finder.getPlayer(playerDeathEvent.getEntity());
        ClanPlayer killer = Finder.getPlayer(killerPlayer);
        killer.incKillCount();
        playerRepository.save(killer);

        killed.incDeathCount();
        playerRepository.save(killed);
    }

    public void setup() {
        playerRepository = PersistenceContext.repositories().players();
    }
}
