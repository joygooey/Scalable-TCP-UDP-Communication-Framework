package client_side;

import message_protocol.MessageFormatter;
import message_protocol.MessageSender;

import java.util.Scanner;
import helpers.Logger;

/**
 * A UDP client application that interacts with a server to perform key-value store operations.
 * It takes user inputs from the console, formats them into messages, sends them to the server using UDP protocol,
 * and displays the responses received from the server.
 */
public class UDPClient {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please include port number and Hostname/ IP Address and try again.");
            return;
        }
        Logger.log(String.format("Continuing with hostname:'%s' port:'%s'", args[0], args[1]));

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu(scanner);

        StorePopulator populator = new StorePopulator(hostname, port);

        // Populate the store with initial PUT requests
        populator.populateStoreUDP();

        // Perform PUT, GET, DELETE operations
        populator.performOperationsUDP();

        // Show menu
        while (true) {
            Menu.UserInput userInputs = menu.displayMenuAndCaptureUserInput();
            String message = null;
            String response = null;

            switch (userInputs.optionSelected) {
                case GET:
                    message = MessageFormatter.formatGetRequest(userInputs.key);
                    response = MessageSender.sendMessageUsingUDP(message, hostname, port);
                    Logger.log(response);
                    break;

                case PUT:
                    message = MessageFormatter.formatPutRequest(userInputs.key, userInputs.value);
                    response = MessageSender.sendMessageUsingUDP(message, hostname, port);
                    Logger.log(response);
                    break;

                case DELETE:
                    message = MessageFormatter.formatDelRequest(userInputs.key);
                    response = MessageSender.sendMessageUsingUDP(message, hostname, port);
                    Logger.log(response);
                    break;

                case EXIT:
                    return;

                case INVALID:
                    break;

                default:
                    System.out.println("Invalid input. Try again.");
            }
        }
    }

}
