package RMI;

import Whiteboard.DrawBoard;
import Whiteboard.WhiteBoard;
import org.json.simple.JSONObject;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Class RemoteClient:
 * Callback by the server, allows the server to conduct operation
 *
 * COMP90015 Distributed Systems, Sem1, 2023
 * @author Xi Luo, 1302954, luoxl7@student.unimelb.edu.au
 * @version jdk18.0.2
 */
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
    public synchronized void retrieveMessage(String message) throws RemoteException{
        System.out.println("New message: " + message); // Message Interface
    }

    @Override
    public synchronized void initiateCanvas(String operator, DrawBoard drawBoard) throws RemoteException{
        this.drawBoard.copyDrawBoard(operator, drawBoard);
    }

    @Override
    public synchronized void initiateChatBox(ArrayList<JSONObject> msgObjs) throws RemoteException{
        this.whiteBoard.initiateChatBox(msgObjs);
    }

    @Override
    public synchronized void addChat(JSONObject msgObj) throws RemoteException{
        this.whiteBoard.remoteAddChat(msgObj);
    }

    @Override
    public synchronized void addShape(String operator, Shape shape, Color color) throws RemoteException{
        drawBoard.remoteAddShape(operator, shape, color);
    }

    @Override
    public synchronized void addText(String operator, String text, Point point, Color color, int fontsize) throws RemoteException{
        drawBoard.remoteAddText(operator, text, point, color, fontsize);
    }

    @Override
    public synchronized void deleteShape(String operator, int index) throws RemoteException{
        drawBoard.remoteDeleteShape(operator, index);
    }

    @Override
    public synchronized void deleteText(String operator, int index) throws RemoteException{
        drawBoard.remoteDeleteText(operator, index);
    }

    @Override
    public synchronized void clearDrawBoard(String operator) throws RemoteException{
        drawBoard.remoteClearDrawBoard(operator);
    }

    @Override
    public synchronized void setUserList(String user, int action) throws RemoteException {
        if (userName.equals(user) && action == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    whiteBoard.beingKickedOff();
                }
            }).start();
        } else {
            whiteBoard.setUserJList();
        }
    }

    @Override
    public synchronized void userNameWarning() throws RemoteException{
        new Thread(new Runnable() {
            @Override
            public void run() {
                whiteBoard.userNameWarning();
            }
        }).start();
    }

    @Override
    public synchronized void serverClosed() throws RemoteException{
        new Thread(new Runnable() {
            @Override
            public void run() {
                whiteBoard.serverCloseWarning();
            }
        }).start();
    }
}