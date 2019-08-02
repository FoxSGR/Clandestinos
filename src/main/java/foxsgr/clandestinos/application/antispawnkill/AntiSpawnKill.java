package foxsgr.clandestinos.application.antispawnkill;

import foxsgr.clandestinos.application.config.ConfigManager;
import foxsgr.clandestinos.application.config.LanguageManager;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class AntiSpawnKill {

    private List<Kill> recentKills;
    private int period;
    private int maxKillsInPeriod;
    private int maxDistance;

    private final LanguageManager languageManager = LanguageManager.getInstance();

    public AntiSpawnKill() {
        recentKills = new ArrayList<>();

        ConfigManager configManager = ConfigManager.getInstance();
        period = configManager.getInt(ConfigManager.ANTI_SPAWN_KILL_PERIOD);
        maxKillsInPeriod = configManager.getInt(ConfigManager.ANTI_SPAWN_KILL_MAX);
        maxDistance = configManager.getInt(ConfigManager.ANTI_SPAWN_KILL_AREA);
    }

    @SuppressWarnings("squid:S135") // I can use continues and breaks whenever I want, sonar lint -.-
    public void add(Location location, Player killer, OfflinePlayer killed) {
        Kill newKill = new Kill(location, killer, killed);

        int count = 0;
        double xSum = 0;
        double zSum = 0;
        ListIterator<Kill> iterator = recentKills.listIterator(recentKills.size());
        while (iterator.hasPrevious()) {
            Kill kill = iterator.previous();

            // Clean up old kills
            long difference = kill.dateDifferenceInSeconds(newKill);
            if (difference > period) {
                iterator.remove();
                continue;
            }

            if (kill.equals(newKill)) { // If same killer and killed
                count++;
                xSum += kill.location.getX();
                zSum += kill.location.getZ();
            }

            if (count == maxKillsInPeriod) {
                break;
            }
        }

        assess(killer, newKill, count, xSum, zSum);
        recentKills.add(newKill);
    }

    private void assess(Player killer, Kill kill, int count, double xSum, double zSum) {
        if (count < maxKillsInPeriod) {
            return;
        }

        double averageX = xSum / maxKillsInPeriod;
        double averageZ = zSum / maxKillsInPeriod;

        Location location = new Location(kill.location.getWorld(), averageX, 0, averageZ);
        double distance = location.distance(kill.location);

        if (distance <= maxDistance) {
            killer.kickPlayer(languageManager.get(LanguageManager.KICKED_FOR_SPAWN_KILL));
        }
    }
}
