package foxsgr.clandestinos.application.listeners;

import foxsgr.clandestinos.application.Finder;
import foxsgr.clandestinos.domain.model.clan.Clan;
import foxsgr.clandestinos.domain.model.clan.ClanTag;
import foxsgr.clandestinos.domain.model.clanplayer.ClanPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FriendlyFireBlocker implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damagerEntity = event.getDamager();
        Entity damagedEntity = event.getEntity();
        if (!(damagerEntity instanceof Player && damagedEntity instanceof Player)) {
            return;
        }

        ClanPlayer damager = Finder.findPlayer((OfflinePlayer) damagerEntity);
        ClanPlayer damaged = Finder.findPlayer((OfflinePlayer) damagedEntity);
        if (damager == null || damaged == null) {
            return;
        }

        ClanTag damagerTag = damager.clan().orElse(null);
        ClanTag damagedTag = damaged.clan().orElse(null);
        if (damagerTag == null || damagedTag == null || !damagerTag.equalsIgnoreColor(damagedTag)) {
            return;
        }

        Clan clan = Finder.findClanEnsureExists(damager);

        // If friendly fire is enabled in the clan, don't cancel the event
        if (clan.isFriendlyFireEnabled()) {
            return;
        }

        // If one of the players has friendly fire disabled, cancel the event
        if (!damager.isFriendlyFireEnabled() || !damaged.isFriendlyFireEnabled()) {
            event.setCancelled(true);
        }
    }
}
