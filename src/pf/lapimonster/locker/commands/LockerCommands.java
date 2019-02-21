package pf.lapimonster.locker.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pf.lapimonster.locker.locked.Locked;
import pf.lapimonster.locker.Locker;
import pf.lapimonster.locker.LockerEdit;

import java.util.ArrayList;
import java.util.List;

public class LockerCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length == 0) {
                this.help(player);
            } else if(args[0].equals("create")) {
                new LockerEdit(player, LockerEdit.CREATE).enable();
            } else if(args[0].equals("remove")) {
                new LockerEdit(player, LockerEdit.REMOVE).enable();
            } else if(args[0].equals("givekey")) {
                try {
                    String target = args[1];

                    if(Bukkit.getPlayer(target) != null) {
                        new LockerEdit(player, target, LockerEdit.ADD_FRIEND).enable();
                    } else {
                        player.sendMessage("§cJoueur introuvable.");
                    }
                } catch(ArrayIndexOutOfBoundsException e) {
                    player.sendMessage("§cVous n'avez pas spécifié le nom du joueur");
                }
            } else if(args[0].equals("removekey")) {
                try {
                    String target = args[1];

                    if(Bukkit.getPlayer(target) != null) {
                        new LockerEdit(player, target, LockerEdit.REMOVE_FRIEND).enable();
                    } else {
                        player.sendMessage("§cJoueur introuvable.");
                    }
                } catch(ArrayIndexOutOfBoundsException e) {
                    player.sendMessage("§cVous n'avez pas spécifié le nom du joueur");
                }
            } else if(args[0].equals("allowedplayer")) {
                List<Locked> l = new ArrayList<>();
                l.addAll(Locker.getInstance().getLockedManager().get(player.getUniqueId()));

                if(l.size() == 0) {
                    player.sendMessage("§cVous n'avez verrouillé aucun coffre.");
                } else {
                    player.sendMessage("§aVous avez vérrouillé " + l.size() + " coffre(s).");
                }
            } else if(args[0].equals("list")) {
                List<Locked> l = new ArrayList<>();
                l.addAll(Locker.getInstance().getLockedManager().get(player.getUniqueId()));

                if(l.size() == 0) {
                    player.sendMessage("§cVous n'avez verrouillé aucun coffre.");
                } else {
                    player.sendMessage("§aVous avez vérrouillé " + l.size() + " coffre(s).");
                }
            }
        }

        return true;
    }


    private void help(Player player) {
        player.sendMessage("§7[§eLOCKER§7] §bcommands :");
        player.sendMessage("/locker create");
        player.sendMessage("/locker remove");
        player.sendMessage("/locker givekey <friend>");
        player.sendMessage("/locker removekey <friend>");
        player.sendMessage("/locker allowedplayer");
        player.sendMessage("/locker list");
    }

}
