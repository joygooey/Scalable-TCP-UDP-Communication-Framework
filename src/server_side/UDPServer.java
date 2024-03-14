package server_side;

import java.net.*;
import helpers.*;
/**
 * A UDP server implementation that extends the abstract Server class.
 */
public class UDPServer extends Server {
    private int port;

    public UDPServer(int port){this.port = port;}

    /**
     * Starts listening for incoming UDP datagrams on the specified port.
     * Upon receiving a datagram, processes the request and sends back a response to the client.
     * Logs any errors that occur during the process.
     */
    protected void startListening(){
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(this.port);
            Logger.log("UDP Server listening on port " + port);
            byte[] receiveData = new byte[1024];

            byte[] sendData = null;

            while (true) {
                // Datagram packet to receive data
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Receive data from client
                socket.receive(receivePacket);

                // Extract client's IP address and port number to send response back.
                InetAddress clientIPAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String clientInfo = "InetAddress: " + clientIPAddress + ", Port number:" + clientPort;

                // Extract message containing checksum value and UUID
                String receivedFullMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                // Split message into UUID, checksum, and message .
                String[] parts = receivedFullMessage.split(":", 3);
                // Check if message is malformed. Log any errors and skip processing of malformed requests.
                if (parts.length < 3) {
                    Logger.log("Invalid message format from " + receivePacket.getAddress() + ":" + receivePacket.getPort());
                    continue;
                }

                // Get uuid, checksum values, and message.
                String uuid = parts[0];
                long receivedChecksum = Long.parseLong(parts[1]);
                String receivedMessage = parts[2];

                // Recalculate checksum for the received message
                byte[] messageBytes = receivedMessage.getBytes();
                long calculatedChecksum = CheckSumUDP.calculateChecksum(messageBytes);

                // Checksum validation
                if (receivedChecksum != calculatedChecksum) {
                    Logger.log("Checksum mismatch for message from " + receivePacket.getAddress() + ":" + receivePacket.getPort());
                    continue; // Skip processing this packet
                }

                // Log message with client info
                Logger.log("Received from client:" + receivedMessage + "." + clientInfo);

                // Process the request
                String response = this.processRequest(receivedMessage);
                // Tag response with uuid.
                response = uuid + ":" + response;

                // Prepare response and send back to client
                sendData = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIPAddress, clientPort);

                socket.send(sendPacket);
            }
        } catch (Exception e) {
            Logger.log("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    /**
     * Entry point of the UDP server application. Parses command-line arguments
     * to obtain the port number, creates a UDPServer instance with the provided
     * port number, and starts listening for incoming UDP datagrams.
     *
     * @param args Command-line arguments. Expects a single argument: the port number.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please include port number and try again.");
            return;
        }
        int port = Integer.parseInt(args[0]);

        UDPServer udpServer = new UDPServer(port);
        udpServer.startListening();
    }
}

