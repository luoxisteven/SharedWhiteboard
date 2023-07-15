package RMI;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteServer extends Remote {
    void register(String userName, IRemoteClient client) throws RemoteException;
    ArrayList<String> getUserList() throws RemoteException;
    void unicastMessage(String userName, String message) throws RemoteException;
    void broadcastMessage(String message) throws RemoteException;
    void initiateDrawBoard(String userName) throws RemoteException;
    void initiateChatBox(String userName) throws RemoteException;
    void setUserList(String userName) throws RemoteException;
    void addShape(Shape shape, Color color, ArrayList<String> userList) throws RemoteException;
    void userAddShape(String userName, Shape shape, Color color) throws RemoteException;
    void addText(String text, Point point, Color color, int fontsize4,
                 ArrayList<String> userList) throws RemoteException;
    void userAddText(String userName, String text, Point point, Color color, int fontsize) throws RemoteException;
    void deleteShape(int index, ArrayList<String> userList) throws RemoteException;
    void userDeleteShape(String userName, int index) throws RemoteException;
    void deleteText(int index, ArrayList<String> userList) throws RemoteException;
    public void userDeleteText(String userName, int index) throws RemoteException;
    void clearDrawBoard(ArrayList<String> userList) throws RemoteException;
    void userClearDrawBoard(String userName) throws RemoteException;
}
