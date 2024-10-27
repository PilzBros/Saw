package com.pilzbros.saw.commands;

import com.pilzbros.saw.Saw;
import com.pilzbros.saw.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SawExe implements CommandExecutor {
    private Saw saw;

    public SawExe(Saw ref) {
        this.saw = ref;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Messages messages = new Messages(this.saw, sender);
        if (!sender.hasPermission("saw.user") && !sender.hasPermission("saw.*") && !sender.hasPermission("saw.admin")) {
            messages.playPermissionError();
        } else if (args.length < 1) {
            if (this.saw.sawPlaying.isEmpty()) {
                messages.sawDescription();
            } else if (this.saw.sawPlaying.firstEl() == sender) {
                messages.currentlyPlaying();
            } else {
                messages.sawDescription();
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("play")) {
                if (!this.saw.setupNeeded) {
                    if (this.saw.sawPlaying.isEmpty()) {
                        this.saw.sawWait.enqueue(sender);
                    } else if (this.saw.sawWait.isEmpty()) {
                        if (this.saw.sawPlaying.firstEl() != sender) {
                            this.saw.sawWait.enqueue(sender);
                            messages.waitingQueueFirst();
                        } else {
                            messages.alreadyPlaying();
                        }
                    } else if (this.saw.sawPlaying.firstEl() != sender) {
                        if (this.saw.sawWait.inQueue(sender)) {
                            messages.alreadyInQueue();
                        } else {
                            this.saw.sawWait.enqueue(sender);
                            messages.waitingQueue(this.saw.sawWait.size());
                        }
                    } else {
                        messages.alreadyPlaying();
                    }
                } else if (!sender.hasPermission("saw.*") && !sender.hasPermission("saw.admin")) {
                    messages.setupNeeded();
                } else {
                    messages.adminSetupNeeded();
                }
            } else if (args[0].equalsIgnoreCase("quit")) {
                if (!this.saw.sawPlaying.isEmpty()) {
                    if (this.saw.sawPlaying.firstEl() == sender) {
                        this.saw.sawManager.quit();
                    } else {
                        this.saw.sawWait.remove(sender);
                        messages.leaveQueue();
                    }
                } else {
                    messages.quitNLNP();
                }
            } else if (args[0].equalsIgnoreCase("cancel")) {
                if (this.saw.sawWait.inQueue(sender)) {
                    this.saw.sawWait.remove(sender);
                    messages.leaveQueue();
                } else {
                    messages.cancelNotInQueue();
                }
            } else {
                Player player;
                if (args[0].equalsIgnoreCase("cp")) {
                    if (!this.saw.sawPlaying.isEmpty()) {
                        if (sender == this.saw.sawPlaying.firstEl()) {
                            player = Bukkit.getPlayer(sender.getName());
                            Location temp = player.getLocation();
                            Location cp = new Location(temp.getWorld(), temp.getX(), temp.getY(), temp.getZ());
                            this.saw.checkPoint = cp;
                            messages.checkpointSet();
                        } else {
                            messages.playPermissionError();
                        }
                    } else {
                        messages.playPermissionError();
                    }
                } else if (args[0].equalsIgnoreCase("checkpoint")) {
                    if (!this.saw.sawPlaying.isEmpty()) {
                        if (sender == this.saw.sawPlaying.firstEl()) {
                            if (this.saw.checkPoint != null) {
                                player = Bukkit.getPlayer(sender.getName());
                                if (this.saw.sawCheckpoints <= 0) {
                                    messages.nomoreCheckpoints();
                                } else {
                                    --this.saw.sawCheckpoints;
                                    player.teleport(this.saw.checkPoint);
                                    messages.sentCheckpoint();
                                }
                            } else {
                                messages.noCheckpoint();
                            }
                        } else {
                            messages.playPermissionError();
                        }
                    } else {
                        messages.playPermissionError();
                    }
                } else {
                    messages.syntaxError();
                }
            }
        } else if (args.length == 2) {
            messages.syntaxError();
        } else {
            messages.syntaxError();
        }

        return true;
    }
}
