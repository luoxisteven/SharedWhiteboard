package RMI;

import Whiteboard.DrawBoard;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRemoteDrawBoard extends Remote{
    void updateBoard(DrawBoard drawBoard) throws RemoteException;
    DrawBoard getDrawBoard() throws RemoteException;

    // methods related to Overlay

    List<Shape> getShapes() throws RemoteException;
    void setShapes(List<Shape> shapes) throws RemoteException;

    List<Color> getShapeColors() throws RemoteException;
    void setShapeColors(List<Color> shapeColors) throws RemoteException;

    List<String> getTexts() throws RemoteException;
    void setTexts(List<String> texts) throws RemoteException;

    List<Color> getTextColors() throws RemoteException;
    void setTextColors(List<Color> textColors) throws RemoteException;

    List<Integer> getTextFontSizes() throws RemoteException;
    void setTextFontSizes(List<Integer> textFontSizes) throws RemoteException;

    List<Point> getTextPoints() throws RemoteException;
    void setTextPoints(List<Point> textPoints) throws RemoteException;
}
