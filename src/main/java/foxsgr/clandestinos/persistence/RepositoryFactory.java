package foxsgr.clandestinos.persistence;

import org.jetbrains.annotations.NotNull;

public interface RepositoryFactory {

    @NotNull
    PlayerRepository players();

    @NotNull
    ClanRepository clans();

    @NotNull
    InviteRepository invites();
}
