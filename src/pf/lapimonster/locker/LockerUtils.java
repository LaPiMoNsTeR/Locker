package pf.lapimonster.locker;

import org.bukkit.Material;
import pf.lapimonster.locker.locked.*;

public class LockerUtils {

    public static Material[] doors = new Material[] {
            Material.ACACIA_DOOR,
            Material.BIRCH_DOOR,
            Material.DARK_OAK_DOOR,
            Material.IRON_DOOR,
            Material.JUNGLE_DOOR,
            Material.SPRUCE_DOOR,
            Material.WOOD_DOOR,
            Material.WOODEN_DOOR
    };

    public static Material[] gates = new Material[] {
            Material.ACACIA_FENCE_GATE ,
            Material.BIRCH_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,
            Material.FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.SPRUCE_FENCE_GATE
    };

    public static String getLockedName(Locked locked) {
        if(locked instanceof LockedChest) {
            return "ce coffre";
        } else if(locked instanceof LockedDoor) {
            return "cette porte";
        } else if(locked instanceof LockedTrapdoor) {
            return "cette trappe";
        } else if(locked instanceof LockedGate) {
            return "ce portail";
        } else return null;
    }


    public static boolean isDoor(Material target) {
        for(Material material : doors) {
            if(material == target)
                return true;
        }

        return false;
    }

    public static boolean isGate(Material target) {
        for(Material material : gates) {
            if(material == target)
                return true;
        }

        return false;
    }


}
