package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.domain.model.Invite;
import foxsgr.clandestinos.domain.model.clan.Clan;

public interface InviteRepository {

    void add(Invite invite);

    Invite find(String invited, String clanTag);

    void remove(Invite invite);

    void removeAllFrom(Clan clan);
}
