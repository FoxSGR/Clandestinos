package foxsgr.clandestinos.application;

import foxsgr.clandestinos.util.TextUtil;
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

    public static final String COMMANDS_HEADER = "commands-header";
    public static final String CREATE_USAGE = "create-usage";
    public static final String INVITE_USAGE = "invite-usage";
    public static final String RELOAD_USAGE = "reload-usage";
    public static final String LEAVE_USAGE = "leave-usage";

    public static final String WRONG_CREATE_USAGE = "wrong-create-usage";
    public static final String WRONG_INVITE_USAGE = "wrong-invite-usage";
    public static final String WRONG_JOIN_USAGE = "wrong-join-usage";
    public static final String DISBAND_USAGE = "disband-usage";

    public static final String NO_PERMISSION = "no-permission";
    public static final String WRONG_SIZE_TAG = "wrong-size-tag";
    public static final String WRONG_SIZE_NAME = "wrong-size-name";
    public static final String MUST_BE_PLAYER = "must-be-player";
    public static final String MUST_BE_LEADER = "must-be-leader";
    public static final String ONLY_LETTERS_TAG = "only-letters-tag";
    public static final String FORBIDDEN_TAG = "forbidden-tag";
    public static final String CLAN_CREATED = "clan-created";
    public static final String CANNOT_IN_CLAN = "cannot-in-clan";
    public static final String TAG_ALREADY_EXISTS = "tag-already-exists";
    public static final String MUST_BE_IN_CLAN = "must-be-in-clan";
    public static final String PLAYER_NOT_ONLINE = "player-not-online";
    public static final String PLAYER_INVITED = "player-invited";
    public static final String RECEIVED_INVITE = "received-invite";
    public static final String ALREADY_INVITED = "already-invited";
    public static final String ALREADY_IN_YOUR_CLAN = "already-in-your-clan";
    public static final String CLAN_DOESNT_EXIST = "clan-doesnt-exist";
    public static final String NOT_INVITED = "not-invited";
    public static final String JOINED_MESSAGE = "joined-message";
    public static final String UNKNOWN_COMMAND = "unknown-command";
    public static final String NO_MONEY_CREATE = "no-money-create";
    public static final String OWNER_CANT_LEAVE = "owner-cant-leave";
    public static final String MUST_BE_OWNER = "must-be-owner";
    public static final String CLAN_DISBANDED = "clan-disbanded";
    public static final String LEFT_CLAN = "left-clan";

    private JavaPlugin plugin;
    private Map<String, String> strings;

    private static LanguageManager instance;
    private static final String FILE_NAME = "language.yml";

    private LanguageManager(JavaPlugin plugin) {
        this.plugin = plugin;
        strings = new HashMap<>();
    }

    public String get(String key) {
        return strings.get(key);
    }

    public static LanguageManager getInstance() {
        return instance;
    }

    public static String placeholder(int index) {
        return String.format("{%d}", index);
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
        ConfigManager configManager = ConfigManager.getInstance();

        // Replace {0} with the the proper value
        replaceString(WRONG_SIZE_TAG, placeholder(0), configManager.getInt(ConfigManager.MIN_TAG_LENGTH));
        replaceString(WRONG_SIZE_TAG, placeholder(1), configManager.getInt(ConfigManager.MAX_TAG_LENGTH));
        replaceString(WRONG_SIZE_NAME, placeholder(0), configManager.getInt(ConfigManager.MIN_NAME_LENGTH));
        replaceString(WRONG_SIZE_NAME, placeholder(1), configManager.getInt(ConfigManager.MAX_NAME_LENGTH));
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

        fileConfiguration.addDefault(COMMANDS_HEADER, "&9Clan Commands");

        fileConfiguration.addDefault(CREATE_USAGE, "&b/clan create (tag) [name] - Create a clan.");
        fileConfiguration.addDefault(INVITE_USAGE, "&b/clan invite (player) - Invite a player to your clan.");
        fileConfiguration.addDefault(RELOAD_USAGE, "&b/clan reload - Reload configurations.");
        fileConfiguration.addDefault(LEAVE_USAGE, "&b/clan leave - Leave your clan.");
        fileConfiguration.addDefault(DISBAND_USAGE, "&b/clan disband - Disband your clan.");

        fileConfiguration.addDefault(WRONG_CREATE_USAGE, "&bTo create a clan, use: &9/clan create (tag) [name]");
        fileConfiguration.addDefault(WRONG_INVITE_USAGE, "&bTo invite a player, use: &9/clan invite (player)");
        fileConfiguration.addDefault(WRONG_JOIN_USAGE, "&bTo join a clan, use: &9/clan join (tag)");

        fileConfiguration.addDefault(NO_PERMISSION, "&cYou don't have permission to use that command.");
        fileConfiguration.addDefault(WRONG_SIZE_TAG, "&cThe tag must be between {0} and {1} characters long.");
        fileConfiguration.addDefault(WRONG_SIZE_NAME, "&cThe name must be between {0} and {1} characters long.");
        fileConfiguration.addDefault(MUST_BE_PLAYER, "&cOnly a player can perform that command!");
        fileConfiguration.addDefault(MUST_BE_LEADER, "&cYou must be a leader to use that command.");
        fileConfiguration.addDefault(FORBIDDEN_TAG, "&cYou can't use that tag.");
        fileConfiguration.addDefault(ONLY_LETTERS_TAG, "&cThe tag can only contain letters and color codes.");
        fileConfiguration.addDefault(CLAN_CREATED, "&aThe clan {0} &awas created!");
        fileConfiguration.addDefault(CANNOT_IN_CLAN, "&cYou cannot use that command because you are in a clan.");
        fileConfiguration.addDefault(TAG_ALREADY_EXISTS, "&cA clan with that tag already exists.");
        fileConfiguration.addDefault(MUST_BE_IN_CLAN, "&cYou must be in a clan to use that command.");
        fileConfiguration.addDefault(PLAYER_NOT_ONLINE, "&cThat player is not online.");
        fileConfiguration.addDefault(PLAYER_INVITED, "&aThe player {0} &ahas been invited to join your clan.");
        fileConfiguration.addDefault(RECEIVED_INVITE, "&aYou were invited to join the {0} &aclan. Use /clan join {1} &ato accept.");
        fileConfiguration.addDefault(ALREADY_INVITED, "&cThat player has already been invited to your clan.");
        fileConfiguration.addDefault(ALREADY_IN_YOUR_CLAN, "&cThat player is already in your clan.");
        fileConfiguration.addDefault(CLAN_DOESNT_EXIST, "&cThat clan does not exist.");
        fileConfiguration.addDefault(NOT_INVITED, "&cYou haven't been invited to that clan.");
        fileConfiguration.addDefault(JOINED_MESSAGE, "&a{0} &ajoined the {1} &aclan.");
        fileConfiguration.addDefault(UNKNOWN_COMMAND, "&cUnkown command. Type &b/clan &cfor a list of commands.");
        fileConfiguration.addDefault(NO_MONEY_CREATE, "&cYou don't have enough money to create a clan. You need at least {0}.");
        fileConfiguration.addDefault(OWNER_CANT_LEAVE, "&cYou are the owner of your clan. To leave your clan, you must use &b/clan makeowner (player) &cor &b/clan disband");
        fileConfiguration.addDefault(MUST_BE_OWNER, "&cOnly the owner of the clan can perform that command.");
        fileConfiguration.addDefault(CLAN_DISBANDED, "&aThe clan {0} &awas disbanded.");
        fileConfiguration.addDefault(LEFT_CLAN, "&a{0} &aleft {1}&a.");
        fileConfiguration.options().copyDefaults(true);
        return fileConfiguration;
    }
}
