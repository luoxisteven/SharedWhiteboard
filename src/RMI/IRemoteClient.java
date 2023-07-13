package RMI;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteClient extends Remote {
    void retrieveMessage(String message) throws RemoteException;
    void initiateCanvas(RemoteDrawBoard remoteDrawBoard) throws RemoteException;
    void addShape(Shape shape, Color color) throws RemoteException;
    void addText(String text, Point point, Color color, int fontsize) throws RemoteException;
    void deleteShape(int index) throws RemoteException;
    void deleteText(int index) throws RemoteException;
}
