package Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;


/**
 * Class JoinWhiteBoard:
 * Parse the parameters and Start Login GUI
 *
 * COMP90015 Distributed Systems, Sem1, 2023
 * @author Xi Luo, 1302954, luoxl7@student.unimelb.edu.au
 * @version jdk18.0.2
 */
public class JoinWhiteBoard {
    public static final String DEFAULT_IP = "localhost";
    public static final int DEFAULT_PORT = 4444;
    public static final String DEFAULT_USERNAME = "Client";
    private String serverAddress;
    private int serverPort;
    private String userName;

    private JoinWhiteBoard(){
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
            System.out.println("java -jar JoinWhiteBoard.jar <IP> <Port> <Username>");
        }
    }

    private void createClient(){
        try {
            Client client = new Client(serverAddress,serverPort,userName);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void clientLogin(){
        ClientLoginGUI clientLoginGUI = new ClientLoginGUI(serverAddress,serverPort,userName);
    }

    public static void main(String[] args){
        JoinWhiteBoard joinWhiteBoard = new JoinWhiteBoard();
        joinWhiteBoard.parseArgs(args);
        joinWhiteBoard.clientLogin();
    }

}
