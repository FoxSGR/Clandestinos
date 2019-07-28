package foxsgr.clandestinos.application;

import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The language/strings manager. Manages the language configuration file.
 */
@SuppressWarnings("WeakerAccess")
public class LanguageManager {

    public static final String COMMANDS_HEADER = "commands-header";
    public static final String CREATE_USAGE = "create-usage";
    public static final String INVITE_USAGE = "invite-usage";
    public static final String UNINVITE_USAGE = "uninvite-usage";
    public static final String RELOAD_USAGE = "reload-usage";
    public static final String LEAVE_USAGE = "leave-usage";
    public static final String DISBAND_USAGE = "disband-usage";
    public static final String INFO_USAGE = "info-usage";
    public static final String KICK_USAGE = "kick-usage";
    public static final String MODTAG_USAGE = "modtag-usage";
    public static final String ENEMY_USAGE = "enemy-usage";
    public static final String MAKE_LEADER_USAGE = "make-leader-usage";
    public static final String REMOVE_LEADER_USAGE = "remove-leader-usage";

    public static final String WRONG_CREATE_USAGE = "wrong-create-usage";
    public static final String WRONG_INVITE_USAGE = "wrong-invite-usage";
    public static final String WRONG_UNINVITE_USAGE = "wrong-uninvite-usage";
    public static final String WRONG_JOIN_USAGE = "wrong-join-usage";
    public static final String WRONG_INFO_USAGE = "wrong-info-usage";
    public static final String WRONG_KICK_USAGE = "wrong-kick-usage";
    public static final String WRONG_MODTAG_USAGE = "wrong-modtag-usage";
    public static final String WRONG_ENEMY_USAGE = "wrong-enemy-usage";
    public static final String WRONG_UNENEMY_USAGE = "wrong-unenemy-usage";
    public static final String WRONG_MAKE_LEADER_USAGE = "wrong-make-leader-usage";
    public static final String WRONG_DEMOTE_LEADER_USAGE = "wrong-demote-leader-usage";

    public static final String NO_PERMISSION = "no-permission";
    public static final String WRONG_SIZE_TAG = "wrong-size-tag";
    public static final String WRONG_SIZE_NAME = "wrong-size-name";
    public static final String MUST_BE_PLAYER = "must-be-player";
    public static final String MUST_BE_LEADER = "must-be-leader";
    public static final String MUST_BE_OWNER = "must-be-owner";
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
    public static final String CLAN_DISBANDED = "clan-disbanded";
    public static final String LEFT_CLAN = "left-clan";
    public static final String UNKNOWN_PLAYER = "unknown-player";
    public static final String UNKNOWN_PLAYER_CLAN = "unknown-player-clan";
    public static final String OWNER = "owner";
    public static final String LEADERS = "leaders";
    public static final String MEMBERS = "members";
    public static final String INFO = "info";
    public static final String KDR = "kdr";
    public static final String KILLS = "kills";
    public static final String DEATHS = "deaths";
    public static final String NAME = "name";
    public static final String MONEY_TAKEN = "money-taken";
    public static final String PLAYER = "player";
    public static final String CLAN = "clan";
    public static final String NOT_IN_YOUR_CLAN = "not-in-your-clan";
    public static final String ONLY_OWNER_KICK_LEADER = "only-owner-kick-leader";
    public static final String PLAYER_KICKED = "player-kicked";
    public static final String CANNOT_KICK_YOURSELF = "cannot-kick-yourself";
    public static final String CANNOT_PROMOTE_YOURSELF = "cannot-promote-yourself";
    public static final String CANNOT_PROMOTE_LEADER = "cannot-promote-leader";
    public static final String NO_INVITE_PENDING = "no-invite-pending";
    public static final String PLAYER_UNINVITED = "player-uninvited";
    public static final String YOU_WERE_UNINVITED = "you-were-uninvited";
    public static final String ONLY_CHANGE_COLORS = "only-change-colors";
    public static final String NEW_TAG_INFO = "new-tag-info";
    public static final String SPY_DISABLED = "spy-disabled";
    public static final String SPY_ENABLED = "spy-enabled";
    public static final String CHANGE_SAME_TAG = "change-same-tag";
    public static final String ALREADY_YOUR_ENEMY = "already-your-enemy";
    public static final String ENEMY_DECLARATION = "enemy-declaration";
    public static final String NOT_YOUR_ENEMY = "not-your-enemy";
    public static final String ALREADY_REQUESTED_NEUTRALITY = "already-requested-neutrality";
    public static final String SUCCESSFUL_PROMOTE = "successful-promote";
    public static final String PROMOTED = "promoted";
    public static final String CANNOT_DEMOTE_MEMBER = "cannot-demote-member";
    public static final String CANNOT_DEMOTE_YOURSELF = "cannot-demote-yourself";
    public static final String SUCCESSFUL_DEMOTE = "successful-demote";
    public static final String DEMOTED = "demoted";

