package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.persistence.yaml.YAMLRepositoryFactory;
import org.bukkit.plugin.java.JavaPlugin;

public final class PersistenceContext {

    private static RepositoryFactory repositoryFactory;

    private PersistenceContext() {

    }

    public static RepositoryFactory repositories() {
        return repositoryFactory;
    }

    public static void init(JavaPlugin plugin) {
        repositoryFactory = new YAMLRepositoryFactory(plugin);
    }
}
