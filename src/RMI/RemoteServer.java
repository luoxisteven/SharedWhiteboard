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
        userList.add(userName);
    }

    @Override
    public synchronized void register(String userName, IRemoteClient client) throws RemoteException {
        if (!userList.contains(userName)){
            userList.add(userName);
            clientMap.put(userName, client);
            addUser(userName);
            whiteBoard.setUserJList();
            initiateDrawBoard(this.userName, userName);
            initiateChatBox(userName);
        } else{
            client.userNameWarning();
        }
    }

    @Override
    public synchronized void unicastMessage(String userName, String message) throws RemoteException {
        IRemoteClient client = clientMap.get(userName);
        if (client != null) {
            client.retrieveMessage(message);
        }
    }

    @Override
    public synchronized void broadcastMessage(String message) throws RemoteException {
        for (String userName : userList) {
            unicastMessage(userName, message);
        }
    }

    @Override
    public synchronized void initiateDrawBoard(String operator, String userName) throws RemoteException{
        IRemoteClient client = clientMap.get(userName);
        if (client != null) {
            client.initiateCanvas(operator, drawBoard);
        }
    }

    @Override
    public synchronized void initiateChatBox(String userName) throws RemoteException{
        IRemoteClient client = clientMap.get(userName);
        if (client != null) {
            client.initiateChatBox(whiteBoard.getMsgObjs());
        }
    }

    public synchronized void addChat(ArrayList<String> userList, JSONObject chatObj) throws RemoteException{
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.addChat(chatObj);
            }
        }
    }

    public synchronized void userAddChat(String userName, JSONObject chatObj) throws RemoteException{
        if (!userName.equals(this.userName)){
            whiteBoard.remoteAddChat(chatObj);
        }
        ArrayList<String> userList = new ArrayList<>(this.userList);
        userList.remove(userName);
        this.addChat(userList,chatObj);
    }

    @Override
    public synchronized void addShape(String operator, Shape shape, Color color,
                         ArrayList<String> userList) throws RemoteException{
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.addShape(operator, shape, color);
            }
        }
    }

    @Override
    public synchronized void userAddShape(String operator, Shape shape, Color color) throws RemoteException{
        if (!operator.equals(this.userName)){
            drawBoard.remoteAddShape(operator, shape, color);
        }
        ArrayList<String> userList = new ArrayList<>(this.userList);
        userList.remove(operator);
        this.addShape(operator, shape, color, userList);
    }

    @Override
    public synchronized void addText(String operator, String text, Point point, Color color,
                        int fontsize, ArrayList<String> userList) throws RemoteException{
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.addText(operator, text, point, color, fontsize);
            }
        }
    }

    @Override
    public synchronized void userAddText(String operator, String text, Point point,
                            Color color, int fontsize) throws RemoteException{
        if (!operator.equals(this.userName)){
            drawBoard.remoteAddText(operator, text, point, color, fontsize);
        }
        ArrayList<String> userList = new ArrayList<>(this.userList);
        userList.remove(operator);
        this.addText(operator, text, point, color, fontsize, userList);
    }

    @Override
    public synchronized void deleteShape(String operator, int index, ArrayList<String> userList) throws RemoteException{
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.deleteShape(operator, index);
            }
        }
    }

    @Override
    public synchronized void userDeleteShape(String operator, int index) throws RemoteException{
        if (!operator.equals(this.userName)){
            drawBoard.remoteDeleteShape(operator, index);
        }
        ArrayList<String> userList = new ArrayList<>(this.userList);
        userList.remove(operator);
        this.deleteShape(operator, index, userList);
    }

    @Override
    public synchronized void deleteText(String operator, int index,
                           ArrayList<String> userList) throws RemoteException{
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.deleteText(operator, index);
            }
        }
    }

    @Override
    public synchronized void userDeleteText(String operator, int index) throws RemoteException{
        if (!operator.equals(this.userName)){
            drawBoard.remoteDeleteText(operator, index);
        }
        ArrayList<String> userList = new ArrayList<>(this.userList);
        userList.remove(operator);
        this.deleteText(operator, index, userList);
    }

    @Override
    public synchronized void clearDrawBoard(String operator, ArrayList<String> userList) throws RemoteException {
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.clearDrawBoard(operator);
            }
        }
    }

    @Override
    public synchronized void userClearDrawBoard(String operator) throws RemoteException{
        if (!operator.equals(this.userName)){
            drawBoard.remoteClearDrawBoard(operator);
        }
        ArrayList<String> userList = new ArrayList<>(this.userList);
        userList.remove(operator);
        this.clearDrawBoard(operator, userList);
    }

    public void addUser(String user) throws RemoteException{
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.setUserList(user, 1);
            }
        }
    }

    @Override
    public synchronized void kickOutUser(String user) throws RemoteException {
        IRemoteClient kickOutClient = clientMap.get(user);
        kickOutClient.setUserList(user, 0);
        userList.remove(user);
        clientMap.remove(user);

        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.setUserList(user, 0);
            }
        }
    }

    @Override
    public ArrayList<String> getUserList() throws RemoteException {
        return userList;
    }

    @Override
    public synchronized void serverClosed() throws RemoteException{
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.serverClosed();
            }
        }
    }

    @Override
    public synchronized void clientClosed(String operator) throws RemoteException{
        userList.remove(operator);
        clientMap.remove(operator);
        for (String userName: userList){
            IRemoteClient client = clientMap.get(userName);
            if (client != null) {
                client.setUserList(operator, 0);
            }
        }
    }
}
