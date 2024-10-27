package com.pilzbros.saw.io;

import com.pilzbros.saw.Saw;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class InputOutput {
    private static Connection connection;
    public static YamlConfiguration global;
    private Saw saw;
    public static HashMap<Integer, String[]> jailStickParameters = new HashMap();

    public InputOutput(Saw saw) {
        if (!Saw.instance.getDataFolder().exists()) {
            try {
                Saw.instance.getDataFolder().mkdir();
            } catch (Exception var3) {
            }
        }

        global = new YamlConfiguration();
        connection = null;
        this.saw = saw;
    }

    public static synchronized Connection getConnection() {
        if (connection == null) {
            connection = createConnection();
        }

        try {
            if (connection.isClosed()) {
                connection = createConnection();
            }
        } catch (SQLException var1) {
            var1.printStackTrace();
        }

        return connection;
    }

    private static Connection createConnection() {
        try {
            Connection ret;
            if (Settings.getGlobalBoolean(Setting.UseMySQL)) {
                Class.forName("com.mysql.jdbc.Driver");
                ret = DriverManager.getConnection(Settings.getGlobalString(Setting.MySQLConn), Settings.getGlobalString(Setting.MySQLUsername), Settings.getGlobalString(Setting.MySQLPassword));
                ret.setAutoCommit(false);
                Saw.log.log(Level.WARNING, "[SAW] Using MySQL - Connection succeeded");
                return ret;
            } else {
                Class.forName("org.sqlite.JDBC");
                ret = DriverManager.getConnection("jdbc:sqlite:" + (new File(Saw.instance.getDataFolder().getPath(), "saw.sqlite")).getPath());
                ret.setAutoCommit(false);
                Saw.log.log(Level.WARNING, "[SAW] Using SQLite - Connection succeeded");
                return ret;
            }
        } catch (ClassNotFoundException var1) {
            Saw.log.log(Level.SEVERE, "[SAW] Connection to the MySQL database failed. Plugin startup terminated");
            var1.printStackTrace();
            return null;
        } catch (SQLException var2) {
            Saw.log.log(Level.SEVERE, "[SAW] Connection to the MySQL database failed. Plugin startup terminated");
            var2.printStackTrace();
            return null;
        }
    }

    public static synchronized void freeConnection() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException var2) {
                var2.printStackTrace();
            }
        }

    }

    public void LoadSettings() {
        try {
            if (!(new File(Saw.instance.getDataFolder(), "global.yml")).exists()) {
                global.save(new File(Saw.instance.getDataFolder(), "global.yml"));
            }

            global.load(new File(Saw.instance.getDataFolder(), "global.yml"));
            Setting[] var4;
            int var3 = (var4 = Setting.values()).length;

            for(int var2 = 0; var2 < var3; ++var2) {
                Setting s = var4[var2];
                if (global.get(s.getString()) == null) {
                    global.set(s.getString(), s.getDefault());
                }
            }

            global.save(new File(Saw.instance.getDataFolder(), "global.yml"));
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        } catch (IOException var6) {
            var6.printStackTrace();
        } catch (InvalidConfigurationException var7) {
            var7.printStackTrace();
        }

    }

    public void LoadLocations() {
        if (this.getLocation("Start") != null) {
            this.saw.startPoint = this.getLocation("Start");
        }

        if (this.getLocation("Win") != null) {
            this.saw.winPoint = this.getLocation("Win");
        }

        if (this.getLocation("Return") != null) {
            this.saw.returnPoint = this.getLocation("Return");
        }

    }

    public void LoadGame() {
        if (this.getSetting("Time") != -1) {
            this.saw.defaultTime = this.getSetting("Time");
        }

        if (this.getSetting("Lives") != -1) {
            this.saw.defaultLives = this.getSetting("Lives");
        }

        if (this.getSetting("Checkpoints") != -1) {
            this.saw.defaultLives = this.getSetting("Checkpoints");
        }

        if (this.getSetting("Diamonds") != -1) {
            this.saw.diamonds = this.getSetting("Diamonds");
        }

    }

    public int getSetting(String name) {
        try {
            PreparedStatement ps = null;
            ResultSet result = null;
            Connection conn = getConnection();
            ps = conn.prepareStatement("SELECT Value FROM saw_settings WHERE Name = ?");
            ps.setString(1, name);
            result = ps.executeQuery();
            if (result.next()) {
                return result.getInt("Value");
            }

            result.close();
            ps.close();
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

        return -1;
    }

    public void loadCheckpoints() {
        try {
            PreparedStatement ps = null;
            ResultSet result = null;
            Connection conn = getConnection();
            ps = conn.prepareStatement("SELECT `X`, `Y`, `Z`, `World` FROM `saw_checkpoints`");
            result = ps.executeQuery();

            int count;
            for(count = 0; result.next(); ++count) {
                Location l = new Location(this.saw.getServer().getWorld(result.getString("World")), result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"));
                Double x = l.getX();
                Double y = l.getY();
                Double z = l.getZ();
                int x1 = x.intValue();
                int y1 = y.intValue();
                int z1 = z.intValue();
                double key = (double)(x1 + y1 + z1);
                this.saw.checkpoints.put(key, l);
            }

            Saw.log.log(Level.INFO, "[SAW] Loaded " + count + " checkpoint(s)");
        } catch (SQLException var14) {
            Saw.log.log(Level.SEVERE, "[SAW] Error while loading checkpoints - " + var14.getMessage());
            var14.printStackTrace();
        }

    }

    public void testSettings() {
        try {
            PreparedStatement ps = null;
            ResultSet set = null;
            Connection conn = getConnection();
            ps = conn.prepareStatement("SELECT * FROM saw_settings");
            set = ps.executeQuery();

            while(set.next()) {
            }
        } catch (SQLException var4) {
            Saw.log.log(Level.SEVERE, "[SAW] Error while loading test settings from the database! - " + var4.getMessage());
            var4.printStackTrace();
        }

    }

    public Location getLocation(String name) {
        try {
            String sql = "SELECT `X`, `Y`, `Z`, `World` FROM `saw_locations` WHERE `Name` = ? LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()) {
                Location l = new Location(this.saw.getServer().getWorld(result.getString("World")), result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z"));
                return l;
            }
        } catch (SQLException var6) {
            Saw.log.log(Level.WARNING, "[Saw]: Had an issue loading DB locations");
        }

        return null;
    }

    public void PrepareDB() {
        Connection conn = getConnection();
        Statement st = null;

        try {
            st = conn.createStatement();
            if (Settings.getGlobalBoolean(Setting.UseMySQL)) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS `saw_locations` ( `Name` varchar(250) NOT NULL DEFAULT '', `X` double DEFAULT NULL, `Y` double DEFAULT NULL, `Z` double DEFAULT NULL, `World` varchar(250) DEFAULT NULL , PRIMARY KEY (`Name`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS `saw_checkpoints` ( `X` double DEFAULT NULL, `Y` double DEFAULT NULL, `Z` double DEFAULT NULL, `World` varchar(250) DEFAULT NULL , PRIMARY KEY (`Name`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS `saw_settings` ( `Name` varchar(250) NOT NULL, `Value` int DEFAULT NULL, PRIMARY KEY (`Name`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            } else {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS \"saw_settings\" (\"Name\" VARCHAR PRIMARY KEY  NOT NULL , \"Value\" DOUBLE)");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS \"saw_locations\" (\"Name\" VARCHAR PRIMARY KEY  NOT NULL , \"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" VARCHAR, STRING)");
                st.executeUpdate("CREATE TABLE IF NOT EXISTS \"saw_checkpoints\" (\"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"World\" VARCHAR, STRING)");
            }

            conn.commit();
            st.close();
        } catch (SQLException var4) {
            var4.printStackTrace();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public void updateSetting(int value, String setting) {
        try {
            Connection conn = getConnection();
            String sql;
            PreparedStatement preparedStatement;
            if (this.getSetting(setting) == -1) {
                sql = "INSERT INTO saw_settings (Name, Value) VALUES (?,?)";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, setting);
                preparedStatement.setDouble(2, (double)value);
                preparedStatement.executeUpdate();
            } else {
                sql = "UPDATE `saw_settings` SET `Value` = ? WHERE `Name` = ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setDouble(1, (double)value);
                preparedStatement.setString(2, setting);
                preparedStatement.executeUpdate();
                connection.commit();
            }

            conn.commit();
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

    }

    public void updateLocation(Location location, String name) {
        try {
            Connection conn = getConnection();
            String sql;
            PreparedStatement preparedStatement;
            if (this.getLocation(name) == null) {
                sql = "INSERT INTO saw_locations (Name, X, Y, Z, World) VALUES (?,?,?,?,?)";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setDouble(2, location.getX());
                preparedStatement.setDouble(3, location.getY());
                preparedStatement.setDouble(4, location.getZ());
                preparedStatement.setString(5, location.getWorld().getName());
                preparedStatement.executeUpdate();
            } else {
                sql = "UPDATE `saw_locations` SET `X` = ?, `Y` = ?, `Z` = ?, `World` = ? WHERE `Name` = ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setDouble(1, location.getX());
                preparedStatement.setDouble(2, location.getY());
                preparedStatement.setDouble(3, location.getZ());
                preparedStatement.setString(4, location.getWorld().getName());
                preparedStatement.setString(5, name);
                preparedStatement.executeUpdate();
                connection.commit();
            }

            conn.commit();
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

    }

    public void storeCheckpoint(Location location) {
        try {
            Connection conn = getConnection();
            String sql = "INSERT INTO saw_checkpoints (X, Y, Z, World) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            Double x = location.getX();
            Double y = location.getY();
            Double z = location.getZ();
            int x1 = x.intValue();
            int y1 = y.intValue();
            int z1 = z.intValue();
            double key = (double)(x1 + y1 + z1);
            this.saw.checkpoints.put(key, location);
            preparedStatement.setDouble(1, (double)x1);
            preparedStatement.setDouble(2, (double)y1);
            preparedStatement.setDouble(3, (double)z1);
            preparedStatement.setString(4, location.getWorld().getName());
            preparedStatement.executeUpdate();
            conn.commit();
        } catch (SQLException var13) {
            Saw.log.log(Level.WARNING, "[Saw]: Error while storing checkpoint to DB");
            var13.printStackTrace();
        }

    }
}
