package RMI;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteUserControl extends Remote {
    void register(String userName, IRemoteClient client, IRemoteDrawBoard remoteDrawBoard) throws RemoteException;
    ArrayList<String> getUserList() throws RemoteException;

    void initiateDrawBoard(String userName) throws RemoteException;
    void addShape(Shape shape, Color color) throws RemoteException;

    void addText(String text, Point point, Color color, int fontsize4) throws RemoteException;

    void deleteShape(int index) throws RemoteException;
    void deleteText(int index) throws RemoteException;
}
