package RMI;

import Whiteboard.DrawBoard;
import org.json.simple.JSONObject;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteClient extends Remote {
    void retrieveMessage(String message) throws RemoteException;
    void initiateCanvas(String operator, DrawBoard drawBoard) throws RemoteException;
    void initiateChatBox(ArrayList<JSONObject> msgObjs) throws RemoteException;
    void addChat(JSONObject msgObj) throws RemoteException;
    void addShape(String operator, Shape shape, Color color) throws RemoteException;
    void addText(String operator, String text, Point point, Color color, int fontsize) throws RemoteException;
    void deleteShape(String operator, int index) throws RemoteException;
    void deleteText(String operator, int index) throws RemoteException;
    void clearDrawBoard(String operator) throws RemoteException;
    void setUserList(String user, int action) throws RemoteException;
    void userNameWarning() throws RemoteException;
    public void serverClosed() throws RemoteException;
}
