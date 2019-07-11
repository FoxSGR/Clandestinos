package foxsgr.clandestinos.persistence.jpa;

import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import foxsgr.clandestinos.persistence.ClanPlayerRepository;

class ClanPlayerRepositoryJPA extends JPARepository<ClanPlayer, String> implements ClanPlayerRepository {

    ClanPlayerRepositoryJPA() {
        super(ClanPlayer.class);
    }
}
