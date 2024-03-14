package client_side;

import java.util.Scanner;

import client_side.Menu.UserInput;
import message_protocol.MessageFormatter;
import message_protocol.MessageSender;
import helpers.Logger;

public class TCPClient {

    // Need driver code to perform 5 of each get put del before taking user input.

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please include port number and Hostname/ IP Address and try again.");
            return;
        }
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu(scanner);

        StorePopulator populator = new StorePopulator(hostname, port);

        // Populate the store with initial PUT requests
        populator.populateStoreTCP();

        // Perform PUT, GET, DELETE operations
        populator.performOperationsTCP();

        // Show menu
        while (true) {
            UserInput userInputs = menu.displayMenuAndCaptureUserInput();
            String message = null;
            String response = null;

            switch (userInputs.optionSelected) {
                case GET:
                    message = MessageFormatter.formatGetRequest(userInputs.key);
                    response = MessageSender.sendMessageUsingTCP(message, hostname, port);
                    Logger.log(response);
                    break;

                case PUT:
                    message = MessageFormatter.formatPutRequest(userInputs.key, userInputs.value);
                    response = MessageSender.sendMessageUsingTCP(message, hostname, port);
                    Logger.log(response);
                    break;

                case DELETE:
                    message = MessageFormatter.formatDelRequest(userInputs.key);
                    response = MessageSender.sendMessageUsingTCP(message, hostname, port);
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

