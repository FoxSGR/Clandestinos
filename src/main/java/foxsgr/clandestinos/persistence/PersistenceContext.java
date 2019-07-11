package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.persistence.jpa.JPARepositoryFactory;
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

    @SuppressWarnings({"unused", "squid:CommentedOutCodeLine"})
    public static void init(JavaPlugin plugin) {
        // repositoryFactory = new YAMLRepositoryFactory(plugin);
        repositoryFactory = new JPARepositoryFactory();
    }
}
