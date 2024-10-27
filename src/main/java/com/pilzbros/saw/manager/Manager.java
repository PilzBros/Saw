package com.pilzbros.saw.manager;

import com.pilzbros.saw.Saw;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Manager {
    private Saw saw;
    private PotionEffect potionEffect;
    private PotionEffect potionRun;
    private PotionEffect potionJump;

    public Manager(Saw ref) {
        this.potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, 100000, 1);
        this.potionRun = new PotionEffect(PotionEffectType.SPEED, 100000, 2);
        this.potionJump = new PotionEffect(PotionEffectType.JUMP, 100000, 1);
        this.saw = ref;
    }

    public void autoCheck() {
        this.auto();
        this.saw.sawScoreboardManager.displaySawTimer();
        this.saw.sawScoreboardManager.displayQueueTimer();
    }

    public void bootup() {
        this.reset();
    }

    public void shutdown() {
        this.shutdownNotify();
    }

    public void auto() {
        if (this.saw.sawPlaying.isEmpty() && !this.saw.sawWait.isEmpty()) {
            this.queuePlayerCheck();
            this.start((CommandSender)this.saw.sawWait.deleteFromHead());
        }

    }

    public void reset() {
        this.saw.sawLives = this.saw.defaultLives;
        this.saw.sawTime = this.saw.defaultTime;
        this.saw.checkPoint = null;
        this.saw.sawCheckpoints = this.saw.defaultCheckpoints;
        this.saw.sawWait.clear();
    }

    private void queuePlayerCheck() {
        for(Player player = Bukkit.getServer().getPlayer(((CommandSender)this.saw.sawWait.firstEl()).getName()); !player.isOnline(); player = Bukkit.getServer().getPlayer(((CommandSender)this.saw.sawWait.firstEl()).getName())) {
            this.saw.sawWait.dequeue();
        }

    }

    public void deathCheck(Player p, Location loc) {
        if (p == this.saw.sawPlaying.firstEl()) {
            if (this.saw.sawLives <= 0) {
                this.death();
            } else {
                --this.saw.sawLives;
                if (this.saw.sawLives == 0) {
                    this.death();
                } else if (this.saw.checkPoint == null) {
                    this.saw.checkPoint = p.getLocation();
                }
            }
        }

    }

    public void respawnCheck(Player p, PlayerRespawnEvent event) {
    }

    public void commandCheck(PlayerCommandPreprocessEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayer().getName());
        if (player == this.saw.sawPlaying.firstEl() && (!player.hasPermission("saw.admin") || !player.hasPermission("saw.*"))) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "[Saw] " + ChatColor.WHITE + "You cannot use commands other than " + ChatColor.YELLOW + "/saw" + ChatColor.WHITE + " while playing");
        }

    }

    public void teleportCheck(Player p, PlayerTeleportEvent event) {
        if (p == this.saw.sawPlaying.firstEl() && (!p.hasPermission("saw.admin") || !p.hasPermission("saw.*"))) {
            event.getTo().equals(this.saw.startPoint);
        }

    }

    public void moveCheck(PlayerMoveEvent event) {
        Player p = Bukkit.getPlayer(event.getPlayer().getName());
        if (p == this.saw.sawPlaying.firstEl()) {
            Location pl = p.getLocation();
            if (pl.getBlockX() == this.saw.winPoint.getBlockX() && pl.getBlockY() == this.saw.winPoint.getBlockY() && pl.getBlockZ() == this.saw.winPoint.getBlockZ()) {
                this.winner(p);
            }
        }

    }

    private void start(CommandSender s) {
        this.reset();
        Player player = Bukkit.getPlayer(s.getName());
        this.playerSetup(player);
        s.sendMessage(ChatColor.RED + "[Saw] " + ChatColor.WHITE + " Begin! You have " + this.saw.defaultTime + " minutes & " + this.saw.defaultLives + " lives to try and beat saw!");
        if (this.saw.startPoint != null) {
            player.teleport(this.saw.startPoint);
            this.saw.sawPlaying.enqueue(s);
        } else {
            Bukkit.getServer().dispatchCommand(s, "warp saw2");
            this.saw.sawPlaying.enqueue(s);
        }

    }

    private void playerSetup(Player player) {
        player.setAllowFlight(false);
        player.setFoodLevel(20);
        player.setFoodLevel(20);
        player.addPotionEffect(this.potionEffect);
        player.addPotionEffect(this.potionJump);
        player.addPotionEffect(this.potionRun);
    }

    private void playerRestore(CommandSender player) {
        Player p = Bukkit.getPlayer(player.getName());
        p.removePotionEffect(this.potionEffect.getType());
        p.removePotionEffect(this.potionJump.getType());
        p.removePotionEffect(this.potionRun.getType());
    }

    public void timesUp() {
        CommandSender player = (CommandSender)this.saw.sawPlaying.firstEl();
        this.saw.sawScoreboardManager.removeBoard(player.getName());
        this.playerRestore(player);
        this.endTeleport(player);
        player.sendMessage(ChatColor.RED + "[Saw] " + ChatColor.WHITE + " Game Over! You ran out of time!");
        this.saw.sawPlaying.clear();
    }

    public void quit() {
        CommandSender player = (CommandSender)this.saw.sawPlaying.firstEl();
        this.saw.sawScoreboardManager.removeBoard(player.getName());
        this.playerRestore(player);
        this.endTeleport(player);
        player.sendMessage(ChatColor.RED + "[Saw] " + ChatColor.WHITE + " You've quit playing Saw!");
        this.saw.sawPlaying.clear();
    }

    public void end() {
        CommandSender player = (CommandSender)this.saw.sawPlaying.firstEl();
        this.saw.sawScoreboardManager.removeBoard(player.getName());
        this.playerRestore(player);
        this.endTeleport(player);
        player.sendMessage(ChatColor.RED + "[Saw] " + ChatColor.WHITE + " An administrator has ended your game of Saw!");
        this.saw.sawPlaying.clear();
    }

    private void death() {
        CommandSender player = (CommandSender)this.saw.sawPlaying.firstEl();
        this.saw.sawScoreboardManager.removeBoard(player.getName());
        this.playerRestore(player);
        this.endTeleport(player);
        player.sendMessage(ChatColor.RED + "[Saw] " + ChatColor.WHITE + " Game over! You died too many times");
        this.saw.sawPlaying.clear();
    }

    private void endTeleport(CommandSender player) {
        Player p = Bukkit.getPlayer(player.getName());
        this.playerRestore(player);
        p.teleport(this.saw.returnPoint);
        p.teleport(this.saw.returnPoint);
    }

    private void winner(Player player) {
        this.saw.sawScoreboardManager.removeBoard(player.getName());
        this.saw.sawPlaying.clear();
        this.endTeleport(player);
        player.sendMessage(ChatColor.RED + "[Saw] " + ChatColor.GREEN + ChatColor.BOLD + "YOU WIN! Thanks for playing saw");
        this.grantDiamonds(player);
    }

    private void grantDiamonds(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack itemstack = new ItemStack(Material.DIAMOND, this.saw.diamonds);
        inventory.addItem(new ItemStack[]{itemstack});
        player.sendMessage(ChatColor.RED + "[Saw] " + ChatColor.WHITE + "Your spoils of " + this.saw.diamonds + " diamonds has been added to your inventory!");
    }

    private void shutdownNotify() {
        CommandSender player;
        while(!this.saw.sawQueue.isEmpty()) {
            player = (CommandSender)this.saw.sawQueue.dequeue();
            player.sendMessage(ChatColor.RED + "[Saw] " + ChatColor.WHITE + " A reload has forced you out of the Saw queue. Once the reload is complete, you will need to join the line again");
            this.saw.sawScoreboardManager.removeBoard(player.getName());
        }

        while(!this.saw.sawPlaying.isEmpty()) {
            player = (CommandSender)this.saw.sawPlaying.dequeue();
            this.endTeleport(player);
            this.saw.sawScoreboardManager.removeBoard(player.getName());
            player.sendMessage(ChatColor.RED + "[Saw] " + ChatColor.WHITE + " A reload has forced you out of Saw in order to be fair. Once the reload is complete, you'll need to start again");
        }

    }
}