
# Server-Client Key-Value Store Project
This project implements a simple key-value store system with a server-client architecture. The server component utilizes TCP and UDP sockets to communicate with clients and stores data in a HashMap. The client component allows users to interact with the server by executing PUT, GET, and DELETE operations.\

## Server Component

### Basic Structure
The server component utilizes a HashMap data structure for storing key-value pairs, providing efficient data retrieval, insertion, and deletion.\

### Server Setup

TCP and UDP are split into different classes.

- Each initializes server sockets for both TCP and UDP protocols accordingly.
- Listens continuously for incoming connections (TCP) and datagram packets (UDP).
- Handles each request in a single-threaded manner, processing one request at a time.
- Listens until the connection is manually closed.

### Request Handling

- Decodes incoming requests to determine the operation type (PUT, GET, DELETE).
- Performs the requested operation on the key-value store.
- Sends back a response to the client, indicating the success/failure of the operation.

### Logging

- Implements logging functionality to timestamp and log all requests and responses.
- Handles malformed requests:
  - For UDP: Catches exceptions related to datagram packet parsing and logs any malformed packet details.
  - For TCP: Catches and handles exceptions related to stream corruption or unexpected data format.

## Client Component

### Basic Functionality

- Parses command-line arguments for server hostname/IP and port number.

### Communication Setup

- Initializes an appropriate socket depending on the mode (TCP/UDP).
- Implements a timeout mechanism for handling server unresponsiveness.

### Protocol Design

- Defines a simple protocol for encoding and decoding messages for PUT, GET, and DELETE operations.
- Includes error handling for unexpected or malformed responses.

### Logging

- Implements logging with millisecond precision timestamps for all activities.

### Operation Execution

- Executes at least 5 PUTs, 5 GETs, and 5 DELETEs.
- Verifies the key-value store's integrity by checking the correctness of GET and DELETE operations.

## Error Handling and Protocol Compliance

### TCP Server and Client

The TCP implementation leverages TCP's inherent features for ensuring data integrity, such as checksums and reliable data transmission protocols, thereby negating the need for manual implementation of these features. However, strict adherence to the communication protocol format for all requests is enforced: `OPERATION(key, value)`. This format is crucial for the server to process requests correctly. The server side checks for this format, and any malformed requests that do not comply are not processed. This ensures that only correctly formatted requests are handled, enhancing the robustness of the application against incorrect or malicious inputs.

### UDP Client and Server

- In the UDP implementation, additional measures are taken to ensure the integrity and security of the data transmitted. Given UDP's stateless nature and lack of built-in reliability:
  - **Message Tagging**: Every message sent by the client is tagged with a unique identifier (UUID) and checksum values. This tagging helps in identifying and verifying each message uniquely.
  - **Checksum Verification**: Upon receiving a message, the server performs a checksum verification to detect any corruption that might have occurred during transmission. This step is crucial for maintaining data integrity, as UDP does not inherently guarantee the delivery of uncorrupted data.
  - **Response Tagging**: Responses from the server are also tagged with the same UUID used in the client's request. This approach is employed to prevent the processing of unsolicited responses, ensuring that the response is matched accurately with its corresponding request.
  - **Format Enforcement**: Similar to the TCP implementation, UDP communications must adhere to the specified format: `OPERATION(key, value)`. Messages not conforming to this format, as well as those lacking UUID and checksum information, are disregarded and not processed by the server.
  - These error handling and protocol compliance measures are integral to the application's reliability and security, particularly in distinguishing and mitigating the differing characteristics and vulnerabilities associated with TCP and UDP protocols.

## Running the Application
- Compile all the Java files using `javac`.
- Start the server by running either `java UDPServer <port_number>` or `java TCPServer <port_number>`.
- Start the client by running either `java UDPClient <server_hostname/IP> <port_number>` or `TCPClient <server_hostname/IP> <port_number>`.
- Interact with the server through the menu displayed in the console to execute PUT, GET, DELETE operations on the store.

## Dependencies
- Java Development Kit (JDK)
- IDE or text editor for code editing and compilation
- SDK: 19 Oracle OpenJDK version 19.01

## Contributors
- Hsin Yu Guo
}
