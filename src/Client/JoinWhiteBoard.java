package Client;

import RMI.*;
import Whiteboard.WhiteBoard;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JoinWhiteBoard {
    public static final String DEFAULT_IP = "localhost";
    public static final int DEFAULT_PORT = 4444;
    public static final String DEFAULT_USERNAME = "Client";
    private String serverAddress;
    private int serverPort;
    private String userName;

    public JoinWhiteBoard(){
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
            System.out.println("java -jar JoinWhiteBoard.jar <IP> <Port> <Username>");
        }
    }

    public void init(){
//        ClientInternet clientInternet = new ClientInternet();

        try {
            WhiteBoard whiteBoard = new WhiteBoard(userName);
            Registry registry = LocateRegistry.getRegistry(serverAddress,serverPort);
//            IremoteDrawBoard remoteDrawBoard = (IremoteDrawBoard) registry.lookup("DrawBoard");
//            whiteBoard.getDrawBoard().copyRemoteToLocal(remoteDrawBoard);

            IRemoteClient client = new RemoteClient(whiteBoard, whiteBoard.getDrawBoard());
            IRemoteUserControl userControl = (IRemoteUserControl) registry.lookup("UserControl");
            userControl.register(userName, client);

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args){
        JoinWhiteBoard joinWhiteBoard = new JoinWhiteBoard();
        joinWhiteBoard.parseArgs(args);
        joinWhiteBoard.init();
    }

}
