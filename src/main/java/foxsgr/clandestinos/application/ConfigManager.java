package foxsgr.clandestinos.application;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ConfigManager {

    public static final String MIN_TAG_LENGTH = "min-tag-length";
    public static final String MAX_TAG_LENGTH = "max-tag-length";
    public static final String MIN_NAME_LENGTH = "min-name-length";
    public static final String MAX_NAME_LENGTH = "max-name-length";
    public static final String ONLINE_MODE = "online-mode";
    public static final String FORBIDDEN_TAGS = "forbidden-tags";

    private final JavaPlugin plugin;

    private static ConfigManager instance;
    private static final String FILE_NAME = "config.yml";

    private ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        createDefaultConfig();
    }

    public static Integer getInt(String key) {
        return instance.plugin.getConfig().getInt(key);
    }

    public static String getString(String key) {
        return instance.plugin.getConfig().getString(key);
    }

    public static boolean getBoolean(String key) {
        return instance.plugin.getConfig().getBoolean(key);
    }

    public static List<String> getStringList(String key) {
        return instance.plugin.getConfig().getStringList(key);
    }

    static void init(JavaPlugin plugin) {
        instance = new ConfigManager(plugin);
        plugin.reloadConfig();
        instance.save();
    }

    private void save() {
        File configurationFile = new File(plugin.getDataFolder(), FILE_NAME);
        if (configurationFile.exists()) {
            return;
        }

        try {
            plugin.getConfig().save(configurationFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save the config file.");
        }
    }

    private void createDefaultConfig() {
        Configuration config = plugin.getConfig();
        config.addDefault(MIN_TAG_LENGTH, 3);
        config.addDefault(MAX_TAG_LENGTH, 5);
        config.addDefault(MIN_NAME_LENGTH, 3);
        config.addDefault(MAX_NAME_LENGTH, 10);
        config.addDefault(ONLINE_MODE, true);
        config.addDefault(FORBIDDEN_TAGS, new ArrayList<>());
        config.options().copyDefaults(true);
    }
}
