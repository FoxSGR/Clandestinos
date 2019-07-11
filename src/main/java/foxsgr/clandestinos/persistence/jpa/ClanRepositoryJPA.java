package foxsgr.clandestinos.persistence.jpa;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.persistence.ClanRepository;

import javax.persistence.Query;

class ClanRepositoryJPA extends JPARepository<Clan, Integer> implements ClanRepository {

    /**
     * Creates the repository.
     */
    ClanRepositoryJPA() {
        super(Clan.class);
    }

    @Override
    public Clan findByTag(String tag) {
        Query query = entityManager().createQuery("SELECT c FROM Clan c WHERE c.tag.tagValue = :t");
        query.setParameter("t", tag);
        return (Clan) singleResult(query);
    }
}
