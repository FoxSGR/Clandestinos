package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.RepositoryFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class YAMLRepositoryFactory implements RepositoryFactory {

    private ClanPlayerRepository clanPlayerRepository;
    private ClanRepository clanRepository;
    private InviteRepository inviteRepository;

    public YAMLRepositoryFactory(@NotNull JavaPlugin plugin) {
        clanPlayerRepository = new ClanPlayerRepositoryYAML(plugin);
        clanRepository = new ClanRepositoryYAML(plugin);
        inviteRepository = new InviteRepositoryYAML(plugin);
    }

    @Override
    @NotNull
    public ClanPlayerRepository players() {
        return clanPlayerRepository;
    }

    @Override
    @NotNull
    public ClanRepository clans() {
        return clanRepository;
    }

    @Override
    @NotNull
    public InviteRepository invites() {
        return inviteRepository;
    }
}
