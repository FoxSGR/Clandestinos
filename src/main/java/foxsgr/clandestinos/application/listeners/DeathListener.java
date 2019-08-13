package foxsgr.clandestinos.application.listeners;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.application.antispawnkill.AntiSpawnKill;
import foxsgr.clandestinos.application.config.ConfigManager;
import foxsgr.clandestinos.application.config.LanguageManager;
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

    private final ConfigManager configManager = ConfigManager.getInstance();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent playerDeathEvent) {
        Player killerPlayer = playerDeathEvent.getEntity().getKiller();
        if (killerPlayer == null) {
            return;
        }

        ClanPlayer killed = Finder.getPlayer(playerDeathEvent.getEntity());
        ClanPlayer killer = Finder.getPlayer(killerPlayer);
        updateKDRs(killer, killed);
        updateClans(killerPlayer, killer, killed);

        if (antiSpawnKill != null) {
            antiSpawnKill.add(playerDeathEvent.getEntity().getLocation(), killerPlayer, playerDeathEvent.getEntity());
        }
    }

    public void setup() {
        playerRepository = PersistenceContext.repositories().players();
        clanRepository = PersistenceContext.repositories().clans();

        if (ConfigManager.getInstance().getBoolean(ConfigManager.ANTI_SPAWN_KILL_ENABLED)) {
            antiSpawnKill = new AntiSpawnKill();
        } else { // else clause necessary in case plugin gets reloaded with different anti spawn kill enabled setting
            antiSpawnKill = null;
        }
    }

    private void updateKDRs(ClanPlayer killer, ClanPlayer killed) {
        killer.incKillCount();
        playerRepository.save(killer);

        killed.incDeathCount();
        playerRepository.save(killed);
    }

    @SuppressWarnings("squid:S2259") // With the way things are implemented, NullPointerExceptions won't occur
    private void updateClans(Player killerPlayer, ClanPlayer killer, ClanPlayer killed) {
        Clan killerClan = Finder.clanFromPlayer(killer);
        Clan killedClan = Finder.clanFromPlayer(killed);
        boolean killerTurnedEnemy = updateClan(killerClan, killedClan, true);
        boolean killedTurnedEnemy = updateClan(killedClan, killedClan, false);
        if (killerTurnedEnemy || killedTurnedEnemy) {
            assert killerClan != null;
            assert killedClan != null;
            LanguageManager.broadcast(killerPlayer.getServer(), LanguageManager.CLANS_NOW_ENEMIES, killerClan.tag(),
                    killedClan.tag());
        }
    }

    private boolean updateClan(Clan clan, Clan otherClan, boolean isKiller) {
        if (clan == null) {
            return false;
        }

        KDR kdr = clan.kdr();
        if (kdr == null) {
            kdr = calculateKDR(clan);
        }

        KDR newKDR;
        if (isKiller) {
            newKDR = kdr.addKills(1);
        } else {
            newKDR = kdr.addDeaths(1);
        }

        boolean turnedEnemy = false;
        if (configManager.getBoolean(ConfigManager.TURN_ENEMY_ON_KILL) && otherClan != null
                && !clan.equals(otherClan) && !clan.isEnemy(otherClan)) {
            clan.addEnemy(otherClan);
            turnedEnemy = true;
        }

        clan.updateKDR(newKDR);
        clanRepository.update(clan);
        return turnedEnemy;
    }

    private KDR calculateKDR(Clan clan) {
        Set<ClanPlayer> clanPlayers = Finder.playersInClan(clan);
        CalculateClanKDRService calculateClanKDRService = new CalculateClanKDRService();
        return calculateClanKDRService.calculateClanKDR(clan, clanPlayers);
    }
}
