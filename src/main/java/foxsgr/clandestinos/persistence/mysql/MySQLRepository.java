package foxsgr.clandestinos.persistence.mysql;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class MySQLRepository {

    protected final JavaPlugin plugin;

    public MySQLRepository(JavaPlugin plugin) {
        this.plugin = plugin;
    }
}