    /**
     * The plugin.
     */
    private JavaPlugin plugin;

    /**
     * The loaded strings.
     */
    private Map<String, String> strings;

    /**
     * The class single instance.
     */
    private static LanguageManager instance;

    /**
     * The name of the file with the configuration.
     */
    private static final String FILE_NAME = "language.yml";

    /**
     * Creates the language manager.
     *
     * @param plugin the plugin.
     */
    private LanguageManager(JavaPlugin plugin) {
        this.plugin = plugin;
        strings = new HashMap<>();
    }

    /**
     * Finds a string given its key in the configuration file. (one of the constants)
     *
     * @param key the key of the string to find in the configuration file. (one of the constants)
     * @return the found string or null if it doesn't exist.
     */
    public String get(String key) {
        return strings.get(key);
    }

    /**
     * Returns the (single) language manager class instance.
     *
     * @return the (single) language manager class instance.
     */
    public static LanguageManager getInstance() {
        return instance;
    }

    /**
     * Sends a string to a command sender, finding it by its key. Also replaces placeholders in the string, if
     * provided.
     *
     * @param sender            the command sender to send the message to.
     * @param key               the key of the string. (one of the constants)
     * @param placeholderValues the placeholder values to replace (optional). The first will replace {0} and so on.
     */
    public static void send(CommandSender sender, String key, Object... placeholderValues) {
        String message = createMessage(key, placeholderValues);
        sender.sendMessage(message);
    }

    /**
     * Broadcasts a string, finding it by its key. Also replaces placeholders in the string, if provided.
     *
     * @param server            the server to broadcast the message in.
     * @param key               the key of the string. (one of the constants)
     * @param placeholderValues the placeholder values to replace (optional). The first will replace {0} and so on.
     */
    public static void broadcast(Server server, String key, Object... placeholderValues) {
        String message = createMessage(key, placeholderValues);
        server.broadcastMessage(message);
    }

    /**
     * Loads the language manager instance.
     *
     * @param plugin the plugin.
     */
    static void init(JavaPlugin plugin) {
        instance = new LanguageManager(plugin);
        instance.init();
    }

    /**
     * Loads the strings.
     */
    private void init() {
        FileConfiguration configuration = createFileConfiguration();
        save(configuration);
        load(configuration);
    }

    /**
     * Saves the file configuration if it doesn't exist.
     *
     * @param configuration the configuration to save.
     */
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

    /**
     * Loads the file configuration containing the strings.
     *
     * @param configuration the configuration to load to.
     */
    @SuppressWarnings("squid:CommentedOutCodeLine")
    private void load(FileConfiguration configuration) {
        File languageFile = new File(plugin.getDataFolder(), FILE_NAME);

        try {
            configuration.load(languageFile);

            Set<String> keys = configuration.getKeys(false);
            for (String key : keys) {
                String value = Objects.requireNonNull(configuration.getString(key));
                // value = TextUtil.translateColoredText(value); Commented because send already translates
                strings.put(key, value);
            }

            setupStrings();
        } catch (IOException | InvalidConfigurationException e) {
            throw new IllegalStateException("Could not load the language file!", e);
        }
    }

