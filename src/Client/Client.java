package Client;

import Whiteboard.WhiteBoard;

import RMI.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Class Client:
 * Create and Store whiteboard and Internet utility
 *
 * COMP90015 Distributed Systems, Sem1, 2023
 * @author Xi Luo, 1302954, luoxl7@student.unimelb.edu.au
 * @version jdk18.0.2
 */
public class Client {
    private String serverAddress;
    private int serverPort;
    private String userName;
    public static final int MODE = 1; // 0 is Server, 1 is Client
    private WhiteBoard whiteBoard;
    private Registry registry;
    private IRemoteServer remoteServer;
    private RemoteClient remoteClient;

    public Client(String serverAddress, int serverPort, String userName) throws RemoteException, NotBoundException{
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.userName = userName;
        createWhiteboard();
        createInternet();
    }

    private void createWhiteboard(){
        whiteBoard = new WhiteBoard(userName, MODE);
    }

    private void createInternet() throws RemoteException, NotBoundException{
        registry = LocateRegistry.getRegistry(serverAddress,serverPort);
        remoteClient = new RemoteClient(userName, whiteBoard);
        remoteServer = (IRemoteServer) registry.lookup("RemoteServer");
        whiteBoard.setRemoteServer(remoteServer);
        whiteBoard.getDrawBoard().setRemoteServer(remoteServer);
        remoteServer.register(userName, remoteClient);
    }

}
