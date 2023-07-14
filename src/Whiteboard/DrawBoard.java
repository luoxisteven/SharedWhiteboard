package Whiteboard;

import RMI.IRemoteDrawBoard;
import RMI.IRemoteUserControl;

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

public class DrawBoard extends JPanel implements Serializable {
    private int x1, y1, x2, y2;
    private String shape = "Pencil";
    private JTextField textField = new JTextField();
    private Shape tempShape = null;
    private Color currentColor = Color.BLACK;
    private int currentFontSize = 12;
    private java.util.List<Color> shapeColors = new ArrayList<>();
    private java.util.List<Color> textColors = new ArrayList<>();
    private java.util.List<Shape> shapes = new ArrayList<>();
    private java.util.List<String> texts = new ArrayList<>();
    private java.util.List<Integer> textFontSizes = new ArrayList<>();
    private List<Point> textPoints = new ArrayList<>();
    private transient Graphics2D g2;
    private static final double THRESHOLD = 1.0; // Distance Threshold for erasing
    private IRemoteDrawBoard remoteDrawBoard;
    private IRemoteUserControl remoteUserControl;
    private int mode;
    private String userName;

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
                if (!shape.equals("Pencil") || !shape.equals("Eraser")) {
                    addShape();
                }
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
            case "Line":
                tempShape = new Line2D.Double(x1, y1, x2, y2);
                break;
            case "Circle":
                tempShape = new Ellipse2D.Double(x1, y1, diameter, diameter);
                break;
            case "Oval":
                tempShape = new Ellipse2D.Float(x1, y1, x2 - x1, y2 - y1);
                break;
            case "Rectangle":
                tempShape = new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
                break;
            case "Text":
                addText();
                break;
        }
        repaint();
    }

    private void pencilDraw(){
        try {
            if (remoteDrawBoard!=null){
                remoteDrawBoard.addShape(userName,new Line2D.Float(x1, y1, x2, y2),currentColor);
            }
            shapes.add(new Line2D.Float(x1, y1, x2, y2));
            shapeColors.add(currentColor);
            repaint();
            x1 = x2;
            y1 = y2;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void addShape() {
        try {
            if (remoteDrawBoard!=null){
                remoteDrawBoard.addShape(userName,tempShape,currentColor);
            }
            if (tempShape != null) {
                shapeColors.add(currentColor);
                shapes.add(tempShape);
                tempShape = null;
                repaint();
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void addText(){
        try {
            if (remoteDrawBoard!=null){
                remoteDrawBoard.addText(userName, textField.getText(),
                        new Point(x1, y1), currentColor, currentFontSize);
            }
            textColors.add(currentColor);
            texts.add(textField.getText());
            textPoints.add(new Point(x1, y1));
            textFontSizes.add(currentFontSize);
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
                    if (remoteDrawBoard != null) {
                        remoteDrawBoard.deleteShape(userName, i);
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
                    if (remoteDrawBoard != null) {
                        remoteDrawBoard.deleteText(userName, i);
                    }
                    break;
                }
            }
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
        // Add these lines to change the cursor based on the shape
        switch (shape) {
            case "Pencil":
            case "Line":
            case "Circle":
            case "Oval":
            case "Rectangle":
                this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                break;
            case "Text":
                this.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                break;
            case "Eraser":
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                break;
        }
    }

    public void copyDrawBoard(DrawBoard drawBoard){
        this.shapes = drawBoard.getShapes();
        this.shapeColors = drawBoard.getShapeColors();
        this.texts = drawBoard.getTexts();
        this.textColors = drawBoard.getTextColors();
        this.textPoints = drawBoard.getTextPoints();
        this.textFontSizes = drawBoard.getTextFontSizes();
        repaint();
    }

    public void clearDrawBoard() {
        try {
            if (remoteDrawBoard!=null){
                remoteDrawBoard.clearDrawBoard(userName);
            }
            shapes.clear(); // Remove all shapes
            shapeColors.clear(); // Remove all shape colors
            texts.clear(); // Remove all texts
            textPoints.clear(); // Remove all text positions
            textColors.clear(); // Remove all text colors
            textFontSizes.clear();
            repaint(); // Refresh the panel to reflect the changes
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void remoteAddShape(Shape shape, Color color){
        if(shape!=null) {
            shapes.add(shape);
            shapeColors.add(color);
            repaint();
        }
    }

    public void remoteAddText(String text, Point point, Color color, int fontsize){
        texts.add(text);
        textPoints.add(point);
        textColors.add(color);
        textFontSizes.add(fontsize);
        repaint();
    }

    public void remoteDeleteShape(int index){
        if (shapes.size()>index && shapeColors.size()>index){
            shapes.remove(index);
            shapeColors.remove(index);
        }
        repaint();
    }

    public void remoteDeleteText(int index){
        if (texts.size()>index && textPoints.size()>index &&
                textColors.size()>index && textFontSizes.size()>index){
            texts.remove(index);
            textPoints.remove(index);
            textColors.remove(index);
            textFontSizes.remove(index);
        }
        repaint();
    }

    public void remoteClearDrawBoard(){
        shapes.clear(); // Remove all shapes
        shapeColors.clear(); // Remove all shape colors
        texts.clear(); // Remove all texts
        textPoints.clear(); // Remove all text positions
        textColors.clear(); // Remove all text colors
        textFontSizes.clear();
        repaint(); // Refresh the panel to reflect the changes
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

    public IRemoteDrawBoard getRemoteDrawBoard() {
        return remoteDrawBoard;
    }

    public void setRemoteDrawBoard(IRemoteDrawBoard remoteDrawBoard) {
        this.remoteDrawBoard = remoteDrawBoard;
    }

    public void setRemoteUserControl(IRemoteUserControl remoteUserControl){
        this.remoteUserControl = remoteUserControl;
    }
}