    /**
     * Replaces some placeholders acording to the plugin config.
     */
    private void setupStrings() {
        ConfigManager configManager = ConfigManager.getInstance();

        // Replace {0} with the the proper value
        replaceString(WRONG_SIZE_TAG, placeholder(0), configManager.getInt(ConfigManager.MIN_TAG_LENGTH));
        replaceString(WRONG_SIZE_TAG, placeholder(1), configManager.getInt(ConfigManager.MAX_TAG_LENGTH));
        replaceString(WRONG_SIZE_NAME, placeholder(0), configManager.getInt(ConfigManager.MIN_NAME_LENGTH));
        replaceString(WRONG_SIZE_NAME, placeholder(1), configManager.getInt(ConfigManager.MAX_NAME_LENGTH));
    }

    /**
     * Replaces a placeholder with a value in a loaded string.
     *
     * @param key         the key of the string to replace.
     * @param toReplace   the placeholder to replace.
     * @param replaceInto the value of the placeholder.
     */
    private void replaceString(String key, String toReplace, Object replaceInto) {
        String value = strings.get(key);
        if (value == null) {
            throw new IllegalStateException(String.format("The string '%s' was not found in the language file!", key));
        }

        strings.put(key, value.replace(toReplace, replaceInto.toString()));
    }

