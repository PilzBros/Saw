package com.pilzbros.saw.messages;

import com.pilzbros.saw.Saw;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Messages {
    private Saw saw;
    private CommandSender player;

    public Messages(Saw ref, CommandSender sender) {
        this.saw = ref;
        this.player = sender;
    }

    public void playPermissionError() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.RED + " You don't have permission to access Saw!");
    }

    public void checkpointSet() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.YELLOW + "Checkpoint set!");
    }

    public void noCheckpoint() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.RED + "You have either not reached or not set a checkpoint to go back to");
    }

    public void sentCheckpoint() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.YELLOW + "You have been sent back to your last checkpoint");
    }

    public void nomoreCheckpoints() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.RED + "You have used all of your checkpoint teleports");
    }

    public void sawDescription() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + " Saw is this servers unique adventure game that will test all of your skills, to see if you're good enough to walk away with 15 diamonds!");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.GRAY + "--Commands--");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "Play - /saw play");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "Quit - /saw quit");
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + "Leave Line - /saw cancel");
    }

    public void syntaxError() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.RED + " Whoops. Incorrect syntax");
    }

    public void waitingQueue(int i) {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + " Only one persion can play Saw at a time. You have been added to the queue, and are number " + ChatColor.BLUE + i + ChatColor.WHITE + ". You will be automatically teleported when it is your turn. To cancel your turn, type" + ChatColor.YELLOW + " /saw cancel");
    }

    public void waitingQueueFirst() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + " Only one persion can play Saw at a time. You have been added to the queue, and are first in line. You will be automatically teleported when it is your turn. To cancel your turn, type" + ChatColor.YELLOW + " /saw cancel");
    }

    public void cancelNotInQueue() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.RED + " You were not in line to play Saw, therefore you cannot cancel your place in line!");
    }

    public void alreadyInQueue() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + " You're already in line");
    }

    public void leaveQueue() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + " You have cancelled your place in line to play saw!");
    }

    public void alreadyPlaying() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.RED + " You're already playing saw. You cannot join the queue again until after you're done");
    }

    public void quitPlaying() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.WHITE + " You have quit playing saw!");
    }

    public void currentlyPlaying() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.GREEN + " You're still playing Saw!" + ChatColor.WHITE + " If you want to leave, type " + ChatColor.YELLOW + "/saw quit");
    }

    public void quitNLNP() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.RED + " You are not playing, nor are you in line for Saw");
    }

    public void setupNeeded() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.RED + "Unable to play SAW at the moment. Initial setup by an admin is required");
    }

    public void adminSetupNeeded() {
        this.player.sendMessage(this.saw.sawPrefix + ChatColor.RED + "You need to setup saw before you can play. Type " + ChatColor.YELLOW + "      /sawadmin setup");
    }
}