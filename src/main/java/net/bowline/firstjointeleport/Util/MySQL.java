package net.bowline.firstjointeleport.Util;

import net.bowline.firstjointeleport.FirstJoinTeleport;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQL {

    private static Connection con;

    public static Connection getConnection() {
        return MySQL.con;
    }

    public static void setConnection(final String host, final String user, final String password, final String database, final String port) {
        if (host == null || user == null || password == null || database == null)
            return;
        disconnect(false);
        try {
            MySQL.con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
            ProxyServer.getInstance().getLogger().info(ChatColor.GREEN + "MySQL Connected.");
        }
        catch (final Exception e) {
            ProxyServer.getInstance().getLogger().info(ChatColor.GREEN + "MySQL Connection Error: " + e.getMessage());
        }
    }

    public static void connect() {
        try {
            connect(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void connect(final boolean message) throws IOException {
        Configuration configurationConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(FirstJoinTeleport.getPlugin().getDataFolder(), "config.yml"));

        final String host = configurationConfig.getString("settings.mysql.host");
        final String user = configurationConfig.getString("settings.mysql.user");
        final String password = configurationConfig.getString("settings.mysql.password");
        final String database =configurationConfig.getString("settings.mysql.database");
        final String port = configurationConfig.getString("settings.mysql.port");
        if (isConnected()) {
            if (message)
                ProxyServer.getInstance().getLogger().info(ChatColor.GREEN + "MySQL Connection Error: Already connected!");
        }
        else if (host.equalsIgnoreCase(""))
            ProxyServer.getInstance().getLogger().info(ChatColor.GREEN + "MySQL Connection Error: Host is empty");
        else if (user.equalsIgnoreCase(""))
            ProxyServer.getInstance().getLogger().info(ChatColor.GREEN + "MySQL Connection Error: User is empty");
        else if (database.equalsIgnoreCase(""))
            ProxyServer.getInstance().getLogger().info(ChatColor.GREEN + "MySQL Connection Error: Database is empty");
        else if (port.equalsIgnoreCase(""))
            ProxyServer.getInstance().getLogger().info(ChatColor.GREEN + "MySQL Connection Error: Port is empty");
        else
            setConnection(host, user, password, database, port);
    }

    public static void disconnect() {
        disconnect(true);
    }

    private static void disconnect(final boolean message) {
        try {
            if (isConnected()) {
                MySQL.con.close();
                ProxyServer.getInstance().getLogger().info(ChatColor.RED + "MySQL Disconnected");
            }
            else if (message)
                ProxyServer.getInstance().getLogger().info(ChatColor.RED + "MySQL Connection Error: No existing connection");
        }
        catch (final Exception e) {
            if (message)
                ProxyServer.getInstance().getLogger().info(ChatColor.GREEN + "MySQL Disconnect Error: " + e.getMessage());
        }
        MySQL.con = null;
    }

    public static void reconnect() {
        disconnect();
        connect();
    }

    public static boolean isConnected() {
        if (MySQL.con != null)
            try {
                return !MySQL.con.isClosed();
            }
            catch (final Exception e) { }
        return false;
    }

    public static void update(final String command) throws IOException {
        if (command == null)
            return;
        connect(false);
        try {
            final Statement st = getConnection().createStatement();
            st.executeUpdate(command);
            st.close();
        }
        catch (final Exception e) {
            ProxyServer.getInstance().getLogger().info(ChatColor.RED + "MySQL Update:");
            ProxyServer.getInstance().getLogger().info(ChatColor.RED + "Command: " + command);
            ProxyServer.getInstance().getLogger().info(ChatColor.RED + "Error: " + e.getMessage());
        }
    }

    public static ResultSet query(final String command) throws IOException {
        if (command == null)
            return null;
        connect(false);
        ResultSet rs = null;
        try {
            final Statement st = getConnection().createStatement();
            rs = st.executeQuery(command);
        }
        catch (final Exception e) {
            ProxyServer.getInstance().getLogger().info(ChatColor.RED + "MySQL Query:");
            ProxyServer.getInstance().getLogger().info(ChatColor.RED + "Command: " + command);
            ProxyServer.getInstance().getLogger().info(ChatColor.RED + "Error: " + e.getMessage());
        }
        return rs;
    }
}
