package foxsgr.clandestinos.persistence.jpa;

import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;

class ClanPlayerRepositoryJPA implements ClanPlayerRepository {

    @Override
    public ClanPlayer find(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(ClanPlayer clanPlayer) {
        throw new UnsupportedOperationException();
    }
}
