package foxsgr.clandestinos.persistence.jpa;

import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.PlayerRepository;

class PlayerRepositoryJPA implements PlayerRepository {

    @Override
    public ClanPlayer find(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(ClanPlayer clanPlayer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void load(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unload(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void leaveFromClan(Clan clan) {
        throw new UnsupportedOperationException();
    }
}
