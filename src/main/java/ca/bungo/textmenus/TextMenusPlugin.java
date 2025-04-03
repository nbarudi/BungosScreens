package ca.bungo.textmenus;

import ca.bungo.textmenus.commands.TestCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public class TextMenusPlugin extends JavaPlugin {
    public static final String IDENTIFIER = "textmenus";

    public static Logger LOGGER;


    @Override
    public void onEnable() {
        LOGGER = this.getSLF4JLogger();
        LOGGER.info("Text Menus Plugin Enabled");

        //Registering Commands
        Bukkit.getServer().getCommandMap().register(IDENTIFIER, new TestCommand());
    }

    @Override
    public void onDisable() {}
}