    /**
     * Creates a file configuration including the default values.
     *
     * @return the created file configuration.
     */
    private static FileConfiguration createFileConfiguration() {
        FileConfiguration fileConfiguration = new YamlConfiguration();

        fileConfiguration.addDefault(COMMANDS_HEADER, "&9Clan Commands");

        fileConfiguration.addDefault(CREATE_USAGE, "&b/clan create (tag) [name] - Create a clan.");
        fileConfiguration.addDefault(INVITE_USAGE, "&b/clan invite (name) - Invite a player to your clan.");
        fileConfiguration.addDefault(INVITE_USAGE, "&b/clan uninvite (name) - Cancel an invite.");
        fileConfiguration.addDefault(RELOAD_USAGE, "&b/clan reload - Reload configurations.");
        fileConfiguration.addDefault(LEAVE_USAGE, "&b/clan leave - Leave your clan.");
        fileConfiguration.addDefault(DISBAND_USAGE, "&b/clan disband - Disband your clan.");
        fileConfiguration.addDefault(INFO_USAGE,
                "&b/clan info [clan/player] (tag/player name) - Show clan/player information.");
        fileConfiguration.addDefault(KICK_USAGE, "&b/clan kick (name) - Kick a player from your clan.");
        fileConfiguration.addDefault(MODTAG_USAGE, "&b/clan modtag (newtag) - Change the colors of your clan tag.");
        fileConfiguration.addDefault(ENEMY_USAGE, "&b/clan enemy (tag) - Declare that a clan is your enemy.");
        fileConfiguration.addDefault(MAKE_LEADER_USAGE, "&b/clan makeleader (player) - Promote a player to leader.");
        fileConfiguration.addDefault(REMOVE_LEADER_USAGE, "&b/clan removeleader (player) - Demote a player from leader.");

        fileConfiguration.addDefault(WRONG_CREATE_USAGE, "&cTo create a clan, use: &b/clan create (tag) [name]");
        fileConfiguration.addDefault(WRONG_INVITE_USAGE, "&cTo invite a player, use: &b/clan invite (player)");
        fileConfiguration.addDefault(WRONG_UNINVITE_USAGE, "&cTo cancel an invite, use: &b/clan uninvite (player)");
        fileConfiguration.addDefault(WRONG_JOIN_USAGE, "&cTo join a clan, use: &b/clan join (tag)");
        fileConfiguration.addDefault(WRONG_INFO_USAGE,
                "&cTo show clan/player information, use: &b/clan info [clan/player] (tag/player name)");
        fileConfiguration.addDefault(WRONG_KICK_USAGE, "&cTo kick a player from your clan, use: &b/clan kick (player)");
        fileConfiguration.addDefault(WRONG_MODTAG_USAGE,
                "&cTo change the colors of your tag, use: &b/clan modtag (newtag)");
        fileConfiguration.addDefault(WRONG_ENEMY_USAGE, "&cTo declare a clan as your enemy, use: &b/clan enemy (tag)");
        fileConfiguration.addDefault(WRONG_UNENEMY_USAGE, "&cTo request neutrality, use: &b/clan unenemy (tag)");
        fileConfiguration.addDefault(WRONG_MAKE_LEADER_USAGE, "&cTo promote a player from your clan, use:  &b/clan makeleader (player)");
        fileConfiguration.addDefault(WRONG_DEMOTE_LEADER_USAGE, "&cTo depromote a player from your clan, use:  &b/clan removeleader (player)");

        fileConfiguration.addDefault(NO_PERMISSION, "&cYou don't have permission to use that command.");
        fileConfiguration.addDefault(WRONG_SIZE_TAG, "&cThe tag must be between {0} and {1} characters long.");
        fileConfiguration.addDefault(WRONG_SIZE_NAME, "&cThe name must be between {0} and {1} characters long.");
        fileConfiguration.addDefault(MUST_BE_OWNER, "&cOnly the owner of the clan can perform that command.");
        fileConfiguration.addDefault(MUST_BE_PLAYER, "&cOnly a player can perform that command!");
        fileConfiguration.addDefault(MUST_BE_LEADER, "&cYou must be a leader to use that command.");
        fileConfiguration.addDefault(FORBIDDEN_TAG, "&cYou can't use that tag.");
        fileConfiguration.addDefault(ONLY_LETTERS_TAG, "&cThe tag can only contain letters and color codes.");
        fileConfiguration.addDefault(CLAN_CREATED, "&aThe clan {0} &awas created!");
        fileConfiguration.addDefault(CANNOT_IN_CLAN, "&cYou cannot use that command because you are in a clan.");
        fileConfiguration.addDefault(TAG_ALREADY_EXISTS, "&cA clan with that tag already exists.");
        fileConfiguration.addDefault(MUST_BE_IN_CLAN, "&cYou must be in a clan to use that command.");
        fileConfiguration.addDefault(PLAYER_NOT_ONLINE, "&cThat player was not found.");
        fileConfiguration.addDefault(PLAYER_INVITED, "&aThe player {0} &ahas been invited to join your clan.");
        fileConfiguration.addDefault(RECEIVED_INVITE,
                "&aYou were invited to join the {0} &aclan. Use /clan join {1} &ato accept.");
        fileConfiguration.addDefault(ALREADY_INVITED, "&cThat player has already been invited to your clan.");
        fileConfiguration.addDefault(ALREADY_IN_YOUR_CLAN, "&cThat player is already in your clan.");
        fileConfiguration.addDefault(CLAN_DOESNT_EXIST, "&cThat clan does not exist.");
        fileConfiguration.addDefault(NOT_INVITED, "&cYou haven't been invited to that clan.");
        fileConfiguration.addDefault(JOINED_MESSAGE, "&a{0} &ajoined the {1} &aclan.");
        fileConfiguration.addDefault(UNKNOWN_COMMAND, "&cUnkown command. Type &b/clan &cfor a list of commands.");
        fileConfiguration.addDefault(NO_MONEY_CREATE,
                "&cYou don't have enough money to create a clan. You need at least {0}.");
        fileConfiguration.addDefault(OWNER_CANT_LEAVE,
                "&cYou are the owner of your clan. To leave your clan, you must use &b/clan makeowner (player) &cor &b/clan disband");
        fileConfiguration.addDefault(CLAN_DISBANDED, "&aThe clan {0} &awas disbanded.");
        fileConfiguration.addDefault(LEFT_CLAN, "&a{0} &aleft {1}&a.");
        fileConfiguration.addDefault(UNKNOWN_PLAYER, "&cThat player is not in the clan database.");
        fileConfiguration.addDefault(UNKNOWN_PLAYER_CLAN, "&cThat player or clan is not in the clan database.");
        fileConfiguration.addDefault(INFO, "&bInfo");
        fileConfiguration.addDefault(OWNER, "&bOwner:&f");
        fileConfiguration.addDefault(LEADERS, "&bLeaders:&f");
        fileConfiguration.addDefault(MEMBERS, "&bMembers:&f");
        fileConfiguration.addDefault(KDR, "&bKDR:&f");
        fileConfiguration.addDefault(KILLS, "&bKills:&f");
        fileConfiguration.addDefault(DEATHS, "&bDeaths:&f");
        fileConfiguration.addDefault(NAME, "&bName:&f");
        fileConfiguration.addDefault(CLAN, "&bClan:&f");
        fileConfiguration.addDefault(MONEY_TAKEN, "&a{0} has been removed from your account.");
        fileConfiguration.addDefault(PLAYER, "&bPlayer&f");
        fileConfiguration.addDefault(NOT_IN_YOUR_CLAN, "&cThat player is not in your clan.");
        fileConfiguration.addDefault(ONLY_OWNER_KICK_LEADER, "&cOnly the owner of the clan can kick a leader.");
        fileConfiguration.addDefault(PLAYER_KICKED, "&a{0} &awas kicked out of {1}&a.");
        fileConfiguration.addDefault(CANNOT_KICK_YOURSELF,
                "&cYou can't kick yourself. Use &b/clan leave &cto leave your clan or &b/clan disband &cif you're the owner.");
        fileConfiguration.addDefault(NO_INVITE_PENDING, "&cThat player doesn't have a pending invite from your clan.");
        fileConfiguration.addDefault(PLAYER_UNINVITED, "&a{0} &ais no longer invited to your clan.");
        fileConfiguration.addDefault(YOU_WERE_UNINVITED, "&aYou are no longer invited to {0}&a.");
        fileConfiguration.addDefault(ONLY_CHANGE_COLORS, "&cYou can only change the tag's colors and letter case.");
        fileConfiguration.addDefault(NEW_TAG_INFO, "{0} &ais now known as {1}&a.");
        fileConfiguration.addDefault(SPY_DISABLED, "&bClan chat spy disabled.");
        fileConfiguration.addDefault(SPY_ENABLED, "&bClan chat spy enabled.");
        fileConfiguration.addDefault(CHANGE_SAME_TAG, "{0} &calready is your clan tag.");
        fileConfiguration.addDefault(ALREADY_YOUR_ENEMY, "{0} &cis already your enemy.");
        fileConfiguration.addDefault(ENEMY_DECLARATION, "{0} &adeclared {1} &aas their enemy.");
        fileConfiguration.addDefault(NOT_YOUR_ENEMY, "&cThat clan is not your enemy.");
        fileConfiguration.addDefault(ALREADY_REQUESTED_NEUTRALITY, "&cYou have already requested neutrality to clan.");
        fileConfiguration.addDefault(CANNOT_PROMOTE_LEADER, "&cThat player is already a leader.");
        fileConfiguration.addDefault(CANNOT_PROMOTE_YOURSELF, "&cYou cannot promote yourself.");
        fileConfiguration.addDefault(SUCCESSFUL_PROMOTE, "&b{0} is now a leader.");
        fileConfiguration.addDefault(PROMOTED, "&bYou are now a leader of your clan.");
        fileConfiguration.addDefault(CANNOT_DEMOTE_MEMBER, "&c{0} isn't a leader.");
        fileConfiguration.addDefault(CANNOT_DEMOTE_YOURSELF, "&cYou cannot demote yourself.");
        fileConfiguration.addDefault(SUCCESSFUL_DEMOTE, "&b{0} is no longer a leader.");
        fileConfiguration.addDefault(DEMOTED, "&bYou are no longer a leader of your clan.");

        fileConfiguration.options().copyDefaults(true);
        return fileConfiguration;
    }

    /**
     * Creates the string that matches a placeholder with a given index.
     *
     * @param index the index of the placeholder.
     * @return the string that matches the placeholder.
     */
    private static String placeholder(int index) {
        return String.format("{%d}", index);
    }

    /**
     * Creates a message, given a string key and, optionally, the placeholders to replace.
     *
     * @param key               the key of the string to find.
     * @param placeholderValues the placeholder values to replace (optional). The first will replace {0} and so on.
     * @return the created message.
     */
    @NotNull
    private static String createMessage(String key, Object... placeholderValues) {
        String message = instance.get(key);
        if (message == null) {
            return "null";
        }

        for (int i = 0; i < placeholderValues.length; i++) {
            String value = String.valueOf(placeholderValues[i]);
            message = message.replace(placeholder(i), value);
        }

        return TextUtil.translateColoredText(message);
    }
}
