package foxsgr.clandestinos.application;

import clandestino.lib.TextUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class LanguageManager {

    public static final String NO_PERMISSION = "no-permission";
    public static final String WRONG_SIZE_TAG = "wrong-size-tag";
    public static final String WRONG_SIZE_NAME = "wrong-size-name";
    public static final String MUST_BE_PLAYER = "must-be-player";
    public static final String ONLY_LETTERS_TAG = "only-letters-tag";
    public static final String FORBIDDEN_TAG = "forbidden-tag";
    public static final String WRONG_CREATE_USAGE = "wrong-create-usage";
    public static final String CLAN_CREATED = "clan-created";
    public static final String CANNOT_IN_CLAN = "cannot-in-clan";
    public static final String TAG_ALREADY_EXISTS = "tag-already-exists";

    public static final String COMMANDS_HEADER = "commands-header";
    public static final String CREATE_USAGE = "create-usage";
    public static final String RELOAD_USAGE = "reload-usage";

    private JavaPlugin plugin;
    private Map<String, String> strings;

    private static LanguageManager instance;
    private static final String FILE_NAME = "language.yml";

    private LanguageManager(JavaPlugin plugin) {
        this.plugin = plugin;
        strings = new HashMap<>();
    }

    public static String get(String key) {
        return instance.strings.get(key);
    }

    static void init(JavaPlugin plugin) {
        instance = new LanguageManager(plugin);
        instance.init();
    }

    private void init() {
        FileConfiguration configuration = createFileConfiguration();
        save(configuration);
        load(configuration);
    }

    private void save(FileConfiguration configuration) {
        File languageFile = new File(plugin.getDataFolder(), FILE_NAME);
        if (languageFile.exists()) {
            return;
        }

        try {
            //noinspection ResultOfMethodCallIgnored
            languageFile.getParentFile().mkdirs();
            configuration.options().copyDefaults(true);
            configuration.save(languageFile);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save the language file!", e);
        }
    }

    private void load(FileConfiguration configuration) {
        File languageFile = new File(plugin.getDataFolder(), FILE_NAME);

        try {
            configuration.load(languageFile);

            Set<String> keys = configuration.getKeys(false);
            for (String key : keys) {
                String value = Objects.requireNonNull(configuration.getString(key));
                value = TextUtil.translateColoredText(value);
                strings.put(key, value);
            }

            setupStrings();
        } catch (IOException | InvalidConfigurationException e) {
            throw new IllegalStateException("Could not load the language file!", e);
        }
    }

    private void setupStrings() {
        // Replace {0} with the the proper value
        replaceString(WRONG_SIZE_TAG, "{0}", ConfigManager.getInt(ConfigManager.MIN_TAG_LENGTH));
        replaceString(WRONG_SIZE_TAG, "{1}", ConfigManager.getInt(ConfigManager.MAX_TAG_LENGTH));
        replaceString(WRONG_SIZE_NAME, "{0}", ConfigManager.getInt(ConfigManager.MIN_NAME_LENGTH));
        replaceString(WRONG_SIZE_NAME, "{1}", ConfigManager.getInt(ConfigManager.MAX_NAME_LENGTH));
    }

    private void replaceString(String key, String toReplace, Object replaceInto) {
        String value = strings.get(key);
        if (value == null) {
            throw new IllegalStateException(String.format("The string '%s' was not found in the language file!", key));
        }

        strings.put(key, value.replace(toReplace, replaceInto.toString()));
    }

    private static FileConfiguration createFileConfiguration() {
        FileConfiguration fileConfiguration = new YamlConfiguration();
        fileConfiguration.addDefault(NO_PERMISSION, "&cYou don't have permission to use that command.");
        fileConfiguration.addDefault(WRONG_SIZE_TAG, "&cThe tag must be between {0} and {1} characters long.");
        fileConfiguration.addDefault(WRONG_SIZE_NAME, "&cThe name must be between {0} and {1} characters long.");
        fileConfiguration.addDefault(MUST_BE_PLAYER, "&cOnly a player can perform that command!");
        fileConfiguration.addDefault(FORBIDDEN_TAG, "&cYou can't use that tag.");
        fileConfiguration.addDefault(ONLY_LETTERS_TAG, "&cThe tag can only contain letters.");
        fileConfiguration.addDefault(WRONG_CREATE_USAGE, "&bTo create a clan, use: &9/clan create (tag) (name)");
        fileConfiguration.addDefault(CLAN_CREATED, "&aThe clan {0} &awas created!");
        fileConfiguration.addDefault(CANNOT_IN_CLAN, "&cYou cannot use that command because you are in a clan.");
        fileConfiguration.addDefault(TAG_ALREADY_EXISTS, "&cA clan with that tag already exists.");

        fileConfiguration.addDefault(COMMANDS_HEADER, "&9Clan Commands");
        fileConfiguration.addDefault(CREATE_USAGE, "&b/clan create (tag) [name] - Create a clan.");
        fileConfiguration.addDefault(RELOAD_USAGE, "&b/clan reload - Reload configurations.");
        return fileConfiguration;
    }
}
