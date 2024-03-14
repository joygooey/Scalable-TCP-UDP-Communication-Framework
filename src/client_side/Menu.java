package client_side;

import java.util.Scanner;

/**
 * Handles options to display and capture user input
 */
public class Menu {
    private Scanner scanner;

    enum UserInputOption {
        PUT, //1
        GET, //2
        DELETE, //3
        EXIT,
        INVALID,
    }

    class UserInput {
        UserInputOption optionSelected;
        String key;
        String value;

        public UserInput(UserInputOption option, String key) {
            this.optionSelected = option;
            this.key = key;
        }

        public UserInput(UserInputOption option, String key, String value) {
            this.optionSelected = option;
            this.key = key;
            this.value = value;
        }
    }

    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    public UserInput displayMenuAndCaptureUserInput() {
        System.out.println("\nPlease choose an option:");
        System.out.println("1. Put");
        System.out.println("2. Get");
        System.out.println("3. Delete");
        System.out.println("4. Exit");
        System.out.print("Enter your choice (1-4): ");

        int operation = this.scanner.nextInt(); // Read user choice
        scanner.nextLine(); // Consume newline


        switch (operation) {
            case 1:
                return handlePut();
            case 2:
                return handleGet();
            case 3:
                return handleDelete();
            case 4:
                System.out.println("Exiting...");
                return new UserInput(UserInputOption.EXIT, null);
            default:
                System.out.println("Invalid option, please try again.");
                return new UserInput(UserInputOption.INVALID, null);
        }

    }

    private UserInput handlePut() {
        System.out.print("Enter key: ");
        String key = scanner.nextLine();
        System.out.print("Enter value: ");
        String value = scanner.nextLine();
        return new UserInput(UserInputOption.PUT, key, value);
    }

    private UserInput handleGet() {
        System.out.print("Enter key: ");
        String key = scanner.nextLine();
        return new UserInput(UserInputOption.GET, key);
    }

    private UserInput handleDelete() {
        System.out.print("Enter key: ");
        String key = scanner.nextLine();
        // messageRequest = communication_protocol.MessageFormatter.formatDelRequest(key);
        return new UserInput(UserInputOption.DELETE, key);
    }

}
