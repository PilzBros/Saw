package com.pilzbros.saw.listener;

import com.pilzbros.saw.io.Setting;
import com.pilzbros.saw.io.Settings;
import com.pilzbros.saw.Saw;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {
    private Saw saw;

    public PlayerListener(Saw ref) {
        this.saw = ref;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();
        if ((player.hasPermission("saw.*") || player.hasPermission("saw.admin")) && this.saw.updateNeeded && Settings.getGlobalBoolean(Setting.NotifyOnNewUpdates)) {
            player.sendMessage(ChatColor.GREEN + this.saw.sawPrefix + ChatColor.YELLOW + "An update for SAW is available for update! Please update to get the latest features and bug fixes!");
        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerJoinEvent evt) {
        if (!this.saw.sawPlaying.isEmpty()) {
            OfflinePlayer playing = Bukkit.getOfflinePlayer(((CommandSender)this.saw.sawPlaying.firstEl()).getName());
            if (playing.getName().equals(evt.getPlayer().getName())) {
                this.saw.sawPlaying.clear();
            }
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!this.saw.sawPlaying.isEmpty()) {
            Player player = event.getEntity();
            Location loc = player.getLocation();
            this.saw.sawManager.deathCheck(player, loc);
        }

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!this.saw.sawPlaying.isEmpty()) {
            if (player == this.saw.sawPlaying.firstEl()) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.saw, new Runnable() {
                    public void run() {
                        Player player = Bukkit.getPlayer(((CommandSender)PlayerListener.this.saw.sawPlaying.firstEl()).getName());
                        if (PlayerListener.this.saw.checkPoint == null && PlayerListener.this.saw.checkpoints.isEmpty()) {
                            player.teleport(PlayerListener.this.saw.startPoint);
                        } else {
                            player.teleport(PlayerListener.this.saw.checkPoint);
                        }

                    }
                }, 1L);
            }

            this.saw.sawManager.respawnCheck(player, event);
        }

    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!event.isCancelled()) {
            if (!this.saw.sawPlaying.isEmpty() && !event.getMessage().toLowerCase().startsWith("/saw")) {
                this.saw.sawManager.commandCheck(event);
            }

        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!this.saw.sawPlaying.isEmpty()) {
            Player player = event.getPlayer();
            this.saw.sawManager.teleportCheck(player, event);
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!this.saw.sawPlaying.isEmpty() && this.saw.sawPlaying.firstEl() == event.getPlayer()) {
            this.saw.sawManager.moveCheck(event);
        }

    }
}
