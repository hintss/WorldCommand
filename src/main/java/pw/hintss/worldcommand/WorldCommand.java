package pw.hintss.worldcommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hintss on 7/19/15.
 */
public class WorldCommand extends JavaPlugin implements Listener {
    final Map<String, List<Command>> commands = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        for (String world : getConfig().getKeys(false)) {
            getLogger().info("loading commands for world: " + world);

            commands.put(world, new ArrayList<Command>());

            for (String command : getConfig().getConfigurationSection(world).getKeys(false)) {

                getLogger().info("   loading command: " + command);

                String path = world + '.' + command + '.';
                Sender sender = Sender.CONSOLE;
                if (getConfig().contains(path + "sender")) {
                    if (getConfig().getString(path + "sender").equalsIgnoreCase("player")) {
                        sender = Sender.PLAYER;
                    } else {
                        if (!getConfig().getString(path + "sender").equalsIgnoreCase("console")) {
                            getLogger().warning("sender isn't \"console\" or \"player\"! assuming console!");
                        }
                    }
                } else {
                    getLogger().warning("sender isn't specified! assuming console!");
                }

                String cmd = getConfig().getString(path + "command");
                boolean onJoin = getConfig().getBoolean(path + "onjoin");

                getLogger().info("      command: " + cmd);
                getLogger().info("      sender:  " + sender.name());
                getLogger().info("      on join: " + onJoin);

                commands.get(world).add(new Command(command, cmd, sender, onJoin));
            }
        }

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        commands.clear();
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        checkAndRunCommand(event.getPlayer(), true);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        checkAndRunCommand(event.getPlayer(), false);
    }

    private void checkAndRunCommand(final Player p, final boolean join) {

        new BukkitRunnable() {
            @Override
            public void run() {
                String world = p.getWorld().getName();

                if (!commands.containsKey(world)) {
                    return;
                }

                for (Command c : commands.get(world)) {
                    if (!c.isOnJoin() && join) {
                        continue;
                    }

                    if (!p.hasPermission("worldcommand." + world + '.' + c.getName())) {
                        continue;
                    }

                    CommandSender sender = c.getSender() == Sender.CONSOLE ? Bukkit.getConsoleSender() : p;

                    Bukkit.dispatchCommand(sender, c.getCommand(p));
                }
            }
        }.runTaskLater(this, 1L);
    }
}
