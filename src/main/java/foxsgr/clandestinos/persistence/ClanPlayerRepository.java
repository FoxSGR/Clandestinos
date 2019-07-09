package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.domain.model.ClanPlayer;

public interface ClanPlayerRepository {

    ClanPlayer find(String id);
    void save(ClanPlayer clanPlayer);
}
