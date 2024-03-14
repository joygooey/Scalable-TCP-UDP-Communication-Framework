package server_side;

import java.io.*;
import java.net.*;

import helpers.Logger;
/**
 * A TCP server implementation that extends the abstract Server class.
 */
public class TCPServer extends server_side.Server {
    private int port;

    public TCPServer(int port) {
        this.port = port;
    }
    /**
     * Starts listening for incoming TCP connections on the specified port.
     * Upon receiving a connection, processes the request and sends back a response to the client.
     * Logs requests and any errors that occur during the process.
     */
    protected void startListening() {
        /// changes!!
        try (ServerSocket serverSocket = new ServerSocket(this.port, 1,InetAddress.getByName("0.0.0.0"))) {
            Logger.log("TCP Server listening on port " + port);

            while(true) {
                // Waits for a client connection to accept and logs to console.
                Socket clientSocket = serverSocket.accept();
                String clientInfo = "InetAddress: " + clientSocket.getInetAddress() + ", Port number:" + clientSocket.getPort();
                Logger.log("Connection established with Client. " + clientInfo);

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                    // Reads input and checks if it's a valid request.
                    String inputLine = reader.readLine();
                    String serverResponse = null;
                    if (!isValidRequest(inputLine))  {
                        // Log malformed request
                        Logger.log("Received malformed request from " + clientInfo + " saying: " + inputLine );
                        // Generate error message to respond to client.
                        serverResponse = "ERROR: Server received following malformed request: " + inputLine;
                    } else {
                        // Log received query with specific details
                        Logger.log("Server received the following message: " + inputLine + " from " + clientInfo);
                        serverResponse = this.processRequest(inputLine);
                    }
                    writer.write(serverResponse);
                    writer.newLine(); // Add a newline (ensure proper termination)
                    writer.flush();   // Flush to send the response immediately
                } finally {
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            Logger.log("Error: " + e.getMessage());
        }
    }

    /**
     * Performs validation check on received data to check format: "OPERATION(key, value)"
     * Note: Keys and values that contain commas or parentheses are considered invalid.
     * @param inputLine The input string to parse
     * @return True if request is in expected format, False otherwise.
     */
    private boolean isValidRequest(String inputLine) {
        if (inputLine == null || inputLine.isEmpty()) {
            return false;
        }
        String regex = "^(PUT|GET|DELETE)\\([a-zA-Z0-9_\\s]+(, [a-zA-Z0-9_\\s]+)?\\)$";

        return inputLine.matches(regex);
    }

    /**
     * Entry point of the TCP server application. Parses command-line arguments
     * to obtain the port number, creates a TCPServer instance with the provided
     * port number, and starts listening for incoming TCP connections.
     *
     * @param args Command-line arguments. Expects a single argument: the port number.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please include port number and try again.");
            return;
        }
        int port = Integer.parseInt(args[0]);

        TCPServer tcpServer = new TCPServer(port);
        tcpServer.startListening();
    }


}