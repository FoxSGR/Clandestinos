package foxsgr.clandestinos.application;

import foxsgr.clandestinos.application.listeners.ChatManager;
import foxsgr.clandestinos.application.listeners.DeathListener;
import foxsgr.clandestinos.application.listeners.JoinQuitListener;
import foxsgr.clandestinos.persistence.PersistenceContext;
import foxsgr.clandestinos.util.Plugins;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Clandestinos extends JavaPlugin {

    private boolean usingPAPI;
    private ChatManager chatManager;
    private JoinQuitListener joinQuitListener;
    private DeathListener deathListener;
    private static final String CLAN_COMMAND = "clan";
    private static final String CLAN_CHAT_COMMAND = ".";

    public Clandestinos() {
        super();
        chatManager = new ChatManager(this);
        joinQuitListener = new JoinQuitListener();
        deathListener = new DeathListener();
    }

    @Override
    public void onEnable() {
        ClanLogger.init(this);
        ConfigManager.init(this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            usingPAPI = true;
        }

        LanguageManager.init(this);
        PersistenceContext.init(this);

        // Must be after PersistenceContext.init(this)
        chatManager.setup();
        joinQuitListener.setup();
        deathListener.setup();

        EconomyManager.init(this);

        Plugins.registerCommand(this, CLAN_COMMAND, new ClanCommand(this));
        Plugins.registerCommand(this, CLAN_CHAT_COMMAND, new ClanChatCommand());
        Bukkit.getPluginManager().registerEvents(chatManager, this);
        Bukkit.getPluginManager().registerEvents(deathListener, this);
    }

    boolean isUsingPAPI() {
        return usingPAPI;
    }
}
