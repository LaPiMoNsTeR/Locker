package pf.lapimonster.locker.locked;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LockedChest extends Locked {
    public LockedChest(Player owner, Location location) {
        super(owner, location);
    }

    public LockedChest(UUID owner, Location location) {
        super(owner, location);
    }
}
