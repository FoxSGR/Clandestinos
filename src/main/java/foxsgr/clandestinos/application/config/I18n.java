package foxsgr.clandestinos.application.config;

import foxsgr.clandestinos.util.TextUtil;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
public class I18n {

    private static final String USAGE_CATEGORY = "usage.";
    public static final String COMMANDS_HEADER = USAGE_CATEGORY + "commands-header";
    public static final String CREATE_USAGE = USAGE_CATEGORY + "create";
    public static final String INVITE_USAGE = USAGE_CATEGORY + "invite";
    public static final String UNINVITE_USAGE = USAGE_CATEGORY + "uninvite";
    public static final String RELOAD_USAGE = USAGE_CATEGORY + "reload";
    public static final String LEAVE_USAGE = USAGE_CATEGORY + "leave";
    public static final String DISBAND_USAGE = USAGE_CATEGORY + "disband";
    public static final String INFO_USAGE = USAGE_CATEGORY + "info";
    public static final String KICK_USAGE = USAGE_CATEGORY + "kick";
    public static final String MODTAG_USAGE = USAGE_CATEGORY + "modtag";
    public static final String ENEMY_USAGE = USAGE_CATEGORY + "enemy";
    public static final String UNENEMY_USAGE = USAGE_CATEGORY + "unenemy";
    public static final String MAKE_LEADER_USAGE = USAGE_CATEGORY + "make-leader";
    public static final String REMOVE_LEADER_USAGE = USAGE_CATEGORY + "remove-leader";
    public static final String LIST_USAGE = USAGE_CATEGORY + "list";
    public static final String FF_USAGE = USAGE_CATEGORY + "friendly-fire";
    public static final String CLAN_FF_USAGE = USAGE_CATEGORY + "clan-friendly-fire";

    private static final String WRONG_USAGE_CATEGORY = "wrong-usage.";
    public static final String WRONG_CREATE_USAGE = WRONG_USAGE_CATEGORY + "create";
    public static final String WRONG_INVITE_USAGE = WRONG_USAGE_CATEGORY + "invite";
    public static final String WRONG_UNINVITE_USAGE = WRONG_USAGE_CATEGORY + "uninvite";
    public static final String WRONG_JOIN_USAGE = WRONG_USAGE_CATEGORY + "join";
    public static final String WRONG_INFO_USAGE = WRONG_USAGE_CATEGORY + "info";
    public static final String WRONG_KICK_USAGE = WRONG_USAGE_CATEGORY + "kick";
    public static final String WRONG_MODTAG_USAGE = WRONG_USAGE_CATEGORY + "modtag";
    public static final String WRONG_ENEMY_USAGE = WRONG_USAGE_CATEGORY + "enemy";
    public static final String WRONG_UNENEMY_USAGE = WRONG_USAGE_CATEGORY + "unenemy";
    public static final String WRONG_MAKE_LEADER_USAGE = WRONG_USAGE_CATEGORY + "make-leader";
    public static final String WRONG_DEMOTE_LEADER_USAGE = WRONG_USAGE_CATEGORY + "demote-leader";
    public static final String WRONG_LIST_USAGE = WRONG_USAGE_CATEGORY + "list";

