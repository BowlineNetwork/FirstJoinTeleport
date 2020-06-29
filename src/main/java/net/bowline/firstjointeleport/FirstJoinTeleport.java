package net.bowline.firstjointeleport;

import net.bowline.firstjointeleport.Commands.FJTCommand;
import net.bowline.firstjointeleport.Util.ConfigManager;
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

public final class FirstJoinTeleport extends Plugin implements Listener {

    private static Plugin plugin;

    @Override
    public void onEnable() {
        this.plugin = this;

        ConfigManager.createDataFolderAndFile("config.yml");
        ConfigManager.createDataFolderAndFile("joined.yml");

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new FJTCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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

        List<String> joinedBefore = joinedBeforeConfig.getStringList("joined-before");

        if (!joinedBefore.contains(player.getUniqueId().toString())) {
            event.getPlayer().connect(ProxyServer.getInstance().getServerInfo(configurationConfig.getString("settings.has-not-played-before-server")));
            joinedBefore.add(player.getUniqueId().toString());
            joinedBeforeConfig.set("joined-before", joinedBefore);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(joinedBeforeConfig, new File(getDataFolder(), "joined.yml"));
        }
    }
}
