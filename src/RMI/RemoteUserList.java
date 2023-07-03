package RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteUserList extends UnicastRemoteObject implements IRemoteUserList {

    protected RemoteUserList() throws RemoteException {

    }
}
