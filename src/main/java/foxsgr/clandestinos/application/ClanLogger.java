package foxsgr.clandestinos.application;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Provides easy static access to the plugin's logger.
 */
class ClanLogger {

    /**
     * The plugin's logger.
     */
    private static Logger logger;

    /**
     * Private constructor to hide the implicit public one.
     */
    private ClanLogger() {
        // Should be empty.
    }

    /**
     * Returns the plugin's logger.
     *
     * @return the plugin's logger.
     */
    static Logger getLogger() {
        return logger;
    }

    /**
     * Initializes the logger field with the plugin's logger.
     *
     * @param plugin the plugin.
     */
    static void init(JavaPlugin plugin) {
        logger = plugin.getLogger();
    }
}
