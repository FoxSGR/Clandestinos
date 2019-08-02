package foxsgr.clandestinos.application.antispawnkill;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.Date;
import java.util.Objects;

class Kill {

    final OfflinePlayer killer;
    final OfflinePlayer killed;
    final Date date;
    final Location location;

    Kill(Location location, OfflinePlayer killer, OfflinePlayer killed) {
        this.location = location;
        this.killer = killer;
        this.killed = killed;
        date = new Date();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Kill)) {
            return false;
        }
        Kill otherKill = (Kill) object;
        return killer.equals(otherKill.killer) &&
                killed.equals(otherKill.killed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(killer, killed);
    }

    public long dateDifferenceInSeconds(Kill otherKill) {
        return (date.getTime() - otherKill.date.getTime()) / 1000;
    }
}
