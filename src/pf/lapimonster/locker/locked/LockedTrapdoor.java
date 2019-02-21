package pf.lapimonster.locker.locked;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LockedTrapdoor extends Locked {
    public LockedTrapdoor(Player owner, Location location) {
        super(owner, location);
    }

    public LockedTrapdoor(UUID owner, Location location) {
        super(owner, location);
    }
}
