package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.domain.model.clan.Clan;

public interface ClanRepository {

    Clan findByTag(String tag);

    boolean add(Clan clan);

    void update(Clan clan);

    void remove(Clan clan);
}
