package net.bowline.firstjointeleport;

import net.bowline.firstjointeleport.Commands.FJTCommand;
import net.bowline.firstjointeleport.Util.ConfigManager;
import net.bowline.firstjointeleport.Util.MySQL;
import net.bowline.firstjointeleport.Util.SQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class FirstJoinTeleport extends Plugin implements Listener {

    private static Plugin plugin;
    private static boolean usingSQL = false;

    @Override
    public void onEnable() {
        this.plugin = this;

        ConfigManager.createDataFolderAndFile("config.yml");
        ConfigManager.createDataFolderAndFile("joined.yml");

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new FJTCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);

        try {
            Configuration configurationConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
            usingSQL = configurationConfig.getBoolean("settings.mysql.enabled");

            MySQL.connect();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        if (usingSQL)
            if (!SQL.tableExists("fjt_data"))
                SQL.createTable("fjt_data", "uuid");
    }

    @Override
    public void onDisable() {
        if (MySQL.isConnected())
            MySQL.disconnect();
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    @EventHandler
    public void onJoinFindCorrectServer(PostLoginEvent event) throws IOException {
        ProxiedPlayer player = event.getPlayer();
        Configuration joinedBeforeConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "joined.yml"));
        Configuration configurationConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));

        if (!configurationConfig.getBoolean("settings.enabled"))
            return;

        if (!usingSQL) {
            List<String> joinedBefore = joinedBeforeConfig.getStringList("joined-before");

            if (!joinedBefore.contains(player.getUniqueId().toString())) {
                event.getPlayer().connect(ProxyServer.getInstance().getServerInfo(configurationConfig.getString("settings.has-not-played-before-server")));
                joinedBefore.add(player.getUniqueId().toString());
                joinedBeforeConfig.set("joined-before", joinedBefore);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(joinedBeforeConfig, new File(getDataFolder(), "joined.yml"));
            }
        } else {
            if (!SQL.exists(event.getPlayer().getUniqueId().toString(), "uuid", "fjt_data")) {
                event.getPlayer().connect(ProxyServer.getInstance().getServerInfo(configurationConfig.getString("settings.has-not-played-before-server")));
                SQL.insertData("uuid", event.getPlayer().getUniqueId().toString(), "fjt_data");
            }
        }
    }
}
