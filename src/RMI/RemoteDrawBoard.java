package RMI;

import Whiteboard.DrawBoard;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RemoteDrawBoard extends UnicastRemoteObject implements IRemoteDrawBoard {
    private String userName;
    private DrawBoard drawBoard;
    private IRemoteUserControl remoteUserControl;

    public RemoteDrawBoard(DrawBoard drawBoard, String userName) throws RemoteException {
        this.drawBoard = drawBoard;
        this.userName = userName;
    }

    public void updateBoard(DrawBoard drawBoard) throws RemoteException {
        this.drawBoard = drawBoard;
    }

    public DrawBoard getDrawBoard() throws RemoteException {
        return drawBoard;
    }

    @Override
    public void addShape(String userName, Shape shape, Color color) throws RemoteException{
        if (!userName.equals(this.userName)){
            drawBoard.remoteAddShape(shape,color);
        }
        ArrayList<String> userList = new ArrayList<>();
        userList.addAll(remoteUserControl.getUserList());
        if (userList.contains(userName)){
            userList.remove(userName);
        }
        remoteUserControl.addShape(shape, color, userList);
    }

    @Override
    public void addText(String userName, String text, Point point, Color color, int fontsize) throws RemoteException{
        if (!userName.equals(this.userName)){
            drawBoard.remoteAddText(text, point, color, fontsize);
        }
        ArrayList<String> userList = new ArrayList<>();
        userList.addAll(remoteUserControl.getUserList());
        if (userList.contains(userName)){
            userList.remove(userName);
        }
        remoteUserControl.addText(text, point, color, fontsize, userList);
    }

    @Override
    public void deleteShape(String userName, int index) throws RemoteException{
        if (!userName.equals(this.userName)){
            drawBoard.remoteDeleteShape(index);
        }
        ArrayList<String> userList = new ArrayList<>();
        userList.addAll(remoteUserControl.getUserList());
        if (userList.contains(userName)){
            userList.remove(userName);
        }
        remoteUserControl.deleteShape(index, userList);
    }

    @Override
    public void deleteText(String userName, int index) throws RemoteException{
        if (!userName.equals(this.userName)){
            drawBoard.remoteDeleteText(index);
        }
        ArrayList<String> userList = new ArrayList<>();
        userList.addAll(remoteUserControl.getUserList());
        if (userList.contains(userName)){
            userList.remove(userName);
        }
        remoteUserControl.deleteText(index, userList);
    }

    @Override
    public void setRemoteUserControl(IRemoteUserControl remoteUserControl){
        this.remoteUserControl = remoteUserControl;
    }

}
