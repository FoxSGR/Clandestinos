package foxsgr.clandestinos.application;

import foxsgr.clandestinos.application.hooks.ClandestinosPAPIExpansion;
import foxsgr.clandestinos.application.listeners.ChatManager;
import foxsgr.clandestinos.application.listeners.DeathListener;
import foxsgr.clandestinos.application.listeners.JoinQuitListener;
import foxsgr.clandestinos.persistence.PersistenceContext;
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
     * The plugin's main command.
     */
    private final ClanCommand clanCommand;

    /**
     * The clan chat command.
     */
    private final ClanChatCommand clanChatCommand;

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
        clanCommand = new ClanCommand(this);
        clanChatCommand = new ClanChatCommand();
    }

    /**
     * Called when the plugin is enabled. Loads the listeners, command executors and managers.
     */
    @Override
    public void onEnable() {
        ClanLogger.init(this);
        ConfigManager.init(this);
        LanguageManager.init(this);
        PersistenceContext.init(this);

        // Must be after PersistenceContext.init(this)
        chatManager.setup();
        joinQuitListener.setup();
        deathListener.setup();

        clanChatCommand.setup();

        EconomyManager.init(this);

        Plugins.registerCommand(this, CLAN_COMMAND, clanCommand);
        Plugins.registerTabCompleter(this, CLAN_COMMAND, clanCommand);

        Plugins.registerCommand(this, CLAN_CHAT_COMMAND, clanChatCommand);

        Bukkit.getPluginManager().registerEvents(chatManager, this);
        Bukkit.getPluginManager().registerEvents(deathListener, this);

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
}