    private static final String ERRORS_CATEGORY = "errors.";
    public static final String NO_PERMISSION = ERRORS_CATEGORY + "no-permission";
    public static final String WRONG_SIZE_TAG = ERRORS_CATEGORY + "wrong-size-tag";
    public static final String WRONG_SIZE_NAME = ERRORS_CATEGORY + "wrong-size-name";
    public static final String MUST_BE_PLAYER = ERRORS_CATEGORY + "must-be-player";
    public static final String MUST_BE_LEADER = ERRORS_CATEGORY + "must-be-leader";
    public static final String MUST_BE_OWNER = ERRORS_CATEGORY + "must-be-owner";
    public static final String ONLY_LETTERS_TAG = ERRORS_CATEGORY + "only-letters-tag";
    public static final String FORBIDDEN_TAG = ERRORS_CATEGORY + "forbidden-tag";
    public static final String CANNOT_IN_CLAN = ERRORS_CATEGORY + "cannot-in-clan";
    public static final String TAG_ALREADY_EXISTS = ERRORS_CATEGORY + "tag-already-exists";
    public static final String MUST_BE_IN_CLAN = ERRORS_CATEGORY + "must-be-in-clan";
    public static final String PLAYER_NOT_ONLINE = ERRORS_CATEGORY + "player-not-online";
    public static final String ALREADY_INVITED = ERRORS_CATEGORY + "already-invited";
    public static final String ALREADY_IN_YOUR_CLAN = ERRORS_CATEGORY + "already-in-your-clan";
    public static final String NOT_INVITED = ERRORS_CATEGORY + "not-invited";
    public static final String CLAN_DOESNT_EXIST = ERRORS_CATEGORY + "clan-doesnt-exist";
    public static final String UNKNOWN_COMMAND = ERRORS_CATEGORY + "unknown-command";
    public static final String NO_MONEY_CREATE = ERRORS_CATEGORY + "no-money-create";
    public static final String OWNER_CANT_LEAVE = ERRORS_CATEGORY + "owner-cant-leave";
    public static final String UNKNOWN_PLAYER = ERRORS_CATEGORY + "unknown-player";
    public static final String UNKNOWN_PLAYER_CLAN = ERRORS_CATEGORY + "unknown-player-clan";
    public static final String NOT_IN_YOUR_CLAN = ERRORS_CATEGORY + "not-in-your-clan";
    public static final String ONLY_OWNER_KICK_LEADER = ERRORS_CATEGORY + "only-owner-kick-leader";
    public static final String CANNOT_KICK_YOURSELF = ERRORS_CATEGORY + "cannot-kick-yourself";
    public static final String CANNOT_PROMOTE_YOURSELF = ERRORS_CATEGORY + "cannot-promote-yourself";
    public static final String CANNOT_PROMOTE_LEADER = ERRORS_CATEGORY + "cannot-promote-leader";
    public static final String NO_INVITE_PENDING = ERRORS_CATEGORY + "no-invite-pending";
    public static final String ONLY_CHANGE_COLORS = ERRORS_CATEGORY + "only-change-colors";
    public static final String ALREADY_YOUR_ENEMY = ERRORS_CATEGORY + "already-your-enemy";
    public static final String CHANGE_SAME_TAG = ERRORS_CATEGORY + "change-same-tag";
    public static final String NOT_YOUR_ENEMY = ERRORS_CATEGORY + "not-your-enemy";
    public static final String CANNOT_DEMOTE_MEMBER = ERRORS_CATEGORY + "cannot-demote-member";
    public static final String CANNOT_DEMOTE_YOURSELF = ERRORS_CATEGORY + "cannot-demote-yourself";
    public static final String THATS_YOUR_CLAN = ERRORS_CATEGORY + "thats-your-clan";
    public static final String ALREADY_REQUESTED_NEUTRALITY = ERRORS_CATEGORY + "already-requested-neutrality";

