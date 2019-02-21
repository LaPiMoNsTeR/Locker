package pf.lapimonster.locker.locked;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Locked {

    private UUID owner;
    private String ownerName;
    private Location location;
    private List<String> friends = new ArrayList<>();


    public Locked(Player owner, Location location) {
        this(owner.getUniqueId(), location);
        this.ownerName = owner.getName();
    }

    public Locked(UUID owner, Location location) {
        this.owner = owner;
        this.location = location;
    }


    public UUID getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Location getLocation() {
        return location;
    }

    public List<String> getFriends() {
        return friends;
    }

    public boolean isFriend(String player) {
        return this.friends.contains(player);
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public void addFriend(String player) {
        this.friends.add(player);
    }

    public void removeFriend(String player) {
        this.friends.remove(player);
    }



    public boolean canFullAccess(Player player) {
        return this.getOwner().equals(player.getUniqueId()) || player.hasPermission("locker.admin.bypass");
    }

    public boolean canAccess(Player player) {
        return this.getOwner().equals(player.getUniqueId()) || this.isFriend(player.getName()) || player.hasPermission("locker.admin.bypass");
    }



    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", this.getClass().getName());
        map.put("location", this.location.serialize());
        map.put("friends", this.friends);

        return map;
    }

    public static Locked deserialize(UUID owner, Map<String, Object> map) {
        System.out.println("deserialze map : " + map);
        Location location = Location.deserialize((Map<String, Object>) map.get("location"));
        String type = String.valueOf(map.get("type"));
        List<String> friends = (List<String>) map.get("friends");

        try {
            Locked locked = (Locked) Class.forName(type).getConstructors()[1].newInstance(owner, location);

            if(friends != null)
                locked.setFriends(friends);

            return locked;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();

            return null;
        }
    }
}
