package RMI;

import Whiteboard.DrawBoard;
import Whiteboard.WhiteBoard;
import org.json.simple.JSONObject;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RemoteClient extends UnicastRemoteObject implements IRemoteClient {
    private String userName;
    private WhiteBoard whiteBoard;
    private DrawBoard drawBoard;

    public RemoteClient(String userName, WhiteBoard whiteBoard) throws RemoteException {
        this.userName = userName;
        this.whiteBoard = whiteBoard;
        this.drawBoard = whiteBoard.getDrawBoard();
    }

    @Override
    public void retrieveMessage(String message) throws RemoteException{
        System.out.println("New message: " + message);
    }

    @Override
    public void initiateCanvas(String operator, DrawBoard drawBoard) throws RemoteException{
        this.drawBoard.copyDrawBoard(operator, drawBoard);
    }

    @Override
    public void initiateChatBox(ArrayList<JSONObject> msgObjs) throws RemoteException{
        this.whiteBoard.initiateChatBox(msgObjs);
    }

    @Override
    public void addChat(JSONObject msgObj) throws RemoteException{
        this.whiteBoard.remoteAddChat(msgObj);
    }

    @Override
    public void addShape(String operator, Shape shape, Color color) throws RemoteException{
        drawBoard.remoteAddShape(operator, shape, color);
    }

    @Override
    public void addText(String operator, String text, Point point, Color color, int fontsize) throws RemoteException{
        drawBoard.remoteAddText(operator, text, point, color, fontsize);
    }

    @Override
    public void deleteShape(String operator, int index) throws RemoteException{
        drawBoard.remoteDeleteShape(operator, index);
    }

    @Override
    public void deleteText(String operator, int index) throws RemoteException{
        drawBoard.remoteDeleteText(operator, index);
    }

    @Override
    public void clearDrawBoard(String operator) throws RemoteException{
        drawBoard.remoteClearDrawBoard(operator);
    }

    @Override
    public void setUserList(String user, int action) throws RemoteException{
        if (userName.equals(user)&& action == 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    whiteBoard.beingKickedOff();
                }
            }).start();
        } else{
            whiteBoard.setUserJList();
        }
    }
}