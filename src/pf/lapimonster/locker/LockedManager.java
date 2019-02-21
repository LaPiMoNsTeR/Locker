package pf.lapimonster.locker;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pf.lapimonster.locker.locked.Locked;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LockedManager {

    private File file;
    private FileConfiguration config;


    private List<Locked> lockeds = new ArrayList<>();


    public LockedManager() {
        this.file = new File(Locker.getInstance().getDataFolder(), "datas.yml");

        if(!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        this.config = YamlConfiguration.loadConfiguration(this.file);
    }


    public void load() {
        if(this.config.get("datas") != null) {
            for(String key : this.config.getConfigurationSection("datas").getValues(false).keySet()) {
                UUID uuid = UUID.fromString(key);
                String name = this.config.getString("datas." + key + ".owner-name");

                List<Map<?, ?>> mapList = this.config.getMapList("datas." + key + ".lockeds");

                for(Map<?, ?> map : mapList) {
                    Locked locked = Locked.deserialize(uuid, (Map<String, Object>) map);
                    locked.setOwnerName(name);
                    this.addLocked(locked);
                }
            }
        }
    }

    public void update() {
        Map<UUID, List<Map<String, Object>>> updateList = new HashMap<>();

        for(Locked locked : this.lockeds) {
            updateList.computeIfAbsent(locked.getOwner(), uuid -> new ArrayList<>());

            List<Map<String, Object>> l = updateList.get(locked.getOwner());
            l.add(locked.serialize());
            updateList.put(locked.getOwner(), l);
        }

        for(Map.Entry<UUID, List<Map<String, Object>>> e : updateList.entrySet()) {
            Locked locked = this.getFirst(e.getKey());
            this.config.set("datas." + e.getKey().toString() + ".owner-name", locked.getOwnerName());
            this.config.set("datas." + e.getKey().toString() + ".lockeds", e.getValue());
            this.save();
        }
    }


    public void save() {
        try {
            this.config.save(this.file);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public void addLocked(Locked locked) {
        this.lockeds.add(locked);
    }

    public void removeLocked(Locked locked) {
        this.lockeds.remove(locked);
    }

    public List<Locked> getLockeds() {
        return lockeds;
    }

    public Locked get(Location location) {
        for(Locked locked : this.lockeds) {
            if(locked.getLocation().getBlock().getLocation().equals(location.getBlock().getLocation())) {
                return locked;
            }
        }

        return null;
    }


    public Locked get(Location location, Class<? extends Locked> clazz) {
        for(Locked locked : this.lockeds) {
            if(locked.getLocation().getBlock().getLocation().equals(location.getBlock().getLocation())
                    && clazz.isInstance(locked)) {
                return locked;
            }
        }

        return null;
    }


    public List<Locked> get(UUID uuid) {
        List<Locked> l = new ArrayList<>();

        for(Locked locked : this.lockeds) {
            if(locked.getOwner().equals(uuid)) {
                l.add(locked);
            }
        }

        return l;
    }

    public Locked getFirst(UUID uuid) {
        for(Locked locked : this.lockeds) {
            if(locked.getOwner().equals(uuid)) {
                return locked;
            }
        }

        return null;
    }
}
