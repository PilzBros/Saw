package com.pilzbros.saw.manager;

import com.pilzbros.saw.Saw;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Setup {
    private Saw saw;
    CommandSender player;
    public int step;

    public Setup(Saw ref, CommandSender player) {
        this.saw = ref;
        this.step = 0;
        this.player = player;
    }

    public void setupCalled() {
        if (this.saw.setupNeeded) {
            if (this.step == 0) {
                this.startSetup();
            }
        } else {
            this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.WHITE + " Setup");
        }

    }

    public void commandCalled(int step) {
        this.step = step + 1;
        this.continueSetup();
    }

    private void startSetup() {
        ++this.step;
        this.continueSetup();
    }

    private void continueSetup() {
        if (this.step == 1) {
            this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.GREEN + "Setup: " + ChatColor.WHITE + "First, you must set the point at which players will be teleported to when they start the game. Stand on the desired block, and type " + ChatColor.YELLOW + "/sawadmin set startpoint");
        } else if (this.step == 2) {
            this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.GREEN + "Setup: " + ChatColor.WHITE + "Next, you must set the block that will be the winning point. Players will win the game when they stand on this block. Go to the desired block, and type " + ChatColor.YELLOW + "/sawadmin set winpoint");
        } else if (this.step == 3) {
            this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.GREEN + "Setup: " + ChatColor.WHITE + "Next, you must set where players will be teleported when they win/lose/quit the game. Stand on the desired block, and type " + ChatColor.YELLOW + "/sawadmin set returnpoint");
        } else if (this.step == 4) {
            this.player.sendMessage(this.saw.sawAdminPrefix + ChatColor.GREEN + "Setup: " + ChatColor.WHITE + "Success! Saw has been setup. You can change game values (time/lives/checkpoints/diamonds) by typing " + ChatColor.YELLOW + "/sawadmin set [time/lives/checkpoints/diamonds] [#]");
            this.saw.setupNeeded = false;
            this.saw.sawManager.reset();
        }

    }
}