    private static final String GENERAL_CATEGORY = "general.";
    public static final String CLAN_CREATED = GENERAL_CATEGORY + "clan-created";
    public static final String PLAYER_INVITED = GENERAL_CATEGORY + "player-invited";
    public static final String RECEIVED_INVITE = GENERAL_CATEGORY + "received-invite";
    public static final String JOINED_MESSAGE = GENERAL_CATEGORY + "joined-message";
    public static final String CLAN_DISBANDED = GENERAL_CATEGORY + "clan-disbanded";
    public static final String LEFT_CLAN = GENERAL_CATEGORY + "left-clan";
    public static final String THERE_ARE_NO_CLANS = GENERAL_CATEGORY + "there-are-no-clans";
    public static final String MONEY_TAKEN = GENERAL_CATEGORY + "money-taken";
    public static final String PLAYER_KICKED = GENERAL_CATEGORY + "player-kicked";
    public static final String PLAYER_UNINVITED = GENERAL_CATEGORY + "player-uninvited";
    public static final String YOU_WERE_UNINVITED = GENERAL_CATEGORY + "you-were-uninvited";
    public static final String NEW_TAG_INFO = GENERAL_CATEGORY + "new-tag-info";
    public static final String SPY_DISABLED = GENERAL_CATEGORY + "spy-disabled";
    public static final String SPY_ENABLED = GENERAL_CATEGORY + "spy-enabled";
    public static final String ENEMY_DECLARATION = GENERAL_CATEGORY + "enemy-declaration";
    public static final String SUCCESSFUL_PROMOTE = GENERAL_CATEGORY + "successful-promote";
    public static final String PROMOTED = GENERAL_CATEGORY + "promoted";
    public static final String SUCCESSFUL_DEMOTE = GENERAL_CATEGORY + "successful-demote";
    public static final String DEMOTED = GENERAL_CATEGORY + "demoted";
    public static final String CLANS_NOW_NEUTRAL = GENERAL_CATEGORY + "clans-now-neutral";
    public static final String CLAN_WANTS_NEUTRAL = GENERAL_CATEGORY + "clans-wants-neutral";
    public static final String NEUTRAL_CANCELED = GENERAL_CATEGORY + "neutral-canceled";
    public static final String OTHERS_NEUTRAL_CANCELED = GENERAL_CATEGORY + "others-neutral-canceled";
    public static final String KICKED_FOR_SPAWN_KILL = GENERAL_CATEGORY + "kicked-for-spawn-kill";
    public static final String CLANS_NOW_ENEMIES = GENERAL_CATEGORY + "clans-now-enemies";
    public static final String FF_DISABLED = GENERAL_CATEGORY + "ff-disabled";
    public static final String FF_ENABLED = GENERAL_CATEGORY + "ff-enabled";
    public static final String CLAN_FF_DISABLED = GENERAL_CATEGORY + "clan-ff-disabled";
    public static final String CLAN_FF_ENABLED = GENERAL_CATEGORY + "clan-ff-enabled";
    public static final String ACCEPT_NEUTRAL_HELP = GENERAL_CATEGORY + "accept-neutral-help";

    private static final String INFO_CATEGORY = "info.";
    public static final String OWNER = INFO_CATEGORY + "owner";
    public static final String LEADERS = INFO_CATEGORY + "leaders";
    public static final String MEMBERS = INFO_CATEGORY + "members";
    public static final String INFO = INFO_CATEGORY + "info";
    public static final String KDR = INFO_CATEGORY + "kdr";
    public static final String KILLS = INFO_CATEGORY + "kills";
    public static final String DEATHS = INFO_CATEGORY + "deaths";
    public static final String NAME = INFO_CATEGORY + "name";
    public static final String PLAYER = INFO_CATEGORY + "player";
    public static final String CLAN = INFO_CATEGORY + "clan";
    public static final String ENEMIES = INFO_CATEGORY + "enemies";

    private static final String LIST_CATEGORY = "list.";
    public static final String CLANS_LIST_HEADER = LIST_CATEGORY + "clans-list-header";
    public static final String CLANS_LIST_PAGE = LIST_CATEGORY + "clans-list-page";
    public static final String CLANS_LIST_INDEX = LIST_CATEGORY + "clans-list-index";
    public static final String CLANS_LIST_INVALID_PAGE_NUMBER = LIST_CATEGORY + "clans-list-invalid-page-number";
    public static final String CLANS_LIST_KDR = LIST_CATEGORY + "clans-list-kdr";
    public static final String CLANS_LIST_NO_CLANS = LIST_CATEGORY + "no-clans";

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
    private static I18n instance;

    /**
     * The name of the file with the configuration.
     */
    private static final String FILE_NAME = "language.yml";

    /**
     * Creates the language manager.
     *
     * @param plugin the plugin.
     */
    private I18n(JavaPlugin plugin) {
        this.plugin = plugin;
        strings = new HashMap<>();
    }

    /**
     * Finds a string given its key in the configuration file. (one of the constants) Also replaces placeholders in the
     * string, if * provided.
     *
     * @param key               the key of the string to find in the configuration file. (one of the constants)
     * @param placeholderValues the placeholder values to replace (optional). The first will replace {0} and so on.
     * @return the found string or null if it doesn't exist.
     */
    public String get(String key, Object... placeholderValues) {
        return createMessage(key, placeholderValues);
    }

