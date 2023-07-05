package RMI;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteMsg extends UnicastRemoteObject implements IRemoteMsg {

    public RemoteMsg() throws RemoteException {

    }
    public String getMsg() throws RemoteException{
        return "Hello";
    }

}
