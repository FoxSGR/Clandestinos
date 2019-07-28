package foxsgr.clandestinos.persistence.yaml;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class YAMLRepository {

    final File repositoryFolder;
    final JavaPlugin plugin;
    private static final String STORAGE_FOLDER = "storage";
    private static final String EXTENSION = ".yml";

    YAMLRepository(JavaPlugin plugin, @NotNull String folder) {
        File file = new File(plugin.getDataFolder(), STORAGE_FOLDER);
        this.plugin = plugin;
        this.repositoryFolder = new File(file, folder);
    }

    Logger logger() {
        return plugin.getLogger();
    }

    @Nullable
    FileConfiguration file(String name) {
        FileConfiguration fileConfiguration = new YamlConfiguration();
        File file = makeFile(name);
        if (!file.exists()) {
            return null;
        }

        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new IllegalStateException("Could not read YAML file.", e);
        }

        return fileConfiguration;
    }

    void saveFile(FileConfiguration newConfiguration, String fileName) {
        try {
            newConfiguration.save(makeFile(fileName));
        } catch (IOException e) {
            throw new IllegalStateException("Could not update the persistence configuration file!", e);
        }
    }

    @NotNull
    FileConfiguration loadFile(String fileName) {
        FileConfiguration fileConfiguration = new YamlConfiguration();
        File file = makeFile(fileName);
        if (!file.exists()) {
            return fileConfiguration;
        }

        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new IllegalStateException("Could not load the storage file " + fileName + ".", e);
        }

        return fileConfiguration;
    }

    @NotNull
    File makeFile(String name) {
        if (!name.endsWith(EXTENSION)) {
            name += EXTENSION;
        }

        return new File(repositoryFolder, name);
    }

    @NotNull
    File[] listFiles() {
        File[] list = repositoryFolder.listFiles();
        if (list == null) {
            list = new File[0];
        }

        return list;
    }

    public void removeFilesStartingWith(String content) {
        File[] files = repositoryFolder.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.getName().startsWith(content) && !file.delete()) {
                logger().log(Level.WARNING, "Could not delete the invite file {0}.yml", file.getName());
            }
        }
    }
}
