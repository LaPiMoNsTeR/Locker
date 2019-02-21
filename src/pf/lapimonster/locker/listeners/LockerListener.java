package pf.lapimonster.locker.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import pf.lapimonster.locker.Locker;
import pf.lapimonster.locker.LockerUtils;
import pf.lapimonster.locker.locked.LockedChest;
import pf.lapimonster.locker.locked.LockedDoor;
import pf.lapimonster.locker.locked.LockedGate;
import pf.lapimonster.locker.locked.LockedTrapdoor;

public class LockerListener implements Listener {

    public static final BlockFace[] CHEST_SIDES = new BlockFace[] {BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST};
    public static final BlockFace[] DOOR_SIDES = new BlockFace[] {BlockFace.UP, BlockFace.DOWN};

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getClickedBlock() != null) {
            Material m = e.getClickedBlock().getType();

            if(m == Material.CHEST) {
                Block block = e.getClickedBlock();
                Location location = e.getClickedBlock().getLocation();

                LockedChest locked = (LockedChest) Locker.getInstance().getLockedManager().get(location, LockedChest.class);
                if(locked != null) {
                    if(!locked.canAccess(e.getPlayer())) {
                        e.getPlayer().sendMessage("§cCe coffre est verrouilé.");
                        e.setCancelled(true);
                    }
                } else {
                    for(BlockFace side : CHEST_SIDES) {
                        locked = (LockedChest) Locker.getInstance().getLockedManager().get(block.getRelative(side).getLocation(), LockedChest.class);
                        if(locked != null) {
                            if(!locked.canAccess(e.getPlayer())) {
                                e.getPlayer().sendMessage("§cCe coffre est verrouilé.");
                                e.setCancelled(true);
                            }
                            break;
                        }
                    }
                }
            } else if(LockerUtils.isDoor(m)) {
                Block block = e.getClickedBlock();
                Location location = e.getClickedBlock().getLocation();

                LockedDoor locked = (LockedDoor) Locker.getInstance().getLockedManager().get(location, LockedDoor.class);
                if(locked != null) {
                    if(!locked.canAccess(e.getPlayer())) {
                        e.getPlayer().sendMessage("§cCette porte est verrouilée.");
                        e.setCancelled(true);
                    }
                } else {
                    for(BlockFace side : DOOR_SIDES) {
                        locked = (LockedDoor) Locker.getInstance().getLockedManager().get(block.getRelative(side).getLocation(), LockedDoor.class);
                        if(locked != null) {
                            if(!locked.canAccess(e.getPlayer())) {
                                e.getPlayer().sendMessage("§cCette porte est verrouilée.");
                                e.setCancelled(true);
                            }
                            break;
                        }
                    }
                }
            } else if(m == Material.TRAP_DOOR) {
                Location location = e.getClickedBlock().getLocation();

                LockedTrapdoor locked = (LockedTrapdoor) Locker.getInstance().getLockedManager().get(location, LockedTrapdoor.class);
                if(locked != null) {
                    if(!locked.canAccess(e.getPlayer())) {
                        e.getPlayer().sendMessage("§cCette trappe est verrouilée.");
                        e.setCancelled(true);
                    }
                }
            }else if(LockerUtils.isGate(m)) {
                Location location = e.getClickedBlock().getLocation();

                LockedGate locked = (LockedGate) Locker.getInstance().getLockedManager().get(location, LockedGate.class);
                if(locked != null) {
                    if(!locked.canAccess(e.getPlayer())) {
                        e.getPlayer().sendMessage("§cCe portail est verrouilée.");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Material m = e.getBlock().getType();
        if(m == Material.CHEST) {
            Location location = e.getBlock().getLocation();
            Block block = e.getBlock();

            LockedChest locked = (LockedChest) Locker.getInstance().getLockedManager().get(location, LockedChest.class);
            if(locked != null) {
                if(!locked.canFullAccess(e.getPlayer())) {
                    e.getPlayer().sendMessage("§cCe coffre est verrouilé, vous ne pouvez pas le casser.");
                    e.setCancelled(true);
                } else {
                    Locker.getInstance().getLockedManager().removeLocked(locked);
                    Locker.getInstance().getLockedManager().update();
                    e.getPlayer().sendMessage("§aVérrou supprimé.");
                }
            } else {
                for(BlockFace side : CHEST_SIDES) {
                    locked = (LockedChest) Locker.getInstance().getLockedManager().get(block.getRelative(side).getLocation(), LockedChest.class);
                    if(locked != null) {
                        if(!locked.canFullAccess(e.getPlayer())) {
                            e.getPlayer().sendMessage("§cCe coffre est verrouilé.");
                            e.setCancelled(true);
                        } else {
                            Locker.getInstance().getLockedManager().removeLocked(locked);
                            Locker.getInstance().getLockedManager().update();
                            e.getPlayer().sendMessage("§aVérrou supprimé.");
                        }
                        break;
                    }
                }
            }
        } else if(LockerUtils.isDoor(m)) {
            Location location = e.getBlock().getLocation();
            Block block = e.getBlock();

            LockedDoor locked = (LockedDoor) Locker.getInstance().getLockedManager().get(location, LockedDoor.class);
            if(locked != null) {
                if(!locked.canFullAccess(e.getPlayer())) {
                    e.getPlayer().sendMessage("§cCette porte est verrouilée, vous ne pouvez pas le casser.");
                    e.setCancelled(true);
                } else {
                    Locker.getInstance().getLockedManager().removeLocked(locked);
                    Locker.getInstance().getLockedManager().update();
                    e.getPlayer().sendMessage("§aVérrou supprimé.");
                }
            } else {
                for(BlockFace side : DOOR_SIDES) {
                    locked = (LockedDoor) Locker.getInstance().getLockedManager().get(block.getRelative(side).getLocation(), LockedDoor.class);
                    if(locked != null) {
                        if(!locked.canFullAccess(e.getPlayer())) {
                            e.getPlayer().sendMessage("§cCette porte est verrouilée.");
                            e.setCancelled(true);
                        } else {
                            Locker.getInstance().getLockedManager().removeLocked(locked);
                            Locker.getInstance().getLockedManager().update();
                            e.getPlayer().sendMessage("§aVérrou supprimé.");
                        }
                        break;
                    }
                }
            }
        } else if(m == Material.TRAP_DOOR) {
            Location location = e.getBlock().getLocation();

            LockedTrapdoor locked = (LockedTrapdoor) Locker.getInstance().getLockedManager().get(location, LockedTrapdoor.class);
            if(locked != null) {
                if(!locked.canFullAccess(e.getPlayer())) {
                    e.getPlayer().sendMessage("§cCette trappe est verrouilée, vous ne pouvez pas le casser.");
                    e.setCancelled(true);
                } else {
                    Locker.getInstance().getLockedManager().removeLocked(locked);
                    Locker.getInstance().getLockedManager().update();
                    e.getPlayer().sendMessage("§aVérrou supprimé.");
                }
            }
        } else if(LockerUtils.isGate(m)) {
            Location location = e.getBlock().getLocation();

            LockedGate locked = (LockedGate) Locker.getInstance().getLockedManager().get(location, LockedGate.class);
            if(locked != null) {
                if(!locked.canFullAccess(e.getPlayer())) {
                    e.getPlayer().sendMessage("§cCe portail est verrouilée, vous ne pouvez pas le casser.");
                    e.setCancelled(true);
                } else {
                    Locker.getInstance().getLockedManager().removeLocked(locked);
                    Locker.getInstance().getLockedManager().update();
                    e.getPlayer().sendMessage("§aVérrou supprimé.");
                }
            }
        }
    }






}
