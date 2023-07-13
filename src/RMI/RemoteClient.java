package RMI;

import Whiteboard.DrawBoard;
import Whiteboard.WhiteBoard;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteClient extends UnicastRemoteObject implements IRemoteClient {

    private WhiteBoard whiteBoard;
    private DrawBoard drawBoard;

    public RemoteClient(WhiteBoard whiteBoard, DrawBoard drawBoard) throws RemoteException {
        this.whiteBoard = whiteBoard;
        this.drawBoard = drawBoard;
    }

    @Override
    public void retrieveMessage(String message) throws RemoteException {
        System.out.println("New message: " + message);
    }

    public void initiateCanvas(RemoteDrawBoard remoteDrawBoard) throws RemoteException{
        drawBoard.copyRemoteToLocal(remoteDrawBoard);
    }

    public void addShape(Shape shape, Color color) throws RemoteException{
        drawBoard.remoteAddShape(shape, color);
    }

    public void addText(String text, Point point, Color color, int fontsize) throws RemoteException{
        drawBoard.remoteAddText(text, point, color, fontsize);
    }

    public void deleteShape(int index) throws RemoteException{
        drawBoard.remoteDeleteShape(index);
    }
    public void deleteText(int index) throws RemoteException{
        drawBoard.remoteDeleteText(index);
    }

}