package foxsgr.clandestinos.persistence.jpa;

import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.persistence.InviteRepository;

public class InviteRepositoryJPA implements InviteRepository {

    @Override
    public void add(Invite invite) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Invite find(String invited, String clanTag) {
        /*Query query = entityManager().createQuery("SELECT i FROM Invite i WHERE i.inviteResult = foxsgr.clandestinos.domain.model.invite.InviteResult.PENDING AND i.invitedPlayer.id LIKE :invited AND i.invitedTo.tag.tagValue LIKE :clanTag");
        query.setParameter("invited", invited);
        query.setParameter("clanTag", clanTag);
        return (Invite) singleResult(query);*/
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Invite invite) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAllFrom(Clan clan) {
        throw new UnsupportedOperationException();
    }
}
