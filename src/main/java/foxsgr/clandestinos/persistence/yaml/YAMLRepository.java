package foxsgr.clandestinos.persistence.yaml;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

class YAMLRepository {

    private File configurationFile;
    private static final String FOLDER = "storage";
    private static final String EXTENSION = ".yml";

    YAMLRepository(JavaPlugin plugin, @NotNull String fileName) {
        if (!fileName.endsWith(EXTENSION)) {
            fileName += EXTENSION;
        }

        fileName = FOLDER + File.separator + fileName;
        configurationFile = new File(plugin.getDataFolder(), fileName);
    }

    ConfigurationSection section(String name) {
        FileConfiguration fileConfiguration = load();
        return fileConfiguration.getConfigurationSection(name);
    }

    void update(FileConfiguration newConfiguration) {
        try {
            newConfiguration.save(configurationFile);
        } catch (IOException e) {
            throw new IllegalStateException("Could not update the persistence configuration file!", e);
        }
    }

    FileConfiguration load() {
        FileConfiguration fileConfiguration = new YamlConfiguration();
        if (!configurationFile.exists()) {
            return fileConfiguration;
        }

        try {
            fileConfiguration.load(configurationFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new IllegalStateException("Could not load the storage file " + configurationFile.getName() + "!", e);
        }

        return fileConfiguration;
    }
}
