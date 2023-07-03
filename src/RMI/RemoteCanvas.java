package RMI;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RemoteCanvas extends UnicastRemoteObject implements IRemoteCanvas {

    private Canvas canvas;

    public RemoteCanvas(Canvas canvas) throws RemoteException {
        this.canvas = canvas;
    }

    @Override
    public void updateBoard(Canvas canvas) throws RemoteException {

    }

    @Override
    public Canvas getCanvas() throws RemoteException {
        return this.canvas;
    }

    public void setCanvas(Canvas canvas){
        this.canvas = canvas;
    }

}
