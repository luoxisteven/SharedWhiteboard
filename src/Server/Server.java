package Server;

import RMI.*;
import Whiteboard.WhiteBoard;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server{
    private String serverAddress;
    private int serverPort;
    private String userName;
    private WhiteBoard whiteBoard;
    private Registry registry;
    private RemoteServer remoteServer;
    public static final int mode = 0;

    public Server(String serverAddress, int serverPort, String userName){
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.userName = userName;
        this.createWhiteboard();
        this.createInternet();
    }

    public void createWhiteboard(){
        this.whiteBoard = new WhiteBoard(userName, mode);
    }

    public void createInternet(){
        try {
            System.setProperty("java.rmi.server.hostname", serverAddress);
            registry = LocateRegistry.createRegistry(serverPort);
            remoteServer = new RemoteServer(userName,whiteBoard);
            whiteBoard.setRemoteServer(remoteServer);
            whiteBoard.getDrawBoard().setRemoteServer(remoteServer);
            registry.bind("RemoteServer",(IRemoteServer) remoteServer);
        } catch (RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}