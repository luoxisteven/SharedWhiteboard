package RMI;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteUserControl extends Remote {
    void register(String userName, IRemoteClient client) throws RemoteException;
    ArrayList<String> getUserList() throws RemoteException;
    void unicastMessage(String userName, String message) throws RemoteException;
    void broadcastMessage(String message) throws RemoteException;
    void initiateDrawBoard(String userName) throws RemoteException;
    void addShape(Shape shape, Color color, ArrayList<String> userList) throws RemoteException;
    void addText(String text, Point point, Color color, int fontsize4,
                 ArrayList<String> userList) throws RemoteException;
    void deleteShape(int index, ArrayList<String> userList) throws RemoteException;
    void deleteText(int index, ArrayList<String> userList) throws RemoteException;
}
