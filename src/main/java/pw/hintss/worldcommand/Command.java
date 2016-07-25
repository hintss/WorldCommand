package pw.hintss.worldcommand;

import org.bukkit.entity.Player;

/**
 * Created by hintss on 7/25/2016.
 */
public class Command {
    private final String name;
    private final String command;
    private final Sender sender;
    private final boolean onJoin;

    public Command(String name, String command, Sender sender, boolean onJoin) {
        this.name = name;
        this.command = command;
        this.sender = sender;
        this.onJoin = onJoin;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return this.command;
    }

    public String getCommand(Player p) {
        return this.command.replace("@p", p.getName());
    }

    public Sender getSender() {
        return this.sender;
    }

    public boolean isOnJoin() {
        return this.onJoin;
    }
}
