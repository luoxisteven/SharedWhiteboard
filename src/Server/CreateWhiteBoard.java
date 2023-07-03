package Server;

import RMI.RemoteCanvas;
import Whiteboard.WhiteBoard;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class CreateWhiteBoard {
    public static final String DEFAULT_IP = "localhost";
    public static final int DEFAULT_PORT = 4444;
    public static final String DEFAULT_USERNAME = "Manager";
    private String serverAddress;
    private int serverPort;
    private String userName;

    public CreateWhiteBoard(){
        serverAddress = DEFAULT_IP;
        serverPort = DEFAULT_PORT;
        userName = DEFAULT_USERNAME;
    }

    public void parseArgs(String args[]) {
        try {
            serverAddress = args[0];
            serverPort = Integer.parseInt(args[1]);
            userName = args[2];
        } catch (Exception e) {
            System.out.println("Warning: Please check for the params.");
            System.out.println("java -jar CreateWhiteBoard.jar <IP> <Port> <Username>");
        }
    }

    public void init(){
        WhiteBoard whiteBoard = new WhiteBoard(userName);
//        RemoteCanvas remoteCanvas = new RemoteCanvas(whiteBoard.getCanvas());
        try {
            Registry registry = LocateRegistry.createRegistry(serverPort);
//            registry.bind("Canvas", remoteCanvas);
        } catch (RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
//        catch (AlreadyBoundException e) {
//            throw new RuntimeException(e);
//        }

    }

    public static void main(String args[]) {
        CreateWhiteBoard createWhiteBoard = new CreateWhiteBoard();
        createWhiteBoard.parseArgs(args);
        createWhiteBoard.init();
    }

}
