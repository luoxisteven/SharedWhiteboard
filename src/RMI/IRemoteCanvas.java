package RMI;

import Whiteboard.WhiteBoard;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteCanvas extends Remote{
    void updateBoard(Canvas canvas) throws RemoteException;
    Canvas getCanvas() throws RemoteException;
}
