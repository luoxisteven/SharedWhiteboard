package RMI;

import Whiteboard.DrawBoard;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteDrawBoard extends Remote {
    DrawBoard getDrawBoard() throws RemoteException;
    void addShape(Shape shape, Color color) throws RemoteException;
    void addText(String text, Point point, Color color, int fontsize4) throws RemoteException;
}
