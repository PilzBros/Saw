package com.pilzbros.saw;

import com.pilzbros.saw.io.InputOutput;
import com.pilzbros.saw.io.Setting;
import com.pilzbros.saw.io.Settings;
import com.pilzbros.saw.commands.SawAdminExe;
import com.pilzbros.saw.commands.SawExe;
import com.pilzbros.saw.database.Database;
import com.pilzbros.saw.database.MySQLConnection;
import com.pilzbros.saw.database.Update;
import com.pilzbros.saw.structs.DLL;
import com.pilzbros.saw.structs.Queue;
import com.pilzbros.saw.listener.PlayerListener;
import com.pilzbros.saw.manager.Manager;
import com.pilzbros.saw.manager.ScoreboardManager;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Saw extends JavaPlugin implements Listener {
    public final String pluginName = "Saw";
    public final String pluginVersion = "1.0";
    public final String sawPrefix;
    public final String sawAdminPrefix;
    public int sawTime;
    public int sawLives;
    public int sawCheckpoints;
    public int diamonds;
    public long lastCheckTime;
    public int defaultTime;
    public int defaultLives;
    public double defaultDiamonds;
    public int defaultCheckpoints;
    public boolean playPermitted;
    public boolean updateNeeded;
    public boolean setupNeeded;
    public final ScoreboardManager sawScoreboardManager;
    public final Manager sawManager;
    public Location startPoint;
    public Location winPoint;
    public Location returnPoint;
    public Location checkPoint;
    public final Database database;
    public final MySQLConnection connection;
    public Queue<CommandSender> sawPlaying;
    public Queue<CommandSender> sawQueue;
    public DLL<CommandSender> sawWait;
    public HashMap<Double, Location> checkpoints;
    public static Logger log = Logger.getLogger("Minecraft");
    public static Saw instance;
    public InputOutput IO;

    public Saw() {
        this.sawPrefix = ChatColor.RED + "[Saw] ";
        this.sawAdminPrefix = ChatColor.GREEN + "[Saw Admin] ";
        this.setupNeeded = false;
        this.checkpoints = new HashMap();
        this.sawPlaying = new Queue();
        this.sawQueue = new Queue();
        this.sawWait = new DLL();
        this.sawManager = new Manager(this);
        this.winPoint = null;
        this.startPoint = null;
        this.returnPoint = null;
        this.checkPoint = null;
        this.database = new Database(this);
        this.checkPoint = null;
        this.playPermitted = true;
        this.updateNeeded = false;
        this.connection = new MySQLConnection();
        this.diamonds = 15;
        this.defaultTime = 15;
        this.defaultLives = 3;
        this.defaultCheckpoints = 3;
        this.sawScoreboardManager = new ScoreboardManager(this);
    }

    public void onEnable() {
        this.getLogger().info("Saw v1.0 startup has been initiated...");
        instance = this;
        this.IO = new InputOutput(this);
        this.IO.LoadSettings();
        this.IO.PrepareDB();
        this.IO.LoadLocations();
        this.IO.LoadGame();
        this.IO.loadCheckpoints();
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getCommand("saw").setExecutor(new SawExe(this));
        this.getCommand("sawadmin").setExecutor(new SawAdminExe(this));
        this.database.bootUp();
        this.sawManager.bootup();
        if (Settings.getGlobalBoolean(Setting.CheckForUpdates)) {
            new Update(70142, "", this);
        } else {
            this.getLogger().info(this.sawPrefix + "Automatic update checking has been disabled!");
        }

        this.setupCheck();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                Saw.this.sawManager.autoCheck();
            }
        }, 5L, 5L);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Saw.TimeEvent(this), 20L, 20L);
        this.getLogger().info("Saw v1.0startup complete!");
    }

    public void onDisable() {
        this.sawManager.shutdown();
        InputOutput.freeConnection();
        this.getLogger().info("Saw v1.0 shutdown complete!");
    }

    public void setupCheck() {
        if (this.startPoint == null || this.winPoint == null || this.returnPoint == null) {
            this.setupNeeded = true;
        }

    }

    class TimeEvent implements Runnable {
        Saw saw;

        public TimeEvent(Saw ref) {
            this.saw = ref;
        }

        public void run() {
            if (System.currentTimeMillis() - this.saw.lastCheckTime >= 60000L) {
                int timePassed = (int)Math.round((double)(System.currentTimeMillis() - this.saw.lastCheckTime) / 60000.0D);
                this.saw.lastCheckTime = System.currentTimeMillis();
                if (!this.saw.sawPlaying.isEmpty()) {
                    if (this.saw.sawTime <= 0) {
                        this.saw.sawManager.timesUp();
                    } else {
                        this.saw.sawTime -= timePassed;
                        if (this.saw.sawTime <= 0) {
                            this.saw.sawManager.timesUp();
                        }
                    }
                }

            }
        }
    }
}
