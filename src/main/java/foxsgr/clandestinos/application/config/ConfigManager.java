package foxsgr.clandestinos.application.config;

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
public class ConfigManager {

    private static final String ENABLED_MODULE = "enabled";

    private static final String GENERAL_CATEGORY = "general.";
    public static final String MIN_TAG_LENGTH = GENERAL_CATEGORY + "min-tag-length";
    public static final String MAX_TAG_LENGTH = GENERAL_CATEGORY + "max-tag-length";
    public static final String MIN_NAME_LENGTH = GENERAL_CATEGORY + "min-name-length";
    public static final String MAX_NAME_LENGTH = GENERAL_CATEGORY + "max-name-length";
    public static final String FORBIDDEN_TAGS = GENERAL_CATEGORY + "forbidden-tags";
    public static final String DEFAULT_TAG_COLOR = GENERAL_CATEGORY + "default-tag-color";

    private static final String GAMEPLAY_CATEGORY = "gameplay.";
    public static final String TURN_ENEMY_ON_KILL = "turn-enemy-on-kill";

    public static final String CREATE_CLAN_COST = "economy.create-clan-cost";

    public static final String USE_UUIDS = "storage.use-uuids";

    private static final String CHAT_CATEGORY = "chat-formatting.";
    public static final String CHAT_FORMATTING_ENABLED = CHAT_CATEGORY + ENABLED_MODULE;
    public static final String CHAT_FORMAT = CHAT_CATEGORY + "format";

    private static final String TAGS_CATEGORY = "tag-formatting.";
    public static final String LEFT_OF_TAG = TAGS_CATEGORY + "left-of-tag";
    public static final String RIGHT_OF_TAG = TAGS_CATEGORY + "right-of-tag";
    public static final String MEMBER_DECORATION_COLOR = TAGS_CATEGORY + "member-decoration-color";
    public static final String LEADER_DECORATION_COLOR = TAGS_CATEGORY + "leader-decoration-color";

    private static final String CLAN_CHAT_CATEGORY = "clan-chat.";
    public static final String CLAN_CHAT_ENABLED = CLAN_CHAT_CATEGORY + ENABLED_MODULE;
    public static final String CLAN_CHAT_FORMAT = CLAN_CHAT_CATEGORY + "format";

    private static final String ANTI_SPAWN_KILL_CATEGORY = "anti-spawn-kill.";
    public static final String ANTI_SPAWN_KILL_ENABLED = ANTI_SPAWN_KILL_CATEGORY + ENABLED_MODULE;
    public static final String ANTI_SPAWN_KILL_PERIOD = ANTI_SPAWN_KILL_CATEGORY + "period";
    public static final String ANTI_SPAWN_KILL_MAX = ANTI_SPAWN_KILL_CATEGORY + "max-kills-in-period";
    public static final String ANTI_SPAWN_KILL_AREA = ANTI_SPAWN_KILL_CATEGORY + "area";

    private static final String MYSQL_CATEGORY = "mysql.";
    public static final String MYSQL_ENABLED = MYSQL_CATEGORY + ENABLED_MODULE;
    public static final String MYSQL_HOST = MYSQL_CATEGORY + "host";
    public static final String MYSQL_PORT = MYSQL_CATEGORY + "port";
    public static final String MYSQL_DATABASE = MYSQL_CATEGORY + "database";
    public static final String MYSQL_USERNAME = MYSQL_CATEGORY + "username";
    public static final String MYSQL_PASSWORD = MYSQL_CATEGORY + "password";

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
    public static void init(JavaPlugin plugin) {
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
        config.addDefault(CREATE_CLAN_COST, 1500);

        config.addDefault(CHAT_FORMATTING_ENABLED, true);
        // Must have at least "{player}" and "{content}"
        config.addDefault(CHAT_FORMAT, String.format("%s%s&f%s &f> &7%s", ChatManager.FORMATTED_CLAN_TAG_PLACEHOLDER,
                ChatManager.PREFIX_PLACEHOLDER, ChatManager.PLAYER_PLACEHOLDER,
                ChatManager.CONTENT_PLACEHOLDER));
        config.addDefault(LEFT_OF_TAG, "[");
        config.addDefault(RIGHT_OF_TAG, "]");
        config.addDefault(MEMBER_DECORATION_COLOR, "&7");
        config.addDefault(LEADER_DECORATION_COLOR, "&4");
        config.addDefault(DEFAULT_TAG_COLOR, "&7");

        config.addDefault(TURN_ENEMY_ON_KILL, true);

        config.addDefault(CLAN_CHAT_ENABLED, true);
        config.addDefault(CLAN_CHAT_FORMAT,
                String.format("&7[&6Chat %s&7] &f%s &6> &e%s", ChatManager.COLORED_CLAN_TAG_PLACEHOLDER,
                        ChatManager.PLAYER_PLACEHOLDER, ChatManager.CONTENT_PLACEHOLDER));

        config.addDefault(ANTI_SPAWN_KILL_ENABLED, true);
        config.addDefault(ANTI_SPAWN_KILL_PERIOD, 90);
        config.addDefault(ANTI_SPAWN_KILL_MAX, 4);
        config.addDefault(ANTI_SPAWN_KILL_AREA, 3);

        config.addDefault(MYSQL_ENABLED, false);
        config.addDefault(MYSQL_HOST, "localhost");
        config.addDefault(MYSQL_PORT, "3306");
        config.addDefault(MYSQL_DATABASE, "clandestinos");
        config.addDefault(MYSQL_USERNAME, "root");
        config.addDefault(MYSQL_PASSWORD, "root");

        config.options().copyDefaults(true);
    }
}
