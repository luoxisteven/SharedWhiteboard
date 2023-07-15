package RMI;

import Whiteboard.DrawBoard;
import Whiteboard.WhiteBoard;
import org.json.simple.JSONObject;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RemoteServer extends UnicastRemoteObject implements IRemoteServer {
    private String userName;
    private WhiteBoard whiteBoard;
    private DrawBoard drawBoard;
    private ArrayList<String> userList = new ArrayList<>();
    private Map<String, IRemoteClient> clientMap = new HashMap<>();

    public RemoteServer(String userName, WhiteBoard whiteBoard) throws RemoteException {
        this.userName = userName;
        this.whiteBoard = whiteBoard;
        this.drawBoard = whiteBoard.getDrawBoard();
    }

    @Override
    public void register(String userName, IRemoteClient client) throws RemoteException {
        if (!userList.contains(userName)){
            userList.add(userName);
            clientMap.put(userName, client);
            initiateDrawBoard(userName);
            initiateChatBox(userName);
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
    public void initiateChatBox(String userName) throws RemoteException{
        IRemoteClient client = clientMap.get(userName);
        if (client != null) {
            client.initiateChatBox(whiteBoard.getMsgObjs());
        }
    }

    public void addChat(ArrayList<String> userList, JSONObject chatObj) throws RemoteException{
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.addChat(chatObj);
            }
        }
    }

    public void userAddChat(String userName, JSONObject chatObj) throws RemoteException{
        if (!userName.equals(this.userName)){
            whiteBoard.remoteAddChat(chatObj);
        }
        ArrayList<String> userList = new ArrayList<>(this.userList);
        userList.remove(userName);
        this.addChat(userList,chatObj);
    }

    @Override
    public void setUserList(String userName) throws RemoteException{
        IRemoteClient client = clientMap.get(userName);
        if (client != null) {
            client.setUserList(userList);
        }
    }

    public void addUserToList(String userName) throws RemoteException{

    }

    public void deleteUserFromList(String userName) throws RemoteException{

    }

    @Override
    public void addShape(Shape shape, Color color, ArrayList<String> userList) throws RemoteException{
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.addShape(shape, color);
            }
        }
    }

    @Override
    public void userAddShape(String userName, Shape shape, Color color) throws RemoteException{
        if (!userName.equals(this.userName)){
            drawBoard.remoteAddShape(shape,color);
        }
        ArrayList<String> userList = new ArrayList<>(this.userList);
        userList.remove(userName);
        this.addShape(shape, color, userList);
    }

    @Override
    public void addText(String text, Point point, Color color, int fontsize,
                        ArrayList<String> userList) throws RemoteException{
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.addText(text, point, color, fontsize);
            }
        }
    }

    @Override
    public void userAddText(String userName, String text, Point point, Color color, int fontsize) throws RemoteException{
        if (!userName.equals(this.userName)){
            drawBoard.remoteAddText(text, point, color, fontsize);
        }
        ArrayList<String> userList = new ArrayList<>(this.userList);
        userList.remove(userName);
        this.addText(text, point, color, fontsize, userList);
    }

    @Override
    public void deleteShape(int index, ArrayList<String> userList) throws RemoteException{
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.deleteShape(index);
            }
        }
    }

    @Override
    public void userDeleteShape(String userName, int index) throws RemoteException{
        if (!userName.equals(this.userName)){
            drawBoard.remoteDeleteShape(index);
        }
        ArrayList<String> userList = new ArrayList<>(this.userList);
        userList.remove(userName);
        this.deleteShape(index, userList);
    }

    @Override
    public void deleteText(int index, ArrayList<String> userList) throws RemoteException{
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.deleteText(index);
            }
        }
    }

    @Override
    public void userDeleteText(String userName, int index) throws RemoteException{
        if (!userName.equals(this.userName)){
            drawBoard.remoteDeleteText(index);
        }
        ArrayList<String> userList = new ArrayList<>(this.userList);
        userList.remove(userName);
        this.deleteText(index, userList);
    }

    @Override
    public void clearDrawBoard(ArrayList<String> userList) throws RemoteException {
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.clearDrawBoard();
            }
        }
    }

    @Override
    public void userClearDrawBoard(String userName) throws RemoteException{
        if (!userName.equals(this.userName)){
            drawBoard.remoteClearDrawBoard();
        }
        ArrayList<String> userList = new ArrayList<>(this.userList);
        userList.remove(userName);
        this.clearDrawBoard(userList);
    }

    @Override
    public ArrayList<String> getUserList() throws RemoteException {
        return userList;
    }

}
