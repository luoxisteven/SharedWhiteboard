package Client;

import Whiteboard.WhiteBoard;

import RMI.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private String serverAddress;
    private int serverPort;
    private String userName;
    public static final int MODE = 1;
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
