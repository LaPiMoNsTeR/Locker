package pf.lapimonster.locker.locked;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LockedDoor extends Locked {
    public LockedDoor(Player owner, Location location) {
        super(owner, location);
    }

    public LockedDoor(UUID owner, Location location) {
        super(owner, location);
    }
}
