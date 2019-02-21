package pf.lapimonster.locker;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import pf.lapimonster.locker.commands.LockerCommands;
import pf.lapimonster.locker.listeners.LockerListener;

public class Locker extends JavaPlugin {

    private static Locker instance;

    private LockedManager lockedManager;





    @Override
    public void onEnable() {
        instance = this;

        if(!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        this.lockedManager = new LockedManager();
        this.lockedManager.load();

        this.getServer().getPluginManager().registerEvents(new LockerListener(), this);

        this.getCommand("locker").setExecutor(new LockerCommands());
    }


    public LockedManager getLockedManager() {
        return lockedManager;
    }

    public static Locker getInstance() {
        return instance;
    }
}
