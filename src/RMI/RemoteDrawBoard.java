package RMI;

import Whiteboard.DrawBoard;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteDrawBoard extends UnicastRemoteObject implements IRemoteDrawBoard {
    private DrawBoard drawBoard;

    public RemoteDrawBoard(DrawBoard drawBoard) throws RemoteException {
        this.drawBoard = drawBoard;
    }

    public void updateBoard(DrawBoard drawBoard) throws RemoteException {
        this.drawBoard = drawBoard;
    }

    public DrawBoard getDrawBoard() throws RemoteException {
        return drawBoard;
    }

    public void addShape(Shape shape, Color color) throws RemoteException{
        drawBoard.remoteAddShape(shape,color);
    }

    public void addText(String text, Point point, Color color, int fontsize4) throws RemoteException{
        drawBoard.remoteAddText(text, point, color, fontsize4);
    }

}
