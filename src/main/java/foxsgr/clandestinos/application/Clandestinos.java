package foxsgr.clandestinos.application;

import foxsgr.clandestinos.application.clans.ClanCommand;
import foxsgr.clandestinos.util.Plugins;
import foxsgr.clandestinos.persistence.PersistenceContext;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Clandestinos extends JavaPlugin {

    private boolean usingPAPI;
    private ChatManager chatManager;
    private JoinQuitListener joinQuitListener;
    private DeathListener deathListener;
    private static final String CLAN_COMMAND = "clan";

    public Clandestinos() {
        super();
        chatManager = new ChatManager(this);
        joinQuitListener = new JoinQuitListener();
        deathListener = new DeathListener();
    }

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            usingPAPI = true;
        }

        ConfigManager.init(this);
        LanguageManager.init(this);
        PersistenceContext.init(this);

        // Must be after PersistenceContext.init(this)
        chatManager.setup();
        joinQuitListener.setup();
        deathListener.setup();

        EconomyManager.init(this);

        Plugins.registerCommand(this, CLAN_COMMAND, new ClanCommand(this));
        Bukkit.getPluginManager().registerEvents(chatManager, this);
        Bukkit.getPluginManager().registerEvents(deathListener, this);
    }

    boolean isUsingPAPI() {
        return usingPAPI;
    }
}
