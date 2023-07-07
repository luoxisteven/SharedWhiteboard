package RMI;

import Whiteboard.DrawBoard;
import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RemoteDrawBoard extends UnicastRemoteObject implements IRemoteDrawBoard {
    private DrawBoard drawBoard;
    private List<Color> shapeColors = new ArrayList<>();
    private List<Color> textColors = new ArrayList<>();
    private List<Shape> shapes = new ArrayList<>();
    private List<String> texts = new ArrayList<>();
    private List<Integer> textFontSizes = new ArrayList<>();
    private List<Point> textPoints = new ArrayList<>();

    public RemoteDrawBoard() throws RemoteException {

    }

    @Override
    public void updateBoard(DrawBoard drawBoard) throws RemoteException {
        this.drawBoard = drawBoard;
    }

    @Override
    public DrawBoard getDrawBoard() throws RemoteException {
        return drawBoard;
    }

    // Getters and Setters
    public List<Color> getShapeColors() {
        return shapeColors;
    }

    public void setShapeColors(List<Color> shapeColors) {
        this.shapeColors = shapeColors;
    }

    public List<Color> getTextColors() {
        return textColors;
    }

    public void setTextColors(List<Color> textColors) {
        this.textColors = textColors;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    public List<Integer> getTextFontSizes() {
        return textFontSizes;
    }

    public void setTextFontSizes(List<Integer> textFontSizes) {
        this.textFontSizes = textFontSizes;
    }

    public List<Point> getTextPoints() {
        return textPoints;
    }

    public void setTextPoints(List<Point> textPoints) {
        this.textPoints = textPoints;
    }
}
