package RMI;

import org.json.simple.JSONObject;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteServer extends Remote {
    void register(String userName, IRemoteClient client) throws RemoteException;
    ArrayList<String> getUserList() throws RemoteException;
    void unicastMessage(String userName, String message) throws RemoteException;
    void broadcastMessage(String message) throws RemoteException;
    void initiateDrawBoard(String operator, String userName) throws RemoteException;
    void initiateChatBox(String userName) throws RemoteException;
    public void addChat(ArrayList<String> userList, JSONObject chatObj) throws RemoteException;
    void userAddChat(String userName, JSONObject chatObj) throws RemoteException;
    void addShape(String operator, Shape shape, Color color, ArrayList<String> userList) throws RemoteException;
    void userAddShape(String operator, Shape shape, Color color) throws RemoteException;
    void addText(String operator, String text, Point point, Color color, int fontsize4,
                 ArrayList<String> userList) throws RemoteException;
    void userAddText(String operator, String text, Point point, Color color, int fontsize) throws RemoteException;
    void deleteShape(String operator, int index, ArrayList<String> userList) throws RemoteException;
    void userDeleteShape(String operator, int index) throws RemoteException;
    void deleteText(String operator, int index, ArrayList<String> userList) throws RemoteException;
    void userDeleteText(String operator, int index) throws RemoteException;
    void clearDrawBoard(String operator, ArrayList<String> userList) throws RemoteException;
    void userClearDrawBoard(String userName) throws RemoteException;
    void kickOutUser(String user) throws RemoteException;

}
