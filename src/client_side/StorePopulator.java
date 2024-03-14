package client_side;
import message_protocol.MessageSender;
import message_protocol.MessageFormatter;
import helpers.Logger;

public class StorePopulator {

    private String hostname;
    private int port;

    public StorePopulator(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }
    public void populateStoreUDP() {
        // Automatically populate store with 5 PUT requests.
        int key = 1;
        int value = 1;
        for (int i = 0; i < 5; i++) {
            String message = MessageFormatter.formatPutRequest(String.valueOf(key), String.valueOf(value));
            String response = MessageSender.sendMessageUsingUDP(message, hostname, port);
            Logger.log(response);
            key++;
            value++;
        }
    }

    public void performOperationsUDP() {
        // Reset keys and values for operations
        int key = 6;
        int value = 6;

        String[] operations = new String[]{"PUT", "GET", "DELETE"};

        for (String operation : operations) {
            for (int i = 0; i < 5; i++) {
                String message = null;
                switch (operation) {
                    case "GET":
                        message = MessageFormatter.formatGetRequest(String.valueOf(key));
                        break;
                    case "PUT":
                        message = MessageFormatter.formatPutRequest(String.valueOf(key), String.valueOf(value));
                        break;
                    case "DELETE":
                        message = MessageFormatter.formatDelRequest(String.valueOf(key));
                        break;
                }

                String response = MessageSender.sendMessageUsingUDP(message, hostname, port);
                Logger.log(response);

                key++;
                if (operation.equals("PUT")) {
                    value++;
                }
            }

            // Reset keys and values for the next operation
            key = 6;
            value = 6;
        }
    }

    public void populateStoreTCP() {
        // Automatically populate store with 5 PUT requests.
        int key = 1;
        int value = 1;
        for (int i = 0; i < 5; i++) {
            String message = MessageFormatter.formatPutRequest(String.valueOf(key), String.valueOf(value));
            String response = MessageSender.sendMessageUsingTCP(message, hostname, port);
            Logger.log(response);
            key++;
            value++;
        }
    }

    public void performOperationsTCP() {
        // Reset keys and values for operations
        int key = 6;
        int value = 6;

        String[] operations = new String[]{"PUT", "GET", "DELETE"};

        for (String operation : operations) {
            for (int i = 0; i < 5; i++) {
                String message = null;
                switch (operation) {
                    case "GET":
                        message = MessageFormatter.formatGetRequest(String.valueOf(key));
                        break;
                    case "PUT":
                        message = MessageFormatter.formatPutRequest(String.valueOf(key), String.valueOf(value));
                        break;
                    case "DELETE":
                        message = MessageFormatter.formatDelRequest(String.valueOf(key));
                        break;
                }

                String response = MessageSender.sendMessageUsingTCP(message, hostname, port);
                Logger.log(response);

                key++;
                if (operation.equals("PUT")) {
                    value++;
                }
            }

            // Reset keys and values for the next operation
            key = 6;
            value = 6;
        }
    }
}
