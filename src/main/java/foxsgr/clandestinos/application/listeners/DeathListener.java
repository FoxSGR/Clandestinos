package foxsgr.clandestinos.application.listeners;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.antispawnkill.AntiSpawnKill;
import foxsgr.clandestinos.application.config.ConfigManager;
import foxsgr.clandestinos.domain.model.KDR;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.domain.services.CalculateClanKDRService;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.PlayerRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Set;

public class DeathListener implements Listener {

    private PlayerRepository playerRepository;
    private ClanRepository clanRepository;
    private AntiSpawnKill antiSpawnKill;

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

        updateClanKDR(killer, true);
        updateClanKDR(killed, false);

        if (antiSpawnKill != null) {
            antiSpawnKill.add(playerDeathEvent.getEntity().getLocation(), killerPlayer, playerDeathEvent.getEntity());
        }
    }

    public void setup() {
        playerRepository = PersistenceContext.repositories().players();
        clanRepository = PersistenceContext.repositories().clans();

        if (ConfigManager.getInstance().getBoolean(ConfigManager.ANTI_SPAWN_KILL_ENABLED)) {
            antiSpawnKill = new AntiSpawnKill();
        }
    }

    private void updateClanKDR(ClanPlayer player, boolean isKiller) {
        Clan clan = Finder.clanFromPlayer(player);
        if (clan == null) {
            return;
        }

        KDR kdr = clan.kdr();
        if (kdr == null) {
            calculateKDR(clan);
            return;
        }

        KDR newKDR;
        if (isKiller) {
            newKDR = kdr.addKills(1);
        } else {
            newKDR = kdr.addDeaths(1);
        }

        clan.updateKDR(newKDR);
        clanRepository.update(clan);
    }

    private void calculateKDR(Clan clan) {
        Set<ClanPlayer> clanPlayers = Finder.playersInClan(clan);
        CalculateClanKDRService calculateClanKDRService = new CalculateClanKDRService();
        calculateClanKDRService.calculateClanKDR(clan, clanPlayers);
    }
}
