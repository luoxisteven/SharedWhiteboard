package Server;

import RMI.IRemoteDrawBoard;
import RMI.IRemoteUserControl;
import RMI.RemoteDrawBoard;
import RMI.RemoteUserControl;
import Whiteboard.WhiteBoard;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server{

    private String serverAddress;
    private int serverPort;
    private String userName;
    private WhiteBoard whiteBoard;
    private Registry registry;
    private IRemoteDrawBoard remoteDrawBoard;
    private IRemoteUserControl remoteUserControl;

    public Server(String serverAddress, int serverPort, String userName){
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.userName = userName;
        this.createWhiteboard();
        this.internet();
    }

    public void createWhiteboard(){
        try {
            this.whiteBoard = new WhiteBoard(userName, 0);
            this.remoteDrawBoard = new RemoteDrawBoard(whiteBoard.getDrawBoard(), userName);
            this.whiteBoard.getDrawBoard().setRemoteDrawBoard(this.remoteDrawBoard);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void internet(){
        try {
            System.setProperty("java.rmi.server.hostname",serverAddress);
            registry = LocateRegistry.createRegistry(serverPort);
            remoteUserControl = new RemoteUserControl(whiteBoard,whiteBoard.getDrawBoard());
            registry.bind("UserControl", remoteUserControl);
            registry.bind("ServerBoard", remoteDrawBoard);
            remoteDrawBoard.setRemoteUserControl(remoteUserControl);
            whiteBoard.getDrawBoard().setRemoteUserControl(remoteUserControl);
        } catch (RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
