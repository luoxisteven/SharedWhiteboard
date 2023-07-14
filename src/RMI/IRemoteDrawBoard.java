package RMI;

import Whiteboard.DrawBoard;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteDrawBoard extends Remote {
    DrawBoard getDrawBoard() throws RemoteException;
    void addShape(String userName, Shape shape, Color color) throws RemoteException;
    void addText(String userName, String text, Point point, Color color, int fontsize) throws RemoteException;
    void deleteShape(String userName, int index) throws RemoteException;
    void deleteText(String userName, int index) throws RemoteException;
    void setRemoteUserControl(IRemoteUserControl remoteUserControl) throws RemoteException;
}
