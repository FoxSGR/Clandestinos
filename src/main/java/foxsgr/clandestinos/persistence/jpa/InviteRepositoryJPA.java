package foxsgr.clandestinos.persistence.jpa;

import foxsgr.clandestinos.domain.model.invite.Invite;
import foxsgr.clandestinos.persistence.InviteRepository;

import javax.persistence.Query;

public class InviteRepositoryJPA extends JPARepository<Invite, Integer> implements InviteRepository {

    public InviteRepositoryJPA() {
        super(Invite.class);
    }

    @Override
    public Invite findPending(String invited, String clanTag) {
        Query query = entityManager().createQuery("SELECT i FROM Invite i WHERE i.inviteResult = foxsgr.clandestinos.domain.model.invite.InviteResult.PENDING AND i.invitedPlayer.id LIKE :invited AND i.invitedTo.tag.tagValue LIKE :clanTag");
        query.setParameter("invited", invited);
        query.setParameter("clanTag", clanTag);
        return (Invite) singleResult(query);
    }
}
