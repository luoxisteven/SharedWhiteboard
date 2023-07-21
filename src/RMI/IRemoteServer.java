package RMI;

import org.json.simple.JSONObject;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

// TODO: 修改一下接口

public interface IRemoteServer extends Remote {
    void register(String userName, IRemoteClient client) throws RemoteException;
    ArrayList<String> getUserList() throws RemoteException;
    void unicastMessage(String userName, String message) throws RemoteException;
    void broadcastMessage(String message) throws RemoteException;
    void initiateDrawBoard(String operator, String userName) throws RemoteException; // TODO: 不能给
    void initiateChatBox(String userName) throws RemoteException; // TODO: 不能给
    public void addChat(ArrayList<String> userList, JSONObject chatObj) throws RemoteException; // TODO: 不能给
    void userAddChat(String userName, JSONObject chatObj) throws RemoteException;
    void addShape(String operator, Shape shape, Color color, ArrayList<String> userList) throws RemoteException; // TODO: 不能给
    void userAddShape(String operator, Shape shape, Color color) throws RemoteException; // TODO: 不能给
    void addText(String operator, String text, Point point, Color color, int fontsize4, // TODO: 不能给
                 ArrayList<String> userList) throws RemoteException;
    void userAddText(String operator, String text, Point point, Color color, int fontsize) throws RemoteException;
    void deleteShape(String operator, int index, ArrayList<String> userList) throws RemoteException; // TODO: 不能给
    void userDeleteShape(String operator, int index) throws RemoteException;
    void deleteText(String operator, int index, ArrayList<String> userList) throws RemoteException; // TODO: 不能给
    void userDeleteText(String operator, int index) throws RemoteException;
    void clearDrawBoard(String operator, ArrayList<String> userList) throws RemoteException; // TODO: 不能给
    void userClearDrawBoard(String userName) throws RemoteException;
    void kickOutUser(String user) throws RemoteException; // TODO: 不能给
    void serverClosed() throws RemoteException; // TODO: 不能给
    void clientClosed(String operator) throws RemoteException;
}
