package net.bowline.firstjointeleport.Commands;

import net.bowline.firstjointeleport.FirstJoinTeleport;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;

import static net.md_5.bungee.api.ChatColor.*;

public class FJTCommand extends Command {

    public FJTCommand() {
        super("fjt", "firstjointeleport.admin", "firstjointeleport");
    }

    //fjt reload

    @Override
    public void execute(CommandSender sender, String[] args) {
        switch (args.length) {
            case 1: {
                try {
                    Configuration configurationConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(FirstJoinTeleport.getPlugin().getDataFolder(), "config.yml"));
                    ConfigurationProvider.getProvider(YamlConfiguration.class).save(configurationConfig, new File(FirstJoinTeleport.getPlugin().getDataFolder(), "config.yml"));
                    sender.sendMessage(new TextComponent(GREEN + "Configuration has been reloaded."));
                    return;
                } catch (final Exception e) {
                    e.printStackTrace();
                    sender.sendMessage(new TextComponent(RED + "An error occured while saving the config, please see the console for details."));
                    return;
                }
            } default: {
                sender.sendMessage(new TextComponent(GRAY + "fjt reload" + DARK_GRAY + " - " + GREEN + "Reloads the configuration file."));
            }
        }
    }
}
