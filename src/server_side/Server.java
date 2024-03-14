package server_side;
import java.util.*;

import helpers.Logger;

/**
 * An abstract class representing a server that handles PUT, GET, and DELETE requests to a key-value store.
 * Subclasses must implement the startListening() method to define server-specific behavior.
 */
public abstract class Server {
    private Map<String, String> keyValueStore = new HashMap<>();

    /**
     * Starts the server, enabling it to listen for incoming requests.
     * Subclasses must implement this method to define server-specific behavior.
     */
    protected abstract void startListening();

    /**
     * Parses request strings from clients to extract operation, key, and value (if applicable).
     *
     * @param request The request string from client to parse.
     * @return A String array containing the operation, key, and value extracted from the request.
     */
    private String[] parseRequest(String request){
        // Extract operation from rest of request
        String[] parts = request.split("\\(", 2);
        String operation = parts[0].toUpperCase();

        // Remove closing ')' and split key and value on ","
        String[] keyValue = parts[1].substring(0, parts[1].length() - 1).split(", ");
        String key = keyValue[0];
        String value = keyValue.length > 1 ? keyValue[1] : ""; // Value may not exist for some operations
        return new String[] { operation, key, value };
    }

    /**
     * Processes the request string by performing the appropriate operation on the key-value store.
     *
     * @param request The request string to process.
     * @return A message indicating the result of the operation.
     */
    protected String processRequest(String request) {
        String[] parts = parseRequest(request);
        String message = null;

        String operation = parts[0];
        String key = parts[1];

        switch (operation) {
            case "PUT":
                String value = parts[2];
                keyValueStore.put(key, value);
                message = String.format("Successful PUT: Key: %s, Value:%s ", key, value);

                Logger.log(message);
                return message;

            case "GET":
                String getValue = keyValueStore.get(key);
                if (getValue != null) {
                    message = String.format("Successful GET: '%s' key's value is '%s'", key, getValue);
                } else {
                    message = "ERROR: Could not GET. Key '" + key + "' not found.";
                }

                Logger.log(message);
                return message;

            case "DELETE":
                String delValue = keyValueStore.remove(key);
                if (delValue != null) {
                    message = String.format("Successful DELETE: '%s' key has been deleted from store. It's value was '%s' ", key, delValue);
                } else {
                    message = "ERROR: Could not DELETE. Key '" + key + "' not found.";
                }

                Logger.log(message);
                return message;

            default:
                return "ERROR: Should be unreachable.";
        }
    }

}
