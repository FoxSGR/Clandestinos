package foxsgr.clandestinos.application;

import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    public static final String MIN_TAG_LENGTH = "min-tag-length";
    public static final String MAX_TAG_LENGTH = "max-tag-length";
    public static final String MIN_NAME_LENGTH = "min-name-length";
    public static final String MAX_NAME_LENGTH = "max-name-length";
    public static final String ONLINE_MODE = "online-mode";
    public static final String FORBIDDEN_TAGS = "forbidden-tags";
    public static final String CHAT_FORMAT = "chat-format";
    public static final String CLAN_FORMAT = "clan-format";
    public static final String CREATE_CLAN_COST = "create-clan-cost";

    private final JavaPlugin plugin;

    private static ConfigManager instance;
    private static final String FILE_NAME = "config.yml";

    private ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        createDefaultConfig();
    }

    public Integer getInt(String key) {
        return plugin.getConfig().getInt(key);
    }

    public String getString(String key) {
        return plugin.getConfig().getString(key);
    }

    public boolean getBoolean(String key) {
        return plugin.getConfig().getBoolean(key);
    }

    public List<String> getStringList(String key) {
        return plugin.getConfig().getStringList(key);
    }

    public double getDouble(String key) {
        return plugin.getConfig().getDouble(key);
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    static void init(JavaPlugin plugin) {
        instance = new ConfigManager(plugin);
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
        config.addDefault(MIN_NAME_LENGTH, 0);
        config.addDefault(MAX_NAME_LENGTH, 20);
        config.addDefault(ONLINE_MODE, false); // TODO: Change if plugin gets published
        config.addDefault(FORBIDDEN_TAGS, new ArrayList<>());

        // Must have at least "{player}" and "{content}"
        config.addDefault(CHAT_FORMAT, String.format("%s%s&f%s &f> &7%s", ChatManager.FORMATTED_CLAN_TAG_PLACEHOLDER,
                ChatManager.PREFIX_PLACEHOLDER, ChatManager.PLAYER_PLACEHOLDER,
                ChatManager.CONTENT_PLACEHOLDER));
        config.addDefault(CLAN_FORMAT, "&8[" + ChatManager.COLORED_CLAN_TAG_PLACEHOLDER + "&8] ");
        config.addDefault(CREATE_CLAN_COST, 1500);
        config.options().copyDefaults(true);
    }
}
