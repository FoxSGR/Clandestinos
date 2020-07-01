package foxsgr.clandestinos.persistence;

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

    public static void init(RepositoryFactory repositoryFactory) {
        PersistenceContext.repositoryFactory = repositoryFactory;
    }
}
