package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;

public interface ClanPlayerRepository {

    ClanPlayer find(String id);

    void save(ClanPlayer clanPlayer);

    void load(String id);

    void unload(String id);

    void leaveFromClan(Clan clan);
}
