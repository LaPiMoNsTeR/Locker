package pf.lapimonster.locker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pf.lapimonster.locker.listeners.LockerListener;
import pf.lapimonster.locker.locked.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LockerEdit implements Listener {

    private static List<LockerEdit> lockerEdits = new ArrayList<>();

    private Player owner;
    private int type;
    private String friend;


    public static final int CREATE = 0, REMOVE = 1, ADD_FRIEND = 2, REMOVE_FRIEND = 3, ALLOWED_LIST = 4;


    public LockerEdit(Player owner, int type) {
        this.owner = owner;
        this.type = type;

        this.checkLockerEditExist();

        if(this.type == CREATE)
            this.owner.sendMessage("§eSélectionnez le bloc à verrouiller.");
        else if(this.type == REMOVE)
            this.owner.sendMessage("§eSélectionnez le bloc à déverrouiller.");
    }


    public LockerEdit(Player owner, String friend, int type) {
        this.friend = friend;
        this.owner = owner;
        this.type = type;

        this.checkLockerEditExist();

        if(this.type == ADD_FRIEND) {
            this.owner.sendMessage("§eSélectionnez le bloc à autoriser pour " + this.friend + ".");
        } else if(this.type == REMOVE_FRIEND) {
            this.owner.sendMessage("§eSélectionnez le bloc à bloquer pour " + this.friend + ".");
        }
    }



    private void checkLockerEditExist() {
        Iterator<LockerEdit> it = lockerEdits.iterator();
        while(it.hasNext()) {
            LockerEdit le = it.next();
            if(le.getOwner().equals(this.owner)) {
                le.disable(false);
                it.remove();
            }
        }

        lockerEdits.add(this);
    }


    public Player getOwner() {
        return owner;
    }

    public void enable() {
        Locker.getInstance().getServer().getPluginManager().registerEvents(this, Locker.getInstance());
    }

    public void disable(boolean remove) {
        HandlerList.unregisterAll(this);
        if(remove) lockerEdits.remove(this);
    }



    public void init(Block block) {
        Locked locked = null;

        Material m = block.getType();
        if(m == Material.CHEST) {
            locked = new LockedChest(this.owner, block.getLocation());
        } else if(LockerUtils.isDoor(m)) {
            locked = new LockedDoor(this.owner, block.getLocation());
        } else if(m == Material.TRAP_DOOR) {
            locked = new LockedTrapdoor(this.owner, block.getLocation());
        } else if(LockerUtils.isGate(m)) {
            locked = new LockedGate(this.owner, block.getLocation());
        }

        Locker.getInstance().getLockedManager().addLocked(locked);
        Locker.getInstance().getLockedManager().update();

        this.owner.sendMessage("§aVérrou créé.");
        this.owner.sendMessage("§eVous pouvez autoriser des joueurs à accéder à " + LockerUtils.getLockedName(locked) + " en utilisant la commande '/locker addfriend <friend>'.");
        this.owner.sendMessage("§eVous pouvez supprimer le vérrou en tapant la commande '/locker remove'.");

        this.disable(true);
    }

    public void remove(Locked locked) {
        Locker.getInstance().getLockedManager().removeLocked(locked);
        Locker.getInstance().getLockedManager().update();

        this.owner.sendMessage("§aVérrou supprimé.");
        this.disable(true);
    }

    public void addFriend(Locked locked) {
        if (locked.isFriend(this.friend)) {
            this.owner.sendMessage(this.friend + " §ca déjà les clés de " + LockerUtils.getLockedName(locked) + ".");
        } else {
            locked.addFriend(this.friend);
            Locker.getInstance().getLockedManager().update();

            this.owner.sendMessage(this.friend + " §a peut maintenant accéder à " + LockerUtils.getLockedName(locked) + ".");
        }

        this.disable(true);
    }

    public void removeFriend(Locked locked) {
        if(locked.isFriend(this.friend)) {
            locked.removeFriend(this.friend);
            Locker.getInstance().getLockedManager().update();

            this.owner.sendMessage(this.friend + " §a ne peut plus accéder à " + LockerUtils.getLockedName(locked) + ".");
        } else {
            this.owner.sendMessage(this.friend + " §cn'a pas les clés de " + LockerUtils.getLockedName(locked) + ".");
        }

        this.disable(true);
    }


    public void allowedList(Locked locked) {
        StringBuilder allowedPlayer = new StringBuilder();

        for(String friend : locked.getFriends()) {
            allowedPlayer.append(friend).append(", ");
        }

        allowedPlayer = new StringBuilder(allowedPlayer.substring(0, allowedPlayer.length() - 2));

        this.owner.sendMessage("§eLes personnes suivantes ont accès à " + LockerUtils.getLockedName(locked) + " :");
        this.owner.sendMessage(allowedPlayer.toString());

        this.disable(true);
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getPlayer() == this.owner) {
            if(e.getClickedBlock() != null) {
                Player player = e.getPlayer();
                Block block = e.getClickedBlock();
                Material m = block.getType();


                if(this.type == CREATE) {

                    if(m == Material.CHEST || LockerUtils.isDoor(m) || m == Material.TRAP_DOOR || LockerUtils.isGate(m)) {
                        Locked locked = Locker.getInstance().getLockedManager().get(block.getLocation());
                        if(locked == null) {
                            this.init(block);
                        } else {
                            player.sendMessage("§cQuelqu'un a déjà verrouillé " + LockerUtils.getLockedName(locked) + ".");
                            player.sendMessage("§cCréation d'un vérrou annulé.");
                            this.disable(true);
                        }
                    } else {
                        player.sendMessage("§cVous ne pouvez pas verrouiller ce bloc.");
                        player.sendMessage("§cCréation d'un vérrou annulé.");
                        this.disable(true);
                    }

                } else if(this.type == REMOVE) {

                    Locked locked = getAndCheckAccess(block, m);
                    if(locked != null)
                        this.remove(locked);
                    else this.disable(true);

                } else if(this.type == ADD_FRIEND) {


                    Locked locked = getAndCheckAccess(block, m);
                    if(locked != null)
                        this.addFriend(locked);
                    else this.disable(true);


                } else if(this.type == REMOVE_FRIEND) {


                    Locked locked = getAndCheckAccess(block, m);
                    if(locked != null)
                        this.removeFriend(locked);
                    else this.disable(true);


                } else if(this.type == ALLOWED_LIST) {

                    Locked locked = getAndCheckAccess(block, m);
                    if(locked != null)
                        this.allowedList(locked);
                    else this.disable(true);

                }
            }

            e.setCancelled(true);
        }

    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(e.getPlayer() == this.owner) {
            this.disable(true);
        }
    }



    private Locked getAndCheckAccess(Block b, Material m) {
        Locked locked = Locker.getInstance().getLockedManager().get(b.getLocation());
        if(locked != null) {
            if(locked.canFullAccess(this.owner)) {
                return locked;
            } else {
                this.owner.sendMessage("§cCe vérrou ne vous appartient pas.");
                return null;
            }
        } else if(m == Material.CHEST) {
            for(BlockFace side : LockerListener.CHEST_SIDES) {
                locked = Locker.getInstance().getLockedManager().get(b.getRelative(side).getLocation());
                if(locked != null) {
                    if(locked.canFullAccess(this.owner)) {
                        return locked;
                    }
                }

            }
            return null;
        } else if(LockerUtils.isDoor(m)) {
            for(BlockFace side : LockerListener.DOOR_SIDES) {
                locked = Locker.getInstance().getLockedManager().get(b.getRelative(side).getLocation());
                if(locked != null) {
                    if(locked.canFullAccess(this.owner)) {
                        return locked;
                    }
                }
            }
            return null;
        }



        else {
            this.owner.sendMessage("§cVous n'avez pas verrouillé ce bloc.");
            return null;
        }
    }

}
