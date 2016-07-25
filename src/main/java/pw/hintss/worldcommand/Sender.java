package pw.hintss.worldcommand;

/**
 * Created by hintss on 7/25/2016.
 */
public enum Sender {
    CONSOLE, PLAYER;

    public static Sender fromValue(String value) {
        String upper = value.toUpperCase();

        return valueOf(upper);
    }
}
