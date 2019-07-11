package foxsgr.clandestinos.persistence.yaml;

import foxsgr.clandestinos.persistence.ClanPlayerRepository;
import foxsgr.clandestinos.persistence.ClanRepository;
import foxsgr.clandestinos.persistence.InviteRepository;
import foxsgr.clandestinos.persistence.RepositoryFactory;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class YAMLRepositoryFactory implements RepositoryFactory {

    private ClanPlayerRepository clanPlayerRepository;
    private ClanRepository clanRepository;

    private static final String STORAGE_FILE = "storage.yml";

    public YAMLRepositoryFactory(@NotNull JavaPlugin plugin) {
        File configurationFile = new File(plugin.getDataFolder(), STORAGE_FILE);
        FileConfiguration fileConfiguration = loadStorageFile(configurationFile);

        clanPlayerRepository = new ClanPlayerRepositoryYAML(configurationFile, fileConfiguration);
        clanRepository = new ClanRepositoryYAML(configurationFile, fileConfiguration);
    }

    @Override
    @NotNull
    public ClanPlayerRepository players() {
        return clanPlayerRepository;
    }

    @Override
    @NotNull
    public ClanRepository clans() {
        return clanRepository;
    }

    @Override
    public @NotNull InviteRepository invites() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    private static FileConfiguration loadStorageFile(@NotNull File configurationFile) {
        if (!configurationFile.exists()) {
            createStorageFile(configurationFile);
        }

        try {
            FileConfiguration fileConfiguration = new YamlConfiguration();
            fileConfiguration.load(configurationFile);
            return fileConfiguration;
        } catch (IOException | InvalidConfigurationException e) {
            throw new IllegalStateException("Could not load the storage file!", e);
        }
    }

    private static void createStorageFile(@NotNull File configurationFile) {
        FileConfiguration fileConfiguration = new YamlConfiguration();
        configurationFile.getParentFile().mkdirs();
        try {
            fileConfiguration.save(configurationFile);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create the storage file!", e);
        }
    }
}
