package foxsgr.clandestinos.persistence;

import foxsgr.clandestinos.domain.model.NeutralityRequest;
import foxsgr.clandestinos.domain.model.clan.Clan;

public interface NeutralityRequestRepository {

    void save(NeutralityRequest request);

    NeutralityRequest find(String requesterTag, String requesteeTag);

    void remove(NeutralityRequest request);

    void removeAllFrom(Clan clan);
}
