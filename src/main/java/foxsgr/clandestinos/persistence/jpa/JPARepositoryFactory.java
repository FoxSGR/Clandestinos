package foxsgr.clandestinos.persistence.jpa;

import foxsgr.clandestinos.persistence.*;
import org.jetbrains.annotations.NotNull;

public class JPARepositoryFactory implements RepositoryFactory {

    private PlayerRepository playerRepository;
    private ClanRepository clanRepository;
    private InviteRepository inviteRepository;

    public JPARepositoryFactory() {
        playerRepository = new PlayerRepositoryJPA();
        clanRepository = new ClanRepositoryJPA();
        inviteRepository = new InviteRepositoryJPA();
    }

    @Override
    public @NotNull PlayerRepository players() {
        return playerRepository;
    }

    @Override
    public @NotNull ClanRepository clans() {
        return clanRepository;
    }

    @Override
    public @NotNull InviteRepository invites() {
        return inviteRepository;
    }

    @Override
    public @NotNull NeutralityRequestRepository neutralityRequests() {
        throw new UnsupportedOperationException();
    }
}
