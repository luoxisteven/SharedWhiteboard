package RMI;

import Whiteboard.DrawBoard;
import Whiteboard.WhiteBoard;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemoteUserControl extends UnicastRemoteObject implements IRemoteUserControl {

    private WhiteBoard whiteBoard;
    private DrawBoard drawBoard;
    private ArrayList<String> userList = new ArrayList<>();
    private Map<String, IRemoteClient> clientMap = new HashMap<>();


    public RemoteUserControl(WhiteBoard whiteBoard, DrawBoard drawBoard) throws RemoteException {
        this.whiteBoard = whiteBoard;
        this.drawBoard = drawBoard;
    }

    @Override
    public void register(String userName, IRemoteClient client) throws RemoteException {
        if (!userList.contains(userName)){
            userList.add(userName);
            clientMap.put(userName, client);
            initiateDrawBoard(userName);
        }
    }

    @Override
    public void unicastMessage(String userName, String message) throws RemoteException {
        IRemoteClient client = clientMap.get(userName);
        if (client != null) {
            client.retrieveMessage(message);
        }
    }

    @Override
    public void broadcastMessage(String message) throws RemoteException {
        for (String userName : userList) {
            unicastMessage(userName, message);
        }
    }
    @Override
    public void initiateDrawBoard(String userName) throws RemoteException{
        IRemoteClient client = clientMap.get(userName);
        if (client != null) {
            client.initiateCanvas(drawBoard);
        }
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
    public ArrayList<String> getUserList() throws RemoteException {
        return userList;
    }

    public IRemoteClient getClient(String userName) {
        return clientMap.get(userName);
    }

}
