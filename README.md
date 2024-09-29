# Distributed Shared Whiteboard

## Contributors
- Xi Luo (luoxl7@student.unimelb.edu.au)

## Overview
This project is a distributed system application designed to implement a **Shared Whiteboard** that allows multiple users to draw, collaborate, and communicate over a network. The system is built using the **Client-Server Model** and includes features such as multi-user drawing, text input, color selection, and a chat window.

## Features
### Basic Features
1. **Multi-user Drawing**: Users can draw shapes such as lines, circles, ovals, and rectangles on a shared whiteboard.
2. **Text Input**: Users can input text anywhere on the whiteboard.
3. **Color Selection**: Users can choose from at least 16 colors for drawing.
4. **Real-time Updates**: Changes made by one user are instantly reflected across all connected clients.

### Advanced Features
1. **Chat Window**: A text-based chat window allows users to communicate with each other.
2. **File Menu**: The manager has control over options such as New, Open, Save, Save As, and Close.
3. **User Management**: The manager can kick out a user from the session.

## System Architecture
The system is based on a **Client-Server Model (CS-Model)**:
- **Server**: Manages the state of the whiteboard and handles communication between all clients.
- **Clients**: Connect to the server and receive updates in real-time.

![Picture1](/img/Picture1.jpg)

### Key Architectural Features:
1. **Partial Consistency**: The server acts as the source of truth, but due to internet lag, clients might experience some delay.
2. **Scalability**: The system can be scaled by upgrading the server to handle more clients.
3. **Thread-per-Connection**: Each client connection runs on a separate thread for better performance and resource efficiency.

![Picture2](/img/Picture2.jpg)

## Communication Protocol
The system uses a simple text-based transmission protocol: 

 `Shape/(Shape Type)/(Shape producer)/(X1)/(Y1)/(X2)/(Y2)/(Color)`

- **Shape Type**: The type of shape (e.g., line, circle, rectangle).
- **Shape Producer**: The user who created the shape.
- **Coordinates**: X1, Y1, X2, Y2 specify the position and size.
- **Color**: The color used for drawing.

## Setup and Usage
### Prerequisites
- Java Development Kit (JDK) 11 or higher.
- Network connectivity for distributed usage.

### Running the Application
1. **Starting the Server**:  

    `java CreateWhiteBoard <serverIPAddress> <serverPort> <username>`

2. **Joining as a Client**:  

    `java JoinWhiteBoard <serverIPAddress> <serverPort> <username>`


### User Operations
- **Draw Shapes**: Draw lines, ovals, rectangles.
- **Chat**: Communicate with other users through the chat window.
- **Save/Load Whiteboard**: Save the whiteboard in `.ser`, `.jpg`, or `.png` format.

## Challenges
1. **Concurrency Management**: Ensuring that the whiteboard remains synchronized across all clients despite potential delays.
2. **Communication**: Designing a protocol to efficiently broadcast changes to all clients without overwhelming the network.

## Deliverables
1. **Report**: Describes the system architecture, communication protocols, and implementation details.
2. **Source Code**: Includes the Java source files for both server and client.
3. **Executable Files**: JAR files for running the client and server.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
