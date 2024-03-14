package message_protocol;

public class MessageFormatter {
    public static String formatGetRequest(String key) {
        return String.format("GET(%s)", key);
    }

    public static String formatPutRequest(String key, String value) {
        return String.format("PUT(%s, %s)", key, value);
    }

    public static String formatDelRequest(String key) {
        return String.format("DELETE(%s)", key);
    }
}
