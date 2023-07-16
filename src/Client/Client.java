package Client;

import RMI.*;
import Whiteboard.WhiteBoard;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private String serverAddress;
    private int serverPort;
    private String userName;
    public static final int mode = 1;
    private WhiteBoard whiteBoard;
    private Registry registry;
    private IRemoteServer remoteServer;
    private RemoteClient remoteClient;

    public Client(String serverAddress, int serverPort, String userName){
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.userName = userName;
        createWhiteboard();
        createInternet();
    }

    private void createWhiteboard(){
        whiteBoard = new WhiteBoard(userName, mode);
    }

    private void createInternet(){
        try {

            registry = LocateRegistry.getRegistry(serverAddress,serverPort);

            remoteClient = new RemoteClient(whiteBoard, whiteBoard.getDrawBoard());
            remoteServer = (IRemoteServer) registry.lookup("RemoteServer");
            remoteServer.register(userName, remoteClient);

            whiteBoard.setRemoteServer(remoteServer);
            whiteBoard.getDrawBoard().setRemoteServer(remoteServer);

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

}
