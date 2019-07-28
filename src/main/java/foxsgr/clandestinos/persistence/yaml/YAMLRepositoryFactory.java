package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.persistence.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class YAMLRepositoryFactory implements RepositoryFactory {

    private PlayerRepository playerRepository;
    private ClanRepository clanRepository;
    private InviteRepository inviteRepository;
    private NeutralityRequestRepository neutralityRequestRepository;

    public YAMLRepositoryFactory(@NotNull JavaPlugin plugin) {
        playerRepository = new PlayerRepositoryYAML(plugin);
        clanRepository = new ClanRepositoryYAML(plugin);
        inviteRepository = new InviteRepositoryYAML(plugin);
        neutralityRequestRepository = new NeutralityRequestRepositoryYAML(plugin);
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

    @Override
    public @NotNull NeutralityRequestRepository neutralityRequests() {
        return neutralityRequestRepository;
    }
}
