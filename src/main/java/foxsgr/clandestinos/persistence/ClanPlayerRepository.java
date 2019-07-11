package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;

public interface ClanPlayerRepository {

    ClanPlayer find(String id);
    ClanPlayer save(ClanPlayer clanPlayer);
}
