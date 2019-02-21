package pf.lapimonster.locker.locked;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LockedGate extends Locked {
    public LockedGate(Player owner, Location location) {
        super(owner, location);
    }

    public LockedGate(UUID owner, Location location) {
        super(owner, location);
    }
}
