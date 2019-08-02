package foxsgr.clandestinos.util;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.locks.Lock;

public final class TaskUtil {

    /**
     * Private constructor to hide the implicit public one.
     */
    private TaskUtil() {
        // Should be empty.
    }

    public static void runAsync(Lock mutex, JavaPlugin plugin, Runnable task) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mutex.lock();
                try {
                    task.run();
                    mutex.unlock();
                } catch (Exception e) {
                    mutex.unlock();
                    throw e;
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public static void runAsync(JavaPlugin plugin, Runnable task) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskAsynchronously(plugin);
    }
}
