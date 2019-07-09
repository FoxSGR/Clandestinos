package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.domain.model.Clan;

public interface ClanRepository {

    Clan find(String tag);
    boolean save(Clan clan);
    Clan changeTag(Clan clan, String newTag);
}
