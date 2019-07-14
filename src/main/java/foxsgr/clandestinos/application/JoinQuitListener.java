package foxsgr.clandestinos.application;

import foxsgr.clandestinos.application.clans.ClanPlayerFinder;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private ClanPlayerRepository clanPlayerRepository;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        String id = ClanPlayerFinder.idFromPlayer(player);
        clanPlayerRepository.load(id);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        String id = ClanPlayerFinder.idFromPlayer(player);
        clanPlayerRepository.unload(id);
    }

    void setup() {
        clanPlayerRepository = PersistenceContext.repositories().players();
    }
}
