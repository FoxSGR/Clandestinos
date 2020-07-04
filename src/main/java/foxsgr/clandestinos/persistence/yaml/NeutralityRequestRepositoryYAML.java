package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.domain.model.NeutralityRequest;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.persistence.NeutralityRequestRepository;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import static foxsgr.clandestinos.util.TaskUtil.runAsync;

public class NeutralityRequestRepositoryYAML extends YAMLRepository implements NeutralityRequestRepository {

    private static final String REQUESTER_FIELD = "requester";
    private static final String REQUESTEE_FIELD = "requestee";

    private static final Lock MUTEX = new ReentrantLock();

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

        MUTEX.lock();
        saveFile(fileConfiguration, request.id().toLowerCase());
        MUTEX.unlock();
    }

    @Override
    public NeutralityRequest find(String requesterTag, String requesteeTag) {
        String id = requesterTag + NeutralityRequest.ID_SEPARATOR + requesteeTag;

        MUTEX.lock();
        FileConfiguration fileConfiguration = file(id.toLowerCase());
        MUTEX.unlock();
        if (fileConfiguration == null) {
            return null;
        }

        ClanTag readRequesterTag = new ClanTag(fileConfiguration.getString(REQUESTER_FIELD));
        ClanTag readRequesteeTag = new ClanTag(fileConfiguration.getString(REQUESTEE_FIELD));
        return new NeutralityRequest(readRequesterTag, readRequesteeTag);
    }

    @Override
    public void remove(NeutralityRequest request) {
        MUTEX.lock();
        if (!makeFile(request.id().toLowerCase()).delete()) {
            logger().log(Level.WARNING, "Could not delete the neutrality request file {0}.yml", request.id());
        }
        MUTEX.unlock();
    }

    @Override
    public void removeAllFrom(Clan clan) {
        runAsync(MUTEX, plugin, () -> removeFilesStartingWith(clan.simpleTag()));
    }
}