    /**
     * Returns the (single) language manager class instance.
     *
     * @return the (single) language manager class instance.
     */
    public static I18n getInstance() {
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
        String message = instance.createMessage(key, placeholderValues);
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
        String message = instance.createMessage(key, placeholderValues);
        server.broadcastMessage(message);
    }

    /**
     * Loads the language manager instance.
     *
     * @param plugin the plugin.
     */
    public static void init(JavaPlugin plugin) {
        instance = new I18n(plugin);
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
                ConfigurationSection section = configuration.getConfigurationSection(key);
                for (String string : Objects.requireNonNull(section).getKeys(false)) {
                    String value = Objects.requireNonNull(section.getString(string));
                    strings.put(key + "." + string, value);
                }
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
     * Creates a message, given a string key and, optionally, the placeholders to replace.
     *
     * @param key               the key of the string to find.
     * @param placeholderValues the placeholder values to replace (optional). The first will replace {0} and so on.
     * @return the created message.
     */
    @NotNull
    private String createMessage(String key, Object... placeholderValues) {
        String message = strings.get(key);
        if (message == null) {
            return key;
        }

        for (int i = 0; i < placeholderValues.length; i++) {
            String value = String.valueOf(placeholderValues[i]);
            message = message.replace(placeholder(i), value);
        }

        return TextUtil.translateColoredText(message);
    }

    /**
     * Creates a file configuration including the default values.
     *
     * @return the created file configuration.
     */
    private static FileConfiguration createFileConfiguration() {
        FileConfiguration fileConfiguration = new YamlConfiguration();

        fileConfiguration.addDefault(COMMANDS_HEADER, "&b - &3Clan Commands &b-");

        fileConfiguration.addDefault(CREATE_USAGE, "&b/clan create (tag) [name]&f - Create a clan.");
        fileConfiguration.addDefault(INVITE_USAGE, "&b/clan invite (name)&f - Invite a player to your clan.");
        fileConfiguration.addDefault(UNINVITE_USAGE, "&b/clan uninvite (name)&f - Cancel an invite.");
        fileConfiguration.addDefault(RELOAD_USAGE, "&b/clan reload&f - Reload configurations.");
        fileConfiguration.addDefault(LEAVE_USAGE, "&b/clan leave&f - Leave your clan.");
        fileConfiguration.addDefault(DISBAND_USAGE, "&b/clan disband&f - Disband your clan.");
        fileConfiguration.addDefault(INFO_USAGE,
                "&b/clan info [clan/player] (tag/player name)&f - Show clan/player information.");
        fileConfiguration.addDefault(KICK_USAGE, "&b/clan kick (name)&f - Kick a player from your clan.");
        fileConfiguration.addDefault(MODTAG_USAGE, "&b/clan modtag (newtag)&f - Change the colors of your clan tag.");
        fileConfiguration.addDefault(ENEMY_USAGE, "&b/clan enemy (tag)&f - Declare that a clan is your enemy.");
        fileConfiguration.addDefault(UNENEMY_USAGE, "&b/clan unenemy (tag)&f - Request neutrality to an enemy clan.");
        fileConfiguration.addDefault(MAKE_LEADER_USAGE, "&b/clan makeleader (player)&f - Promote a player to leader.");
        fileConfiguration.addDefault(REMOVE_LEADER_USAGE,
                "&b/clan removeleader (player)&f - Demote a player from leader.");
        fileConfiguration.addDefault(LIST_USAGE, "&b/clan list [page]&f - List all clans.");
        fileConfiguration.addDefault(FF_USAGE, "&b/clan ff&f - Disable personal friendly fire.");
        fileConfiguration.addDefault(CLAN_FF_USAGE, "&b/clan clanff&f - Disable clan friendly fire.");

        fileConfiguration.addDefault(WRONG_CREATE_USAGE, "&cTo create a clan, use: &b/clan create (tag) [name]");
        fileConfiguration.addDefault(WRONG_INVITE_USAGE, "&cTo invite a player, use: &b/clan invite (player)");
        fileConfiguration.addDefault(WRONG_UNINVITE_USAGE, "&cTo cancel an invite, use: &b/clan uninvite (player)");
        fileConfiguration.addDefault(WRONG_JOIN_USAGE, "&cTo join a clan, use: &b/clan join [tag]");
        fileConfiguration.addDefault(WRONG_INFO_USAGE,
                "&cTo show clan/player information, use: &b/clan info [clan/player] (tag/player name)");
        fileConfiguration.addDefault(WRONG_KICK_USAGE, "&cTo kick a player from your clan, use: &b/clan kick (player)");
        fileConfiguration.addDefault(WRONG_MODTAG_USAGE,
                "&cTo change the colors of your tag, use: &b/clan modtag (newtag)");
        fileConfiguration.addDefault(WRONG_ENEMY_USAGE, "&cTo declare a clan as your enemy, use: &b/clan enemy (tag)");
        fileConfiguration.addDefault(WRONG_UNENEMY_USAGE, "&cTo request neutrality, use: &b/clan unenemy (tag)");
        fileConfiguration.addDefault(WRONG_MAKE_LEADER_USAGE,
                "&cTo promote a player from your clan to leader, use:  &b/clan makeleader (player)");
        fileConfiguration.addDefault(WRONG_DEMOTE_LEADER_USAGE,
                "&cTo demote a player from your clan to member, use:  &b/clan removeleader (player)");
        fileConfiguration.addDefault(WRONG_LIST_USAGE, "&cTo list all the clans use: &b/clan list [page]");

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
                "&aYou were invited to join the {0} &aclan. Use &b/clan join {1} &ato accept.");
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
        fileConfiguration.addDefault(CLAN_DISBANDED, "&cThe clan {0} &cwas disbanded.");
        fileConfiguration.addDefault(LEFT_CLAN, "&c{0} &cleft {1}&c.");
        fileConfiguration.addDefault(UNKNOWN_PLAYER, "&cThat player is not in the clan database.");
        fileConfiguration.addDefault(UNKNOWN_PLAYER_CLAN, "&cThat player or clan is not in the clan database.");
        fileConfiguration.addDefault(INFO, "&bInfo");
        fileConfiguration.addDefault(OWNER, "&bOwner:&f");
        fileConfiguration.addDefault(LEADERS, "&bLeaders:&f");
        fileConfiguration.addDefault(MEMBERS, "&bMembers ({0}):&f");
        fileConfiguration.addDefault(KDR, "&bKDR:&f");
        fileConfiguration.addDefault(KILLS, "&bKills:&f");
        fileConfiguration.addDefault(DEATHS, "&bDeaths:&f");
        fileConfiguration.addDefault(NAME, "&bName:&f");
        fileConfiguration.addDefault(CLAN, "&bClan:&f");
        fileConfiguration.addDefault(ENEMIES, "&bEnemies:&f");
        fileConfiguration.addDefault(MONEY_TAKEN, "&a{0} has been removed from your account.");
        fileConfiguration.addDefault(PLAYER, "&bPlayer&f");
        fileConfiguration.addDefault(NOT_IN_YOUR_CLAN, "&cThat player is not in your clan.");
        fileConfiguration.addDefault(ONLY_OWNER_KICK_LEADER, "&cOnly the owner of the clan can kick a leader.");
        fileConfiguration.addDefault(PLAYER_KICKED, "&c{0} &cwas kicked out of {1}&c.");
        fileConfiguration.addDefault(CANNOT_KICK_YOURSELF,
                "&cYou can't kick yourself. Use &b/clan leave &cto leave your clan or &b/clan disband &cif you're the owner.");
        fileConfiguration.addDefault(NO_INVITE_PENDING, "&cThat player doesn't have a pending invite from your clan.");
        fileConfiguration.addDefault(PLAYER_UNINVITED, "&a{0} &ais no longer invited to your clan.");
        fileConfiguration.addDefault(YOU_WERE_UNINVITED, "&cYou are no longer invited to {0}&c.");
        fileConfiguration.addDefault(ONLY_CHANGE_COLORS, "&cYou can only change the tag's colors and letter case.");
        fileConfiguration.addDefault(NEW_TAG_INFO, "{0} &ais now known as {1}&a.");
        fileConfiguration.addDefault(SPY_DISABLED, "&bClan chat spy disabled.");
        fileConfiguration.addDefault(SPY_ENABLED, "&bClan chat spy enabled.");
        fileConfiguration.addDefault(CHANGE_SAME_TAG, "{0} &calready is your clan tag.");
        fileConfiguration.addDefault(ALREADY_YOUR_ENEMY, "{0} &cis already your enemy.");
        fileConfiguration.addDefault(ENEMY_DECLARATION, "{0} &cdeclared {1} &cas their enemy.");
        fileConfiguration.addDefault(NOT_YOUR_ENEMY, "&cThat clan is not your enemy.");
        fileConfiguration.addDefault(ALREADY_REQUESTED_NEUTRALITY, "&cYou have already requested neutrality to clan.");
        fileConfiguration.addDefault(CANNOT_PROMOTE_LEADER, "&cThat player is already a leader.");
        fileConfiguration.addDefault(CANNOT_PROMOTE_YOURSELF, "&cYou cannot promote yourself.");
        fileConfiguration.addDefault(SUCCESSFUL_PROMOTE, "&b{0} is now a leader.");
        fileConfiguration.addDefault(PROMOTED, "&bYou are now a leader of your clan.");
        fileConfiguration.addDefault(CANNOT_DEMOTE_MEMBER, "&c{0} isn't a leader.");
        fileConfiguration.addDefault(CANNOT_DEMOTE_YOURSELF, "&cYou cannot demote yourself.");
        fileConfiguration.addDefault(SUCCESSFUL_DEMOTE, "&c{0} &cis no longer a leader.");
        fileConfiguration.addDefault(DEMOTED, "&bYou are no longer a leader of your clan.");
        fileConfiguration.addDefault(CLANS_NOW_NEUTRAL, "{0} &aand {1} &aare now neutral.");
        fileConfiguration.addDefault(CLAN_WANTS_NEUTRAL, "{0} &awants to be neutral with {1}&a.");
        fileConfiguration.addDefault(NEUTRAL_CANCELED, "&cThe neutrality request to {0} &chas been canceled.");
        fileConfiguration.addDefault(OTHERS_NEUTRAL_CANCELED, "&a{0} &acanceled {1}&a's neutrality request.");
        fileConfiguration.addDefault(THATS_YOUR_CLAN, "{0} &cis your clan.");
        fileConfiguration.addDefault(CLANS_LIST_HEADER, "&bClans: ");
        fileConfiguration.addDefault(CLANS_LIST_PAGE, "&bPage number: {0}");
        fileConfiguration.addDefault(CLANS_LIST_INDEX, "&b{0}.");
        fileConfiguration.addDefault(CLANS_LIST_INVALID_PAGE_NUMBER, "&cInvalid page number. Max page is &b{0}&c.");
        fileConfiguration.addDefault(CLANS_LIST_KDR, "&f{0}");
        fileConfiguration.addDefault(CLANS_LIST_NO_CLANS, "&cThere are no clans.");
        fileConfiguration.addDefault(THERE_ARE_NO_CLANS, "&bThere are no clans.");
        fileConfiguration.addDefault(KICKED_FOR_SPAWN_KILL,
                "&cYou were kicked for killing the same player too many times.");
        fileConfiguration.addDefault(CLANS_NOW_ENEMIES, "{0} &cand {1} &care now enemies.");
        fileConfiguration.addDefault(FF_DISABLED, "&bPersonal friendly fire &adisabled&b.");
        fileConfiguration.addDefault(FF_ENABLED, "&bPersonal friendly fire &cenabled&b.");
        fileConfiguration.addDefault(CLAN_FF_DISABLED, "&bClan friendly fire &adisabled&b.");
        fileConfiguration.addDefault(CLAN_FF_ENABLED, "&bClan friendly fire &cenabled&b.");
        fileConfiguration.addDefault(ACCEPT_NEUTRAL_HELP,
                "&aUse &b/clan unenemy {0} &ato accept the neutrality request.");

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
}
