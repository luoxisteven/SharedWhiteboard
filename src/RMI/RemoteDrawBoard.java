package RMI;

import Whiteboard.DrawBoard;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteDrawBoard extends UnicastRemoteObject implements IRemoteDrawBoard {
    private DrawBoard drawBoard;

    public RemoteDrawBoard() throws RemoteException {

    }

    @Override
    public void updateBoard(DrawBoard drawBoard) throws RemoteException {

    }

    public DrawBoard getDrawBoard() {
        return drawBoard;
    }

    public void setDrawBoard(DrawBoard drawBoard) {
        this.drawBoard = drawBoard;
    }
}
