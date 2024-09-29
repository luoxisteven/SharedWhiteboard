package Server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

/**
 * Class CreateWhiteBoard:
 * Parse the parameters and Start Login GUI
 *
 * COMP90015 Distributed Systems, Sem1, 2023
 * @author Xi Luo, 1302954, luoxl7@student.unimelb.edu.au
 * @version jdk18.0.2
 */
public class CreateWhiteBoard {
    public static final String DEFAULT_IP = "localhost";
    public static final int DEFAULT_PORT = 4444;
    public static final String DEFAULT_USERNAME = "Manager";
    private String serverAddress;
    private int serverPort;
    private String userName;

    private CreateWhiteBoard(){
        serverAddress = DEFAULT_IP;
        serverPort = DEFAULT_PORT;
        userName = DEFAULT_USERNAME;
    }

    private void parseArgs(String args[]) {
        try {
            serverAddress = args[0];
            serverPort = Integer.parseInt(args[1]);
            userName = args[2];
        } catch (Exception e) {
            System.out.println("Warning: Please check for the params.");
            System.out.println("java -jar CreateWhiteBoard.jar <IP> <Port> <Username>");
        }
    }

    private void createSever(){
        try {
            Server server = new Server(serverAddress, serverPort, userName);
            System.out.println("Server created successfully!");
        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Failed to create server: " + e.getMessage());
        }
    }

    private void serverLogin(){
        ServerLoginGUI serverLoginGUI = new ServerLoginGUI(serverAddress, serverPort, userName);
    }

    public static void main(String args[]) {
        CreateWhiteBoard createWhiteBoard = new CreateWhiteBoard();
        createWhiteBoard.parseArgs(args);
        createWhiteBoard.serverLogin();
    }

}
