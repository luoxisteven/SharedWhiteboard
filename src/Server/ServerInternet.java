package Server;

import Whiteboard.WhiteBoard;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerInternet {

    private String serverAddress;
    private int serverPort;
    private String userName;
    private WhiteBoard whiteBoard;

    public ServerInternet(String serverAddress, int serverPort, String userName, WhiteBoard whiteBoard){
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.userName = userName;
        this.whiteBoard = whiteBoard;
    }

    public void RMIRegistry(String serverAddress, int serverPort){
        try {
            Registry registry = LocateRegistry.createRegistry(serverPort);
//            registry.bind("Canvas", this.whiteBoard.getCanvas());
        } catch (RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
