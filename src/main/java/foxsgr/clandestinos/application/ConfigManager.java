package foxsgr.clandestinos.application;

import foxsgr.clandestinos.application.listeners.ChatManager;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration manager. Manages the plugin's configuration file and its options.
 */
@SuppressWarnings("WeakerAccess")
public class ConfigManager {

    public static final String MIN_TAG_LENGTH = "min-tag-length";
    public static final String MAX_TAG_LENGTH = "max-tag-length";
    public static final String MIN_NAME_LENGTH = "min-name-length";
    public static final String MAX_NAME_LENGTH = "max-name-length";
    public static final String USE_UUIDS = "use-uuids";
    public static final String FORBIDDEN_TAGS = "forbidden-tags";
    public static final String CHAT_FORMAT = "chat-format";
    public static final String LEFT_OF_TAG = "left-of-tag";
    public static final String RIGHT_OF_TAG = "right-of-tag";
    public static final String MEMBER_DECORATION_COLOR = "member-decoration-color";
    public static final String LEADER_DECORATION_COLOR = "leader-decoration-color";
    public static final String DEFAULT_TAG_COLOR = "default-tag-color";
    public static final String CLAN_CHAT_FORMAT = "clan-chat-format";
    public static final String CREATE_CLAN_COST = "create-clan-cost";

    /**
     * The plugin.
     */
    private final JavaPlugin plugin;

    /**
     * The single class instance.
     */
    private static ConfigManager instance;

    /**
     * The name of the configuration file.
     */
    private static final String FILE_NAME = "config.yml";

    /**
     * Creates the config manager.
     *
     * @param plugin the plugin.
     */
    private ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        createDefaultConfig();
    }

    /**
     * Finds an integer in the configuration.
     *
     * @param key the key of the integer. (one of the constants)
     * @return the found integer.
     */
    public Integer getInt(String key) {
        return plugin.getConfig().getInt(key);
    }

    /**
     * Finds a string in the configuration.
     *
     * @param key the key of the string. (one of the constants)
     * @return the found string.
     */
    public String getString(String key) {
        return plugin.getConfig().getString(key);
    }

    /**
     * Finds a boolean in the configuration.
     *
     * @param key the key of the boolean. (one of the constants)
     * @return the found boolean.
     */
    public boolean getBoolean(String key) {
        return plugin.getConfig().getBoolean(key);
    }

    /**
     * Finds a string list in the configuration.
     *
     * @param key the key of the string list. (one of the constants)
     * @return the found string list.
     */
    public List<String> getStringList(String key) {
        return plugin.getConfig().getStringList(key);
    }

    /**
     * Finds a double in the configuration.
     *
     * @param key the key of the double. (one of the constants)
     * @return the found double.
     */
    public double getDouble(String key) {
        return plugin.getConfig().getDouble(key);
    }

    /**
     * Returns the (single) config manager instance.
     *
     * @return the config manager instance.
     */
    public static ConfigManager getInstance() {
        return instance;
    }

    /**
     * Loads the configuration.
     *
     * @param plugin the plugin.
     */
    static void init(JavaPlugin plugin) {
        instance = new ConfigManager(plugin);
        instance.save();
    }

    /**
     * Saves the configuration if it doesn't exist (from the default values).
     */
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

    /**
     * Loads the default configuration values.
     */
    private void createDefaultConfig() {
        Configuration config = plugin.getConfig();
        config.addDefault(MIN_TAG_LENGTH, 3);
        config.addDefault(MAX_TAG_LENGTH, 5);
        config.addDefault(MIN_NAME_LENGTH, 0);
        config.addDefault(MAX_NAME_LENGTH, 20);
        config.addDefault(USE_UUIDS, false);
        config.addDefault(FORBIDDEN_TAGS, Arrays.asList("mod", "admin", "owner", "vip", "mvp", "vip+", "dono", "staff",
                "helper", "mvp+"));

        // Must have at least "{player}" and "{content}"
        config.addDefault(CHAT_FORMAT, String.format("%s%s&f%s &f> &7%s", ChatManager.FORMATTED_CLAN_TAG_PLACEHOLDER,
                ChatManager.PREFIX_PLACEHOLDER, ChatManager.PLAYER_PLACEHOLDER,
                ChatManager.CONTENT_PLACEHOLDER));
        config.addDefault(CREATE_CLAN_COST, 1500);
        config.addDefault(LEFT_OF_TAG, "[");
        config.addDefault(RIGHT_OF_TAG, "]");
        config.addDefault(MEMBER_DECORATION_COLOR, "&7");
        config.addDefault(LEADER_DECORATION_COLOR, "&4");
        config.addDefault(DEFAULT_TAG_COLOR, "&7");
        config.addDefault(CLAN_CHAT_FORMAT,
                String.format("&7[&6Chat %s&7] &f%s &6> &e%s", ChatManager.COLORED_CLAN_TAG_PLACEHOLDER,
                        ChatManager.PLAYER_PLACEHOLDER, ChatManager.CONTENT_PLACEHOLDER));
        config.options().copyDefaults(true);
    }
}
