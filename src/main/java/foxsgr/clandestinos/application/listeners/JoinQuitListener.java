package foxsgr.clandestinos.application.listeners;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private PlayerRepository playerRepository;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        String id = Finder.idFromPlayer(player);
        playerRepository.load(id);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        String id = Finder.idFromPlayer(player);
        playerRepository.unload(id);
    }

    public void setup() {
        playerRepository = PersistenceContext.repositories().players();
    }
}
