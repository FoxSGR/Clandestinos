package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.domain.model.clan.Clan;

import java.util.List;

public interface ClanRepository {

    default void load() {

    }

    Clan find(String tag);

    List<Clan> findAll();

    boolean add(Clan clan);

    void update(Clan clan);

    void remove(Clan clan);
}
