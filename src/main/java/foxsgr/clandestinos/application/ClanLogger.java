package foxsgr.clandestinos.application;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ClanLogger {

    private static Logger logger;

    public static Logger getLogger() {
        return logger;
    }

    public static void init(JavaPlugin plugin) {
        logger = plugin.getLogger();
    }
}
