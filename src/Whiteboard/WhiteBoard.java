package Whiteboard;

import RMI.*;
import org.json.simple.JSONObject;

import java.io.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Whiteboard
 *
 * COMP90015 Distributed Systems, Sem1, 2023
 * @author Xi Luo, 1302954, luoxl7@student.unimelb.edu.au
 * @version jdk18.0.2
 */
public class WhiteBoard extends JFrame{
    private DrawBoard drawBoard;
    private JTextArea chatArea;
    private JList<String> userJList;
    private ArrayList<JSONObject> msgObjs = new ArrayList<>();
    private String userName;
    private int mode; // 0 is Server, 1 is Client
    private IRemoteServer remoteServer;

    public WhiteBoard(String userName, int mode) {
        this.userName = userName;
        this.mode = mode;

        if (mode ==0){
            setTitle("Shared Whiteboard Server: " + userName);
        } else if (mode == 1) {
            setTitle("Shared Whiteboard Client: " + userName);
        } else{
            setTitle("Shared Whiteboard: " + userName);
        }
        setSize(1100, 600);
        setLocationRelativeTo(null);

        // Draw Board
        this.drawBoard = new DrawBoard(userName, mode);
        add(drawBoard);

        // West Panel
        JPanel westPanel = new JPanel(); // New west panel that will contain controlPanel and usersPane
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS)); // Set layout to BoxLayout
        JPanel controlPanel = createControlPanel();
        JScrollPane usersPanel = createUsersPanel();
        if (mode==0){
            setJMenuBar(createMenuBar());
        }
        westPanel.add(controlPanel);
        westPanel.add(usersPanel);
        add(westPanel, BorderLayout.WEST);

        // Chat Panel
        JPanel chatPanel = createChatPanel();
        add(chatPanel, BorderLayout.EAST);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(mode==0){
                    serverClose();
                } else if(mode==1){
                    clientClose();
                } else{
                    System.gc();
                    System.exit(0);
                }
            }
        });

        setVisible(true);
        drawBoard.setUserJList(userJList);
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(100, 260));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        // create a titled border with the title "Tools"
        Border titledBorder = BorderFactory.createTitledBorder("Tools");
        controlPanel.setBorder(titledBorder);  // add the border to the panel

        String[] buttonLabels = {"Pencil", "Line", "Circle", "Oval", "Rectangle"};
        for (String label : buttonLabels) {
            controlPanel.add(createButton(label));
        }

        controlPanel.add(createTextButton());
        controlPanel.add(createColorButton());
        controlPanel.add(createButton("Eraser"));

        return controlPanel;
    }

    private JButton createButton(String label) {
        JButton button = new JButton(label);
        button.setMaximumSize(new Dimension(100, 30));
        button.addActionListener(e -> drawBoard.setShape(e.getActionCommand()));
        return button;
    }

    private JButton createTextButton() {
        JButton textButton = new JButton("Text");
        textButton.setMaximumSize(new Dimension(100, 30));

        textButton.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "Text", true);
            JPanel contentPane = new JPanel();
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

            JPanel textPanel = new JPanel(new FlowLayout());
            textPanel.add(new JLabel("Enter text:")); // Add a label for the input field
            JTextField inputField = new JTextField(20);
            textPanel.add(inputField);
            contentPane.add(textPanel);

            JTextField fontSizeField = new JTextField(5); // Add this field for font size input
            JPanel fontSizePanel = new JPanel(new FlowLayout());
            fontSizePanel.add(new JLabel("Font Size:")); // Add a label for the field
            fontSizePanel.add(fontSizeField);
            contentPane.add(fontSizePanel);

            JButton okButton = new JButton("OK");
            okButton.addActionListener(e1 -> {
                drawBoard.setTextField(inputField.getText());
                try {
                    int fontSize = Integer.parseInt(fontSizeField.getText());
                    if (fontSize <= 0) {
                        throw new NumberFormatException("Font size should be a positive number.");
                    }
                    drawBoard.setCurrentFontSize(fontSize);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid font size.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                dialog.dispose();
            });
            contentPane.add(okButton);

            dialog.setContentPane(contentPane);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

            drawBoard.setShape(e.getActionCommand());
        });
        return textButton;
    }

    private JButton createColorButton() {
        JButton colorButton = new JButton("Color");
        colorButton.setMaximumSize(new Dimension(100,30)); // Make button fill horizontal space
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Choose a color",
                        drawBoard.getCurrentColor());
                if (newColor != null) {
                    drawBoard.setCurrentColor(newColor); // Use the new setter here
                }
            }
        });
        return colorButton;
    }

    private JPanel createChatPanel() {
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());

        // create a titled border with the title "Chat Box"
        Border titledBorder = BorderFactory.createTitledBorder("Chat");
        chatPanel.setBorder(titledBorder);  // add the border to the panel

        chatArea = new JTextArea();
        chatArea.setEditable(false); // The chat history should not be editable
        chatArea.setLineWrap(true);
        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatPanel.add(chatScroll, BorderLayout.CENTER);

        JPanel sendMessagePanel = new JPanel();
        sendMessagePanel.setLayout(new BorderLayout());
        JTextField messageField = new JTextField();
        sendMessagePanel.add(messageField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Date now = new Date( );
                    SimpleDateFormat timeFormat = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                    JSONObject chatObj = new JSONObject();
                    chatObj.put("UserName",userName);
                    chatObj.put("Time",timeFormat.format(now));
                    chatObj.put("Msg",messageField.getText());
                    msgObjs.add(chatObj);
                    if (remoteServer!=null){
                        remoteServer.userAddChat(userName,chatObj);
                    }
                    String chat = chatArea.getText()+userName+" ("+timeFormat.format(now) +"): "+messageField.getText();
                    chatArea.setText(chat + "\n\n");
                    messageField.setText("");
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        sendMessagePanel.add(sendButton, BorderLayout.EAST);
        chatPanel.add(sendMessagePanel, BorderLayout.SOUTH);
        chatPanel.setPreferredSize(new Dimension(250, getHeight()));  // set the preferred size

        return chatPanel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // create 'File' menu
        JMenu fileMenu = new JMenu("  File  ");

        // create menu items
        JMenuItem loadItem = createLoadItem();
        JMenuItem saveItem = createSaveItem();

        // add items to 'File' menu
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);

        // add menus to menu bar
        menuBar.add(fileMenu);

        // create 'File' menu
        JMenu editMenu = createEditMenu();
        menuBar.add(editMenu);

        return menuBar;
    }

    public JMenu createEditMenu(){
        JMenu controlMenu = new JMenu(" Edit ");
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawBoard.clearDrawBoard(); // Clear the whiteboard
            }
        });

        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverClose();
            }
        });

        controlMenu.add(newItem);
        controlMenu.add(closeItem);
        return controlMenu;
    }

    public JMenuItem createLoadItem(){
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter serFilter =
                            new FileNameExtensionFilter("SER Files (*.ser)", "ser");
                    fileChooser.addChoosableFileFilter(serFilter);
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    int returnVal = fileChooser.showOpenDialog(null);
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        FileNameExtensionFilter selectedFilter = (FileNameExtensionFilter) fileChooser.getFileFilter();

                        // Get the selected file extension
                        String selectedExtension = selectedFilter.getExtensions()[0];
                        if (selectedExtension.toLowerCase().equals("ser")) {
                            loadFromSer(selectedFile.getAbsolutePath());
                        } else {
                            JOptionPane.showMessageDialog(null, "Unsupported file format");
                        }
                    }
                    for (String user: remoteServer.getUserList()){
                        remoteServer.initiateDrawBoard(userName, user);
                    }
                    userJList.setSelectedValue(userName,true);
                } catch (RemoteException ex){
                    throw new RuntimeException(ex);
                }
            }
        });
        return loadItem;
    }

    private void loadFromSer(String filePath) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
            drawBoard.setShapes((List<Shape>) in.readObject());
            drawBoard.setShapeColors((List<Color>) in.readObject());
            drawBoard.setTexts((List<String>) in.readObject());
            drawBoard.setTextColors((List<Color>) in.readObject());
            drawBoard.setTextPoints((List<Point>) in.readObject());
            drawBoard.setTextFontSizes((List<Integer>) in.readObject());
            drawBoard.repaint();
            in.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public JMenuItem createSaveItem(){
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter serFilter =
                        new FileNameExtensionFilter("SER Files (*.ser)", "ser");
                FileNameExtensionFilter pngFilter =
                        new FileNameExtensionFilter("PNG Images (*.png)", "png");
                FileNameExtensionFilter jpgFilter =
                        new FileNameExtensionFilter("JPG Images (*.jpg)", "jpg");

                fileChooser.addChoosableFileFilter(serFilter);
                fileChooser.addChoosableFileFilter(pngFilter);
                fileChooser.addChoosableFileFilter(jpgFilter);

                // Disable the "All Files" filter.
                fileChooser.setAcceptAllFileFilterUsed(false);

                int returnVal = fileChooser.showSaveDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    FileNameExtensionFilter selectedFilter = (FileNameExtensionFilter) fileChooser.getFileFilter();

                    // Get the selected file extension
                    String selectedExtension = selectedFilter.getExtensions()[0];

                    // Append extension if not present
                    String filePath = selectedFile.getAbsolutePath();
                    if (!filePath.endsWith("." + selectedExtension)) {
                        filePath += "." + selectedExtension;
                    }

                    switch (selectedExtension.toLowerCase()) {
                        case "ser":
                            saveAsSer(filePath);
                            break;
                        case "png":
                        case "jpg":
                            saveAsImage(filePath, selectedExtension);
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Unsupported file format");
                    }
                }
            }
        });
        return saveItem;
    }

    private void saveAsSer(String filePath) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(drawBoard.getShapes());
            out.writeObject(drawBoard.getShapeColors());
            out.writeObject(drawBoard.getTexts());
            out.writeObject(drawBoard.getTextColors());
            out.writeObject(drawBoard.getTextPoints());
            out.writeObject(drawBoard.getTextFontSizes());
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveAsImage(String filePath, String extension) {
        BufferedImage image = new BufferedImage(drawBoard.getWidth(), drawBoard.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        drawBoard.paint(g2d);
        g2d.dispose();
        try {
            ImageIO.write(image, extension, new File(filePath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void initiateChatBox(ArrayList<JSONObject> msgObjs){
        this.msgObjs = msgObjs;
        String chat = "";
        for(JSONObject msgObj: msgObjs){
            chat += msgObj.get("UserName")+" ("+msgObj.get("Time")+"): "+msgObj.get("Msg")+"\n\n";
        }
        chatArea.setText(chat);
    }

    public void remoteAddChat(JSONObject msgObj){
        msgObjs.add(msgObj);
        String chat = chatArea.getText()+msgObj.get("UserName")+" ("+msgObj.get("Time")+"): "+msgObj.get("Msg");
        chatArea.setText(chat + "\n\n");
    }

    public JScrollPane createUsersPanel(){
        try {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            if (remoteServer!=null){
                userJList = new JList<>(remoteServer.getUserList().toArray(new String[0]));
            } else {
                String[] user = {this.userName};
                userJList = new JList<>(user);
            }
            userJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only single selection
            userJList.setFixedCellWidth(100); // Set a fixed cell width

            panel.add(userJList, BorderLayout.CENTER);

            if (mode==0){
                JButton kickOutButton = createKickOutButton();
                panel.add(kickOutButton, BorderLayout.SOUTH);
            }

            JScrollPane usersPane = new JScrollPane(panel); // Add scroll pane to the user list
            // create a titled border with the title "Users"
            Border titledBorder = BorderFactory.createTitledBorder("Users");
            usersPane.setBorder(titledBorder);  // add the border to the panel

            // Set the preferred width of usersPane
            usersPane.setPreferredSize(new Dimension(30, usersPane.getPreferredSize().height));

            return usersPane;
        } catch (RemoteException e){
            throw new RuntimeException(e);
        }
    }

    public JButton createKickOutButton(){
            JButton kickOutButton = new JButton("Kick Out");
            kickOutButton.setMaximumSize(new Dimension(100,30)); // Make button fill horizontal space
            kickOutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        String selectedUser = userJList.getSelectedValue();
                        if(selectedUser == userName) {
                            JOptionPane.showMessageDialog(null,
                                    "Sorry, you are not allow to remove yourself.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } else if (selectedUser != null && remoteServer != null) {
                            remoteServer.kickOutUser(selectedUser);
                            userJList.setListData(remoteServer.getUserList().toArray(new String[0]));
                        }
                    } catch (RemoteException ex){
                        throw new RuntimeException(ex);
                    }

                }
            });
            return kickOutButton;
    }

    public void beingKickedOff(){
        JOptionPane.showMessageDialog(null,
                "Sorry, you have been removed by the Organizer",
                "Denial", JOptionPane.ERROR_MESSAGE);
        System.gc();
        System.exit(0);
    }

    public void userNameWarning(){
        JOptionPane.showMessageDialog(null,
                "This username is already in use. Please try a different one.",
                "Warning", JOptionPane.ERROR_MESSAGE);
        System.gc();
        System.exit(0);
    }

    public void serverCloseWarning(){
        JOptionPane.showMessageDialog(null,
                "The host has terminated the whiteboard session.",
                "Session Ended", JOptionPane.INFORMATION_MESSAGE);
        System.gc();
        System.exit(0);
    }

    public boolean joinApproval(String userName){
        int response = JOptionPane.showConfirmDialog(null,
                "Do you want to grant access to \"" + userName + "\"?",
                "Access Control", JOptionPane.YES_NO_OPTION);
        return response == JOptionPane.YES_OPTION;
    }

    public void hostDenial(){
        JOptionPane.showMessageDialog(null,
                "You are denial from the shared whiteboard by the host.",
                "oDenial", JOptionPane.ERROR_MESSAGE);
        System.gc();
        System.exit(0);
    }

    public ArrayList<JSONObject> getMsgObjs() {
        return msgObjs;
    }

    public void setRemoteServer(IRemoteServer remoteServer) {
        this.remoteServer = remoteServer;
    }

    public DrawBoard getDrawBoard() {
        return drawBoard;
    }

    public void setUserJList() {
        try {
            if(remoteServer!=null){
                String selectedUser = userJList.getSelectedValue();
                userJList.setListData(remoteServer.getUserList().toArray(new String[0]));
                if (remoteServer.getUserList().contains(selectedUser)){
                    userJList.setSelectedValue(selectedUser, true);
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void serverClose(){
        try{
            if(remoteServer!=null){
                remoteServer.serverClosed();
            }
            System.gc();
            System.exit(0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void clientClose(){
        try {
            if (remoteServer != null) {
                remoteServer.clientClosed(userName);
            }
            System.gc();
            System.exit(0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        WhiteBoard whiteBoard = new WhiteBoard("Manager", 0);
    }

}
