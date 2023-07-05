package RMI;

import Whiteboard.DrawBoard;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteDrawBoard extends Remote{
    void updateBoard(DrawBoard drawBoard) throws RemoteException;
    DrawBoard getDrawBoard() throws RemoteException;
}
