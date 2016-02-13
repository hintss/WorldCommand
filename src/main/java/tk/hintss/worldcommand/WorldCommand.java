package tk.hintss.worldcommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by hintss on 7/19/15.
 */
public class WorldCommand extends JavaPlugin implements Listener {
    private static  WorldCommand instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        checkAndRunCommand(event.getPlayer());
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        checkAndRunCommand(event.getPlayer());
    }

    private void checkAndRunCommand(final Player p) {
        new BukkitRunnable() {
            public void run() {
                if (p.getWorld().getName().equalsIgnoreCase(instance.getConfig().getString("world")) && p.hasPermission("worldcommand.run")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), instance.getConfig().getString("command").replace("@p", p.getName()));
                }
            }
        }.runTaskLater(this, 1);
    }
}
