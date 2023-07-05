package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteMsg extends Remote {
    public String getMsg() throws RemoteException;
}
