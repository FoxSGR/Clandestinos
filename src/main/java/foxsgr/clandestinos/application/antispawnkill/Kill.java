package foxsgr.clandestinos.application.antispawnkill;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Objects;

class Kill {

    private final Player killer;
    private final Player killed;
    private final Date date;
    final Location location;

    Kill(Location location, Player killer, Player killed) {
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
        return killer.getName().equals(otherKill.killer.getName()) &&
                killed.equals(otherKill.killed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(killer, killed);
    }

    long dateDifferenceInSeconds(Kill otherKill) {
        return (date.getTime() - otherKill.date.getTime()) / 1000;
    }
}
