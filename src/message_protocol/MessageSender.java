package message_protocol;

import helpers.CheckSumUDP;
import helpers.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.util.UUID;


/**
 * Used for client to send a formatted message/ request using either TCP or UDP
 */
public class MessageSender {
    public static int DEFAULT_TIMEOUT = 2000; //2 seconds

    /**
     * Sends a formatted message using UDP protocol to the specified IP address or hostname and port.
     * Implements checksums and uuid tagging to ensure robustness to malformed or unrequested datagram packets
     * Receives a response from the server.
     * Logs status and errors to console.
     *
     * @param message           The message to send.
     * @param IPAdressOrHostname The IP address or hostname of the destination.
     * @param port              The port number of the destination.
     * @return                  The response received from the server, or an error message if an exception occurs.
     */
    public static String sendMessageUsingUDP(String message, String IPAdressOrHostname, int port) {
        DatagramSocket socket = null;
        DatagramPacket packet = null;

        try {
            // Create a socket with timeout
            socket = new DatagramSocket();
            socket.setSoTimeout(DEFAULT_TIMEOUT);

            InetAddress serverAddress = InetAddress.getByName(IPAdressOrHostname);

            // Generate unique UUID to tag messages with.
            String uuid = String.valueOf(UUID.randomUUID());

            // Calculate checksum for the message.
            byte[] messageBytes = message.getBytes();
            long checksumValue = CheckSumUDP.calculateChecksum(messageBytes);

            //prepare message with checksum + uuid.
            String messageWithChecksumAndUUID = uuid + ":" + checksumValue + ":" + message; // Format: uuid:checksumvalue:message
            byte[] sendData = messageWithChecksumAndUUID.getBytes();

            // Create a packet with the data and send the packet
            packet = new DatagramPacket(sendData, sendData.length, serverAddress, port);
            socket.send(packet);
            Logger.log("Message sent to the server with uuid:"+uuid);

            try {
                // Prepare a buffer to receive the response
                byte[] buffer = new byte[1024];
                DatagramPacket packetToReceive = new DatagramPacket(buffer, buffer.length);

                // Receive the response from the server
                socket.receive(packetToReceive);

                // Check if the response is malformed or unsolicited and log results.
                if (!packetToReceive.getAddress().equals(serverAddress)){
                    return "Received unsolicited response from an unexpected source.";
                }

                // Check recieved response has correct UUID
                // Full message format: "UUID:message"
                String receivedFullMessage = new String(packetToReceive.getData(), 0, packetToReceive.getLength());
                String[] parts = receivedFullMessage.split(":", 2);

                // If UUID doesn't match,
                if(!parts[0].equals(uuid)) {
                    return "Received malicious response from the server with uuid:"+parts[0];
                }

                Logger.log("Successfully received message back from the server with uuid:" + parts[0] + "\n");

                return parts[1];
            } catch (SocketTimeoutException e) {
                // Handle the case where the response is not received within the timeout period
                return String.format("No response received from server within %d milliseconds.", DEFAULT_TIMEOUT);
            } finally {
                // Close the socket
                socket.close();
            }

        } catch (SocketException e) {
            return ("SocketException: " + e.getMessage());
        } catch (IOException e) {
            return ("IOException: " + e.getMessage());
        }
    }


    /**
     * Sends a formatted message using TCP protocol to the specified IP address/ hostname and port.
     * Recieves response from server.
     * TCP innately robust to malformed/ unsolicited messages.
     * Logs status and errors to console.
     *
     * @param message           The message to send.
     * @param IPAdressOrHostname The IP address or hostname of the destination.
     * @param port              The port number of the destination.
     * @return                  The response received from the server, or an error message if an exception occurs.
     */
    public static String sendMessageUsingTCP(String message, String IPAdressOrHostname, int port) {
        Socket socket = null;

        try {
            socket = new Socket(IPAdressOrHostname, port);

            // Set the socket read timeout to 2000 milliseconds (2 seconds)
            socket.setSoTimeout(DEFAULT_TIMEOUT);

            // Create a PrintWriter to send a message to the server
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            // Send the message to the server
            out.println(message);

            // BufferedReader to read the response from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Try to read the response from the server
            try {
                String response = in.readLine(); // Read a line of text from the input stream
                return response;
            } catch (SocketTimeoutException e) {
                // Handle the case where the response is not received within the timeout period
                return String.format("No response received from server within %d milliseconds.", DEFAULT_TIMEOUT);
            } finally {
                // Close the socket
                socket.close();
            }

        } catch (SocketException e) {
            return ("SocketException: " + e.getMessage());
        } catch (IOException e) {
            return ("IOException: " + e.getMessage());
        }
    }
}
