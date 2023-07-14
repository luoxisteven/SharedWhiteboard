package Whiteboard;

import RMI.IRemoteDrawBoard;
import RMI.RemoteDrawBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
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

    public DrawBoard() {

        this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        this.remoteDrawBoard = createRemoteDrawBoard();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();
                y1 = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
                if (!shape.equals("Pencil")) {
                    addShape();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                x2 = e.getX();
                y2 = e.getY();
                if (shape.equals("Pencil")) {
                    shapes.add(new Line2D.Float(x1, y1, x2, y2));
                    shapeColors.add(currentColor);
                    repaint();
                    x1 = x2;
                    y1 = y2;
                } else if (shape.equals("Eraser")) {
                    eraseShape(new Point(x2, y2));
                    repaint ();
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
                textColors.add(currentColor);
                texts.add(textField.getText());
                textPoints.add(new Point(x1, y1));
                textFontSizes.add(currentFontSize);
                break;
        }
        repaint();
    }

    public void addShape() {
        if (tempShape != null) {
            shapeColors.add(currentColor);
            shapes.add(tempShape);
            tempShape = null;
        }
        repaint();
    }

    private void eraseShape(Point point) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (shapes.get(i).contains(point) || isNearLine(shapes.get(i), point)) {
                shapes.remove(i);
                shapeColors.remove(i);
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
                break;
            }
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

    private IRemoteDrawBoard createRemoteDrawBoard(){
        try {
            IRemoteDrawBoard remoteDrawBoard = new RemoteDrawBoard(this);
            return remoteDrawBoard;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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

    public void clearWhiteboard() {
        shapes.clear(); // Remove all shapes
        shapeColors.clear(); // Remove all shape colors
        texts.clear(); // Remove all texts
        textPoints.clear(); // Remove all text positions
        textColors.clear(); // Remove all text colors
        textFontSizes.clear();
        repaint(); // Refresh the panel to reflect the changes
    }

    public void remoteAddShape(Shape shape, Color color){
        shapes.add(shape);
        shapeColors.add(color);
        repaint();
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
//    private void readObject(java.io.ObjectInputStream in)
//            throws IOException, ClassNotFoundException {
//        in.defaultReadObject();
//        this.g2 = (Graphics2D) this.getGraphics();  // 重新初始化g2
//    }
}