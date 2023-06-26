package fr.ellipsialepoulet.kaiivisible;

import fr.ellipsialepoulet.kaiivisible.commands.Visibility;
import org.bukkit.plugin.java.JavaPlugin;

public final class KaiiVisible extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getLogger().info("KaiiVisible is enabled!");

        Visibility visibility = new Visibility();
        try {
          this.getCommand("visibility").setExecutor(visibility);
          this.getCommand("visibility").setTabCompleter(visibility);
        } catch (NullPointerException e) {
          this.getLogger().warning("Could not register command!");
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().info("KaiiVisible is disabled!");
    }
}
