package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.persistence.yaml.YAMLRepositoryFactory;
import org.bukkit.plugin.java.JavaPlugin;

public final class PersistenceContext {

    private static RepositoryFactory repositoryFactory;

    /**
     * Private constructor to hide the implicit public one.
     */
    private PersistenceContext() {
        // Should be empty.
    }

    public static RepositoryFactory repositories() {
        return repositoryFactory;
    }

    public static void init(JavaPlugin plugin) {
        repositoryFactory = new YAMLRepositoryFactory(plugin);
    }
}
