package foxsgr.clandestinos.application.antispawnkill;

import foxsgr.clandestinos.application.config.ConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class AntiSpawnKill {

    private List<Kill> recentKills;
    private int period;
    private int maxKillsInPeriod;
    private int maxDistance;

    public AntiSpawnKill() {
        recentKills = new ArrayList<>();

        ConfigManager configManager = ConfigManager.getInstance();
        period = configManager.getInt(ConfigManager.ANTI_SPAWN_KILL_PERIOD);
        maxKillsInPeriod = configManager.getInt(ConfigManager.ANTI_SPAWN_KILL_MAX);
        maxDistance = configManager.getInt(ConfigManager.ANTI_SPAWN_KILL_AREA);
    }

    @SuppressWarnings("squid:S135") // I can use continues and breaks whenever I want, sonar lint -.-
    public void add(Location location, Player killer, Player killed) {
        Kill newKill = new Kill(location, killer, killed);

        int count = 0;
        double xSum = 0;
        double zSum = 0;

        // List iterator starting in the end of the list for iterating in reverse easily.
        // Iterating in reverse because we want to start in the most recent kills.
        ListIterator<Kill> iterator = recentKills.listIterator(recentKills.size());
        while (iterator.hasPrevious()) {
            Kill kill = iterator.previous();

            // Clean up old kills
            long difference = Math.abs(kill.dateDifferenceInSeconds(newKill));
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

        assess(killer, location, count, xSum, zSum);
        recentKills.add(newKill);
    }

    private void assess(Player killer, Location killLocation, int count, double xSum, double zSum) {
        if (count < maxKillsInPeriod) {
            return;
        }

        double averageX = xSum / maxKillsInPeriod;
        double averageZ = zSum / maxKillsInPeriod;

        Location averageLocation = new Location(killLocation.getWorld(), averageX, 0, averageZ);
        Location killLocationFixed = new Location(killLocation.getWorld(), killLocation.getX(), 0, killLocation.getZ());
        double distance = killLocationFixed.distance(averageLocation);

        if (distance <= maxDistance) {
            killer.getServer().dispatchCommand(killer.getServer().getConsoleSender(), "kick " + killer.getName()
                    + " Não podes matar o mesmo jogador tantas vezes seguidas no mesmo local.");
        }
    }
}
