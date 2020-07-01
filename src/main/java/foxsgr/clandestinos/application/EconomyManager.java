package foxsgr.clandestinos.application;

import foxsgr.clandestinos.application.config.LanguageManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Economy manager. Gives and takes money to and from players.
 */
public class EconomyManager {

    /**
     * The plugin.
     */
    private final JavaPlugin plugin;

    /**
     * The Vault economy.
     */
    private final Economy economy;

    /**
     * The single class instance.
     */
    private static EconomyManager instance;

    /**
     * Creates the economy manager.
     *
     * @param plugin the plugin.
     */
    private EconomyManager(JavaPlugin plugin) {
        this.plugin = plugin;
        economy = setupEconomy();

        if (economy == null) {
            throw new IllegalStateException("Could not setup economy. This plugin requires Vault and an economy provider.");
        }
    }

    /**
     * Takes money from a player and warns him if he doesn't have the given amount.
     *
     * @param player the player to take money from.
     * @param amount the amount of money to take from the player.
     * @return true if the money amount was successfuly taken from the player, false otherwise.
     */
    public boolean take(Player player, double amount) {
        if (economy.withdrawPlayer(player, amount).transactionSuccess()) {
            if (amount != 0) {
                LanguageManager.send(player, LanguageManager.MONEY_TAKEN, format(amount));
            }

            return true;
        }

        return false;
    }

    /**
     * Checks whether a player has a given amount of money.
     *
     * @param player the player to check.
     * @param amount the amount of money to check if the player has.
     * @return true if the player has the given amount of money, false otherwise.
     */
    public boolean hasEnough(Player player, double amount) {
        return economy.has(player, amount);
    }

    /**
     * Formats a given amount of money.
     *
     * @param amount the amount of money to format.
     * @return the formatted amount of money.
     */
    public String format(double amount) {
        return economy.format(amount);
    }

    /**
     * Returns the (single) economy manager instance.
     *
     * @return the economy manager instance.
     */
    public static EconomyManager getInstance() {
        return instance;
    }

    /**
     * Loads the economy manager.
     *
     * @param plugin the plugin.
     */
    static void init(JavaPlugin plugin) {
        instance = new EconomyManager(plugin);
    }

    /**
     * Sets up the vault economy.
     *
     * @return the vault economy.
     */
    private Economy setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return null;
        }

        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return null;
        }

        return rsp.getProvider();
    }
}
