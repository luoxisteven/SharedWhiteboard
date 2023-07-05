package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteMsg extends Remote {
    String getMsg() throws RemoteException;
}
