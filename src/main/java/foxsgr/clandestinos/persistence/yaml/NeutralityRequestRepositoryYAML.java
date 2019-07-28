package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.NeutralityRequest;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.persistence.NeutralityRequestRepository;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class NeutralityRequestRepositoryYAML extends YAMLRepository implements NeutralityRequestRepository {

    private static final String REQUESTER_FIELD = "requester";
    private static final String REQUESTEE_FIELD = "requestee";

    NeutralityRequestRepositoryYAML(JavaPlugin plugin) {
        super(plugin, "neutrality_requests");
    }

    @Override
    public void save(NeutralityRequest request) {
        String requesterTag = request.requester().withoutColor().value().toLowerCase();
        String requesteeTag = request.requestee().withoutColor().value().toLowerCase();

        FileConfiguration fileConfiguration = new YamlConfiguration();
        fileConfiguration.set(REQUESTER_FIELD, requesterTag);
        fileConfiguration.set(REQUESTEE_FIELD, requesteeTag);

        saveFile(fileConfiguration, request.id().toLowerCase());
    }

    @Override
    public NeutralityRequest find(String requesterTag, String requesteeTag) {
        String id = requesterTag + NeutralityRequest.ID_SEPARATOR + requesteeTag;
        FileConfiguration fileConfiguration = file(id.toLowerCase());
        if (fileConfiguration == null) {
            return null;
        }

        ClanTag readRequesterTag = new ClanTag(fileConfiguration.getString(REQUESTER_FIELD));
        ClanTag readRequesteeTag = new ClanTag(fileConfiguration.getString(REQUESTEE_FIELD));
        return new NeutralityRequest(readRequesterTag, readRequesteeTag);
    }

    @Override
    public void remove(NeutralityRequest request) {
        if (!makeFile(request.id().toLowerCase()).delete()) {
            logger().log(Level.WARNING, "Could not delete the neutrality request file {0}.yml", request.id());
        }
    }

    @Override
    public void removeAllFrom(Clan clan) {
        removeFilesStartingWith(clan.simpleTag());
    }
}
