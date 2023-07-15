package RMI;

import Whiteboard.DrawBoard;
import Whiteboard.WhiteBoard;
import org.json.simple.JSONObject;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RemoteClient extends UnicastRemoteObject implements IRemoteClient {
    private WhiteBoard whiteBoard;
    private DrawBoard drawBoard;

    public RemoteClient(WhiteBoard whiteBoard, DrawBoard drawBoard) throws RemoteException {
        this.whiteBoard = whiteBoard;
        this.drawBoard = drawBoard;
    }

    @Override
    public void retrieveMessage(String message) throws RemoteException{
        System.out.println("New message: " + message);
    }

    @Override
    public void initiateCanvas(DrawBoard drawBoard) throws RemoteException{
        this.drawBoard.copyDrawBoard(drawBoard);
    }

    @Override
    public void initiateChatBox(ArrayList<JSONObject> msgObjs) throws RemoteException{
        this.whiteBoard.initiateChatBox(msgObjs);
    }

    @Override
    public void setUserList(ArrayList<String> userList) throws RemoteException{

    }
    @Override
    public void addShape(Shape shape, Color color) throws RemoteException{
        drawBoard.remoteAddShape(shape, color);
    }
    @Override
    public void addText(String text, Point point, Color color, int fontsize) throws RemoteException{
        drawBoard.remoteAddText(text, point, color, fontsize);
    }
    @Override
    public void deleteShape(int index) throws RemoteException{
        drawBoard.remoteDeleteShape(index);
    }

    @Override
    public void deleteText(int index) throws RemoteException{
        drawBoard.remoteDeleteText(index);
    }

    @Override
    public void clearDrawBoard() throws RemoteException{
        drawBoard.remoteClearDrawBoard();
    }
}