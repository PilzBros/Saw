package com.pilzbros.saw.io;

public enum Setting {
    CheckForUpdates("CheckForUpdates", true),
    NotifyOnNewUpdates("NotifyOnNewUpdates", true),
    UseMySQL("Database.UseMySQL", false),
    MySQLConn("Database.MySQLConn", "jdbc:mysql://localhost:3306/minecraft"),
    MySQLUsername("Database.MySQLUSername", "root"),
    MySQLPassword("Database.MySQLPassword", "password");

    private String name;
    private Object def;

    private Setting(String Name, Object Def) {
        this.name = Name;
        this.def = Def;
    }

    public String getString() {
        return this.name;
    }

    public Object getDefault() {
        return this.def;
    }
}
