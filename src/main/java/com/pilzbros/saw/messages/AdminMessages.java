package com.pilzbros.saw.messages;

import com.pilzbros.saw.Saw;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AdminMessages {
    private Saw saw;
    private CommandSender player;

    public AdminMessages(Saw s, CommandSender sender) {
        this.player = sender;
        this.saw = s;
    }

    public void prefix() {
        this.player.sendMessage(this.saw.sawAdminPrefix);
    }

    public void adminCommands() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "Saw Admin Commands");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.GRAY + "--Commands--");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "/sawadmin - Current Game / Queue / Commands");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "/sawadmin clear - Clear wait Queue");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "/sawadmin end - End current game");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "/sawadmin reload - Reload plugin");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "/sawadmin set");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "                  time #  - Sets default game time");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "                  lives #  - Sets default game lives");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "                  diamonds #  - Sets default game diamonds");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "                  checkpoints #  - Sets default checkpoints");
    }

    public void playingDetails() {
        this.player.sendMessage(ChatColor.YELLOW + "Current Saw Game");
        this.player.sendMessage("Player: " + ChatColor.BLUE + ((CommandSender)this.saw.sawPlaying.firstEl()).getName());
        this.player.sendMessage("Time Left: " + ChatColor.RED + this.saw.sawTime);
        this.player.sendMessage("Lives Left: " + ChatColor.RED + this.saw.sawLives);
        this.player.sendMessage("Checkpoints Left: " + ChatColor.RED + this.saw.sawCheckpoints);
    }

    public void queueDetails() {
        this.player.sendMessage(" ");
        this.player.sendMessage(ChatColor.YELLOW + "Queue");
        this.player.sendMessage("Players: " + ChatColor.RED + this.saw.sawWait.size());

        for(int i = 0; i < this.saw.sawWait.size(); ++i) {
            this.player.sendMessage("Players: " + ChatColor.RED + ((CommandSender)this.saw.sawWait.get(i)).getName());
        }

    }

    public void sawDetails() {
        this.player.sendMessage(" ");
        this.player.sendMessage(ChatColor.YELLOW + "Settings");
        this.player.sendMessage("Game Time: " + ChatColor.RED + this.saw.defaultTime);
        this.player.sendMessage("Lives: " + ChatColor.RED + this.saw.defaultLives);
        this.player.sendMessage("Diamonds: " + ChatColor.RED + this.saw.diamonds);
        this.player.sendMessage("Checkpoints: " + ChatColor.RED + this.saw.defaultCheckpoints);
        this.player.sendMessage(" ");
        this.player.sendMessage(ChatColor.YELLOW + "Locations");
        if (this.saw.startPoint != null) {
            this.player.sendMessage("Start Point: " + ChatColor.GREEN + "Set");
        } else {
            this.player.sendMessage("Start Point: " + ChatColor.RED + "Not set");
        }

        if (this.saw.winPoint != null) {
            this.player.sendMessage("Win Point: " + ChatColor.GREEN + "Set");
        } else {
            this.player.sendMessage("Win Point: " + ChatColor.RED + "Not set");
        }

        if (this.saw.returnPoint != null) {
            this.player.sendMessage("Return Point: " + ChatColor.GREEN + "Set");
        } else {
            this.player.sendMessage("Return Point: " + ChatColor.RED + "Not set");
        }

    }

    public void PermissionError() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.RED + " You don't have permission to this command!");
    }

    public void syntaxError() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.RED + "Syntax error");
    }

    public void reboot() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.WHITE + "Saw has been reloaded!");
    }

    public void end() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.WHITE + "User currently playing Saw has been kicked from game");
    }

    public void endNone() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.WHITE + "There is not a current game of Saw to end");
    }

    public void clearQueue() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.WHITE + "The wait queue has been cleared");
    }

    public void settingsUpdated() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.WHITE + "Settings updated successfully");
    }

    public void checkpointsUpdated() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.WHITE + "Checkpoints updated");
    }

    public void checkpointAdded() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.WHITE + "Checkpoint added!");
    }

    public void winpointUpdated() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.WHITE + "Win point updated");
    }

    public void startpointUpdated() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.WHITE + "Start point updated");
    }

    public void returnpointUpdated() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.WHITE + "Return point updated");
    }

    public void setupNotNeeded() {
        this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.WHITE + "Saw is already setup. You can update settings & locations by typing /saw set [property] [value]. Please refer to documentation for commands & properties");
    }
}
