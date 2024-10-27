package com.pilzbros.saw.commands;

import com.pilzbros.saw.manager.Setup;
import com.pilzbros.saw.messages.AdminMessages;
import com.pilzbros.saw.Saw;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SawAdminExe implements CommandExecutor {
    private Saw saw;
    private Setup setup;

    public SawAdminExe(Saw ref) {
        this.saw = ref;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        this.setup = new Setup(this.saw, sender);
        AdminMessages messages = new AdminMessages(this.saw, sender);
        if (!sender.hasPermission("Saw.*") && !sender.hasPermission("Saw.admin")) {
            messages.PermissionError();
        } else if (args.length < 1) {
            messages.prefix();
            if (!this.saw.sawPlaying.isEmpty()) {
                messages.playingDetails();
            }

            if (!this.saw.sawQueue.isEmpty()) {
                messages.queueDetails();
            }

            messages.sawDetails();
        } else {
            Player p;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("clear")) {
                    this.saw.sawQueue.clear();
                    messages.clearQueue();
                } else if (args[0].equalsIgnoreCase("help")) {
                    messages.adminCommands();
                } else if (args[0].equalsIgnoreCase("reload")) {
                    this.saw.sawManager.shutdown();
                    this.saw.sawManager.bootup();
                    this.saw.IO.LoadSettings();
                    this.saw.IO.PrepareDB();
                    this.saw.IO.LoadLocations();
                    this.saw.setupCheck();
                    messages.reboot();
                } else if (args[0].equalsIgnoreCase("end")) {
                    if (!this.saw.sawPlaying.isEmpty()) {
                        this.saw.sawManager.end();
                    } else {
                        messages.endNone();
                    }
                } else if (args[0].equalsIgnoreCase("loc")) {
                    p = Bukkit.getPlayer(sender.getName());
                    p.sendMessage("X:" + p.getLocation().getBlockX());
                    p.sendMessage("Y:" + p.getLocation().getBlockY());
                    p.sendMessage("Z:" + p.getLocation().getBlockZ());
                } else if (args[0].equalsIgnoreCase("setup")) {
                    if (this.saw.setupNeeded) {
                        this.setup.setupCalled();
                    } else {
                        messages.setupNotNeeded();
                    }
                } else {
                    messages.syntaxError();
                }
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("winpoint")) {
                    p = Bukkit.getPlayer(sender.getName());
                    this.saw.winPoint = p.getLocation();
                    this.saw.IO.updateLocation(p.getLocation(), "Win");
                    if (this.saw.setupNeeded) {
                        this.setup.commandCalled(2);
                    } else {
                        messages.winpointUpdated();
                    }
                } else if (args[1].equalsIgnoreCase("startpoint")) {
                    p = Bukkit.getPlayer(sender.getName());
                    this.saw.startPoint = p.getLocation();
                    this.saw.IO.updateLocation(p.getLocation(), "Start");
                    if (this.saw.setupNeeded) {
                        this.setup.commandCalled(1);
                    } else {
                        messages.startpointUpdated();
                    }
                } else if (args[1].equalsIgnoreCase("returnpoint")) {
                    p = Bukkit.getPlayer(sender.getName());
                    this.saw.returnPoint = p.getLocation();
                    this.saw.IO.updateLocation(p.getLocation(), "Return");
                    if (this.saw.setupNeeded) {
                        this.setup.commandCalled(3);
                    } else {
                        messages.returnpointUpdated();
                    }
                } else if (args[1].equalsIgnoreCase("checkpoint")) {
                    p = Bukkit.getPlayer(sender.getName());
                    Location temp = p.getLocation();
                    Location cp = new Location(temp.getWorld(), temp.getX(), temp.getY(), temp.getZ());
                    this.saw.IO.storeCheckpoint(cp);
                    messages.checkpointAdded();
                } else {
                    messages.syntaxError();
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (args[1].equalsIgnoreCase("time")) {
                        this.saw.defaultTime = Integer.parseInt(args[2]);
                        this.saw.IO.updateSetting(Integer.parseInt(args[2]), "Time");
                        messages.settingsUpdated();
                    } else if (args[1].equalsIgnoreCase("lives")) {
                        this.saw.defaultLives = Integer.parseInt(args[2]);
                        this.saw.IO.updateSetting(Integer.parseInt(args[2]), "Lives");
                        messages.settingsUpdated();
                    } else if (args[1].equalsIgnoreCase("diamonds")) {
                        this.saw.diamonds = Integer.parseInt(args[2]);
                        this.saw.IO.updateSetting(Integer.parseInt(args[2]), "Diamonds");
                        messages.settingsUpdated();
                    } else if (args[1].equalsIgnoreCase("checkpoints")) {
                        this.saw.defaultCheckpoints = Integer.parseInt(args[2]);
                        this.saw.IO.updateSetting(Integer.parseInt(args[2]), "Checkpoints");
                        messages.checkpointsUpdated();
                    } else {
                        messages.syntaxError();
                    }
                } else {
                    messages.syntaxError();
                }
            } else {
                messages.syntaxError();
            }
        }

        return true;
    }
}
