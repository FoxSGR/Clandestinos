package foxsgr.clandestinos.persistence.yaml;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

class YAMLRepository {

    private File configurationFile;
    private FileConfiguration fileConfiguration;
    private String section;

    YAMLRepository(@NotNull File configurationFile, @NotNull FileConfiguration fileConfiguration, @NotNull String section) {
        this.configurationFile = configurationFile;
        this.fileConfiguration = fileConfiguration;
        this.section = section;

        if (fileConfiguration.getConfigurationSection(section) == null) {
            fileConfiguration.createSection(section);
        }
    }

    @NotNull
    ConfigurationSection section() {
        ConfigurationSection repoSection = fileConfiguration.getConfigurationSection(section);
        if (repoSection == null) {
            repoSection = fileConfiguration.createSection(section);
        }

        return repoSection;
    }

    @Nullable
    ConfigurationSection subSection(String name) {
        ConfigurationSection repoSection = section();
        return repoSection.getConfigurationSection(name);
    }

    void update() {
        try {
            fileConfiguration.save(configurationFile);
        } catch (IOException e) {
            throw new IllegalStateException("Could not update the persistence configuration file!", e);
        }
    }
}
