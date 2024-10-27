package com.pilzbros.saw.manager;

import com.pilzbros.saw.Saw;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class ScoreboardManager {
    BoardManager sawScore;
    private Saw saw;

    public ScoreboardManager(Saw ref) {
        this.saw = ref;
    }

    public void removeBoard(String name) {
        BoardManager blank = new BoardManager("test", "dummy", DisplaySlot.SIDEBAR);
        blank.setScoreboard(Bukkit.getPlayer(name));
    }

    public void displayQueueTimer() {
        BoardManager sawQueue = new BoardManager("Saw", ChatColor.GREEN + "Saw Wait", DisplaySlot.SIDEBAR);
        BoardManager blank = new BoardManager("test", "dummy", DisplaySlot.SIDEBAR);
        sawQueue.setObjectiveScore("In Line: ", this.saw.sawQueue.size());
        if (!this.saw.sawQueue.isEmpty()) {
            Player[] var6 = Bukkit.getOnlinePlayers();
            int var5 = var6.length;

            for(int var4 = 0; var4 < var5; ++var4) {
                Player online = var6[var4];
                if (this.saw.sawQueue.inQueue(online)) {
                    sawQueue.setScoreboard(online);
                } else {
                    blank.setScoreboard(online);
                }
            }
        }

    }

    public void displaySawTimer() {
        BoardManager sawScore = new BoardManager("Saw", ChatColor.GREEN + "Saw", DisplaySlot.SIDEBAR);
        sawScore.setObjectiveScore("Time Left: ", this.saw.sawTime);
        sawScore.addObjectiveScore("Lives: ", this.saw.sawLives);
        sawScore.addObjectiveScore("Checkpoints: ", this.saw.sawCheckpoints);
        if (!this.saw.sawPlaying.isEmpty()) {
            Player[] var5 = Bukkit.getOnlinePlayers();
            int var4 = var5.length;

            for(int var3 = 0; var3 < var4; ++var3) {
                Player online = var5[var3];
                if (online == this.saw.sawPlaying.firstEl()) {
                    sawScore.setScoreboard(online);
                }
            }
        }

    }
}