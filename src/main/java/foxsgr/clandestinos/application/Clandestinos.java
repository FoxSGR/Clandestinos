package foxsgr.clandestinos.application;

import foxsgr.clandestinos.application.clanchatcommand.ClanChatCommand;
import foxsgr.clandestinos.application.clancommand.ClanCommand;
import foxsgr.clandestinos.application.config.ConfigManager;
import foxsgr.clandestinos.application.config.LanguageManager;
import foxsgr.clandestinos.application.hooks.ClandestinosPAPIExpansion;
import foxsgr.clandestinos.application.listeners.ChatManager;
import foxsgr.clandestinos.application.listeners.DeathListener;
import foxsgr.clandestinos.application.listeners.FriendlyFireBlocker;
import foxsgr.clandestinos.application.listeners.JoinQuitListener;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.persistence.mysql.MySQLRepositoryFactory;
import foxsgr.clandestinos.persistence.yaml.YAMLRepositoryFactory;
import foxsgr.clandestinos.util.Plugins;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Clandestinos clan plugin.
 */
@SuppressWarnings("unused")
public class Clandestinos extends JavaPlugin {

    /**
     * Whether PlacerholderAPI should be used.
     */
    private boolean usingPAPI;

    /**
     * The chat manager/listener.
     */
    private final ChatManager chatManager;

    /**
     * The join/quit listener.
     */
    private final JoinQuitListener joinQuitListener;

    /**
     * The player death listener.
     */
    private final DeathListener deathListener;

    /**
     * The friendly fire blocker event listener.
     */
    private final FriendlyFireBlocker friendlyFireBlocker;

    /**
     * The plugin's main command.
     */
    private final ClanCommand clanCommand;

    /**
     * The clan chat command.
     */
    private final ClanChatCommand clanChatCommand;

    /**
     * The single plugin instance.
     */
    private static Clandestinos instance;

    /**
     * The text usage of the plugin's main command.
     */
    private static final String CLAN_COMMAND = "clan";

    /**
     * The text usage of the clan chat command.
     */
    private static final String CLAN_CHAT_COMMAND = ".";

    /**
     * Creates the plugin. Creates the listeners, command executors and managers.
     */
    public Clandestinos() {
        super();
        chatManager = new ChatManager(this);
        joinQuitListener = new JoinQuitListener();
        deathListener = new DeathListener();
        friendlyFireBlocker = new FriendlyFireBlocker();

        clanCommand = new ClanCommand(this);
        clanChatCommand = new ClanChatCommand();

        instance = this;
    }

    /**
     * Called when the plugin is enabled. Loads the listeners, command executors and managers.
     */
    @Override
    public void onEnable() {
        ConfigManager.init(this);
        LanguageManager.init(this);

        ConfigManager configManager = ConfigManager.getInstance();
        System.out.println(getConfig().getConfigurationSection("mysql"));
        if (configManager.getBoolean(ConfigManager.MYSQL_ENABLED)) {
            PersistenceContext.init(new MySQLRepositoryFactory());
        } else {
            PersistenceContext.init(new YAMLRepositoryFactory(this));
        }

        // Must be after PersistenceContext.init()
        joinQuitListener.setup();
        deathListener.setup();

        if (configManager.getBoolean(ConfigManager.CHAT_FORMATTING_ENABLED)) {
            chatManager.setup();
            Bukkit.getPluginManager().registerEvents(chatManager, this);
        }

        if (configManager.getBoolean(ConfigManager.CLAN_CHAT_ENABLED)) {
            clanChatCommand.setup();
            Plugins.registerCommand(this, CLAN_CHAT_COMMAND, clanChatCommand);
        }

        EconomyManager.init(this);

        Plugins.registerCommand(this, CLAN_COMMAND, clanCommand);
        Plugins.registerTabCompleter(this, CLAN_COMMAND, clanCommand);

        Bukkit.getPluginManager().registerEvents(deathListener, this);
        Bukkit.getPluginManager().registerEvents(friendlyFireBlocker, this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            usingPAPI = true;
            new ClandestinosPAPIExpansion(this).register();
        }
    }

    /**
     * Determines whether PlaceholderAPI should be used.
     *
     * @return true if PlaceholderAPI should be used, false otherwise.
     */
    public boolean isUsingPAPI() {
        return usingPAPI;
    }

    /**
     * Returns the plugin's chat manager.
     *
     * @return the chat manager.
     */
    public ChatManager chatManager() {
        return chatManager;
    }

    /**
     * Returns the (single) plugin instance.
     *
     * @return the plugin.
     */
    public static Clandestinos getInstance() {
        return instance;
    }
}
