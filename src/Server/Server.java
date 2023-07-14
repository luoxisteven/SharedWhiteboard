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
    private RemoteUserControl remoteUserControl;

    public Server(String serverAddress, int serverPort, String userName){
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.userName = userName;
        this.createWhiteboard();
        this.internet();
    }

    public void createWhiteboard(){
        this.whiteBoard = new WhiteBoard(userName);
        this.remoteDrawBoard = whiteBoard.getRemoteDrawBoard();
    }

    public void internet(){
        try {
            System.setProperty("java.rmi.server.hostname",serverAddress);
            registry = LocateRegistry.createRegistry(serverPort);
            remoteUserControl = new RemoteUserControl(whiteBoard,whiteBoard.getDrawBoard());
            registry.bind("UserControl", remoteUserControl);
            registry.bind("ServerBoard", remoteDrawBoard);

//            registry.bind("DrawBoard", remoteDrawBoard);

//            while(true){
//                try {
//                    Thread.sleep(5000);
//                    remoteMsg.broadcastMessage("abc");
//                    // Sleep between messages to avoid flooding the network
//                    Thread.sleep(100);
//                } catch (RemoteException e) {
//                    System.err.println("Failed to send message: " + e);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }

        } catch (RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
