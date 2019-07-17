package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.persistence.PlayerRepository;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.RepositoryFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class YAMLRepositoryFactory implements RepositoryFactory {

    private PlayerRepository playerRepository;
    private ClanRepository clanRepository;
    private InviteRepository inviteRepository;

    public YAMLRepositoryFactory(@NotNull JavaPlugin plugin) {
        playerRepository = new PlayerRepositoryYAML(plugin);
        clanRepository = new ClanRepositoryYAML(plugin);
        inviteRepository = new InviteRepositoryYAML(plugin);
    }

    @Override
    @NotNull
    public PlayerRepository players() {
        return playerRepository;
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
