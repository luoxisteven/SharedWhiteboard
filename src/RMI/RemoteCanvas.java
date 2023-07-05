package RMI;

import Whiteboard.WhiteBoard;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RemoteCanvas extends UnicastRemoteObject implements IRemoteCanvas {

    private WhiteBoard whiteBoard;
    private Canvas canvas;

    public RemoteCanvas(String userName) throws RemoteException {
        this.whiteBoard = new WhiteBoard(userName);
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
