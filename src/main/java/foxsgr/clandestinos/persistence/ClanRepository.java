package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.domain.model.clan.Clan;

import java.util.List;

public interface ClanRepository {

    default void load() {

    }

    Clan find(String tag);

    default List<Clan> findAll() {
        return findAll(100, 0);
    }

    List<Clan> findAll(int limit, int offset);

    boolean add(Clan clan);

    void update(Clan clan);

    void remove(Clan clan);
}
