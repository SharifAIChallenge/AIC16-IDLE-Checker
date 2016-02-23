import com.google.gson.JsonArray;

/**
 * Message class
 */
public class Message {
    public final String name;
    public final JsonArray args;

    public Message(String name, JsonArray args) {
        this.name = name;
        this.args = args;
    }
}
