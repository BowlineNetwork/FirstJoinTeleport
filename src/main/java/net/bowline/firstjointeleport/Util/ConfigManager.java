package net.bowline.firstjointeleport.Util;

import net.bowline.firstjointeleport.FirstJoinTeleport;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigManager {

    public static void createDataFolderAndFile(String configName) {
        if (!FirstJoinTeleport.getPlugin().getDataFolder().exists())
            FirstJoinTeleport.getPlugin().getDataFolder().mkdir();

        File file = new File(FirstJoinTeleport.getPlugin().getDataFolder(), configName);

        if (!file.exists()) {
            try (InputStream in = FirstJoinTeleport.getPlugin().getResourceAsStream(configName)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
