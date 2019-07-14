package foxsgr.clandestinos.persistence.jpa;

import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.RepositoryFactory;
import org.jetbrains.annotations.NotNull;

public class JPARepositoryFactory implements RepositoryFactory {

    private ClanPlayerRepository clanPlayerRepository;
    private ClanRepository clanRepository;
    private InviteRepository inviteRepository;

    public JPARepositoryFactory() {
        clanPlayerRepository = new ClanPlayerRepositoryJPA();
        clanRepository = new ClanRepositoryJPA();
        inviteRepository = new InviteRepositoryJPA();
    }

    @Override
    public @NotNull ClanPlayerRepository players() {
        return clanPlayerRepository;
    }

    @Override
    public @NotNull ClanRepository clans() {
        return clanRepository;
    }

    @Override
    public @NotNull InviteRepository invites() {
        return inviteRepository;
    }
}
