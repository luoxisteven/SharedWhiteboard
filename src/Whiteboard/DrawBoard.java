package Whiteboard;

import RMI.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class DrawBoard extends JPanel{
    private int x1, y1, x2, y2;
    private String shape = "Pencil";
    private JTextField textField = new JTextField();
    private Shape tempShape = null;
    private Color currentColor = Color.BLACK;
    private int currentFontSize = 12;
    private transient Graphics2D g2;
    private List<Color> shapeColors = new ArrayList<>();
    private List<Color> textColors = new ArrayList<>();
    private List<Shape> shapes = new ArrayList<>();
    private List<String> texts = new ArrayList<>();
    private List<Point> textPoints = new ArrayList<>();
    private List<Integer> textFontSizes = new ArrayList<>();
    private JList<String> userJList;
    private static final double THRESHOLD = 1.0; // Distance Threshold for erasing
    private String userName;
    private int mode;
    private IRemoteServer remoteServer;

    public DrawBoard(String userName, int mode) {
        this.userName = userName;
        this.mode = mode;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
            }
            public void mouseReleased(MouseEvent e) {
                addShape();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();
                if (shape.equals("Pencil")) {
                    pencilDraw();
                } else if (shape.equals("Eraser")) {
                    eraseShape(new Point(x2, y2));
                } else {
                    draw();
                }
            }
        });
        setBackground(Color.WHITE);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < shapes.size(); i++) {
            g2.setColor(shapeColors.get(i));
            g2.draw(shapes.get(i));
        }
        for (int i = 0; i < texts.size(); i++) {
            g2.setColor(textColors.get(i));
            g2.setFont(new Font("default", Font.PLAIN, textFontSizes.get(i)));
            g2.drawString(texts.get(i), textPoints.get(i).x, textPoints.get(i).y);
        }
        if (tempShape != null) {
            g2.setColor(currentColor);
            g2.draw(tempShape);
        }
    }

    public void draw() {
        int diameter = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        switch (shape) {
            case "Line" -> tempShape = new Line2D.Double(x1, y1, x2, y2);
            case "Circle" -> tempShape = new Ellipse2D.Double(x1, y1, diameter, diameter);
            case "Oval" -> tempShape = new Ellipse2D.Float(x1, y1, x2 - x1, y2 - y1);
            case "Rectangle" -> tempShape = new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
            case "Text" -> addText();
        }
        repaint();
    }

    private void pencilDraw(){
        try {
            if (remoteServer!=null){
                remoteServer.userAddShape(userName,new Line2D.Float(x1, y1, x2, y2),currentColor);
            }
            shapes.add(new Line2D.Float(x1, y1, x2, y2));
            shapeColors.add(currentColor);
            setSelectedUser(userName);
            repaint();
            x1 = x2;
            y1 = y2;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void addShape() {
        try {
            if (remoteServer!=null){
                remoteServer.userAddShape(userName,tempShape,currentColor);
            }
            if (tempShape != null) {
                shapeColors.add(currentColor);
                shapes.add(tempShape);
                tempShape = null;
                setSelectedUser(userName);
                repaint();
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void addText(){
        try {
            if (remoteServer!=null){
                remoteServer.userAddText(userName, textField.getText(),
                        new Point(x1, y1), currentColor, currentFontSize);
            }
            texts.add(textField.getText());
            textColors.add(currentColor);
            textPoints.add(new Point(x1, y1));
            textFontSizes.add(currentFontSize);
            setSelectedUser(userName);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void eraseShape(Point point) {
        try {
            for (int i = shapes.size() - 1; i >= 0; i--) {
                if (shapes.get(i).contains(point) || isNearLine(shapes.get(i), point)) {
                       shapes.remove(i);
                    shapeColors.remove(i);
                    if (remoteServer != null) {
                        remoteServer.userDeleteShape(userName, i);
                    }
                    break;
                }
            }
            for (int i = textPoints.size() - 1; i >= 0; i--) {
                Rectangle textBound = new Rectangle(textPoints.get(i).x, textPoints.get(i).y - g2.getFontMetrics().getHeight(),
                        g2.getFontMetrics().stringWidth(texts.get(i)), g2.getFontMetrics().getHeight());
                if (textBound.contains(point)) {
                    texts.remove(i);
                    textPoints.remove(i);
                    textColors.remove(i);
                    textFontSizes.remove(i);
                    if (remoteServer != null) {
                        remoteServer.userDeleteText(userName, i);
                    }
                    break;
                }
            }
            setSelectedUser(userName);
            repaint ();
        } catch (RemoteException e) {
                throw new RuntimeException(e);
        }
    }

    private boolean isNearLine(Shape shape, Point point) {
        if (shape instanceof Line2D) {
            Line2D line = (Line2D) shape;
            double distance = line.ptLineDist(point);
            return distance <= THRESHOLD;
        }
        return false;
    }

    public void setShape(String shape) {
        this.shape = shape;
        switch (shape) {
            case "Pencil", "Line", "Circle", "Oval", "Rectangle" ->
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            case "Text" -> this.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            case "Eraser" -> this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    public void copyDrawBoard(String operator, DrawBoard drawBoard){
        this.shapes = drawBoard.getShapes();
        this.shapeColors = drawBoard.getShapeColors();
        this.texts = drawBoard.getTexts();
        this.textColors = drawBoard.getTextColors();
        this.textPoints = drawBoard.getTextPoints();
        this.textFontSizes = drawBoard.getTextFontSizes();
        this.setSelectedUser(operator);
        repaint();
    }

    public void clearDrawBoard() {
        try {
            if (remoteServer!=null){
                remoteServer.userClearDrawBoard(userName);
            }
            shapes.clear();
            shapeColors.clear();
            texts.clear();
            textPoints.clear();
            textColors.clear();
            textFontSizes.clear();
            setSelectedUser(userName);
            repaint();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void remoteAddShape(String operator, Shape shape, Color color){
        if(shape!=null) {
            shapes.add(shape);
            shapeColors.add(color);
            setSelectedUser(operator);
            repaint();
        }
    }

    public void remoteAddText(String operator, String text, Point point, Color color, int fontsize){
        texts.add(text);
        textPoints.add(point);
        textColors.add(color);
        textFontSizes.add(fontsize);
        setSelectedUser(operator);
        repaint();
    }

    public void remoteDeleteShape(String operator, int index){
        if (shapes.size()>index && shapeColors.size()>index){
            shapes.remove(index);
            shapeColors.remove(index);
        }
        setSelectedUser(operator);
        repaint();
    }

    public void remoteDeleteText(String operator, int index){
        if (texts.size()>index && textPoints.size()>index &&
                textColors.size()>index && textFontSizes.size()>index){
            texts.remove(index);
            textPoints.remove(index);
            textColors.remove(index);
            textFontSizes.remove(index);
        }
        setSelectedUser(operator);
        repaint();
    }

    public void remoteClearDrawBoard(String operator){
        shapes.clear();
        shapeColors.clear(); 
        texts.clear();
        textPoints.clear();
        textColors.clear();
        textFontSizes.clear();
        setSelectedUser(operator);
        repaint();
    }

    public List<Color> getShapeColors() {
        return shapeColors;
    }

    public List<Color> getTextColors() {
        return textColors;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public List<String> getTexts() {
        return texts;
    }

    public List<Integer> getTextFontSizes() {
        return textFontSizes;
    }

    public void setShapeColors(List<Color> shapeColors) {
        this.shapeColors = shapeColors;
    }

    public void setTextColors(List<Color> textColors) {
        this.textColors = textColors;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

    public void setTextPoints(List<Point> textPoints) {
        this.textPoints = textPoints;
    }

    public void setTextFontSizes(List<Integer> textFontSizes) {
        this.textFontSizes = textFontSizes;
    }

    public List<Point> getTextPoints() {
        return textPoints;
    }

    public void setTextField(String text){
        textField.setText(text);
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public Color getCurrentColor(){
        return this.currentColor;
    }

    public void setCurrentFontSize(int size) {
        this.currentFontSize = size;
    }

    public void setRemoteServer(IRemoteServer remoteServer) {
        this.remoteServer = remoteServer;
    }

    public void setUserJList(JList<String> userJList) {
        this.userJList = userJList;
    }

    public void setSelectedUser(String userName){
        userJList.setSelectedValue(userName,true);
    }
}