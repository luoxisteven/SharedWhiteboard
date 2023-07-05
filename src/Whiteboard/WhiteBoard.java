package Whiteboard;

import RMI.RemoteDrawBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class WhiteBoard extends JFrame {

    private String userName;
    private DrawBoard drawBoard;
    private JTextArea chatArea;
    private RemoteDrawBoard remoteDrawBoard;

    public WhiteBoard(String userName) {

        this.userName = userName;
        setTitle("Shared Whiteboard Server: " + userName);

        drawBoard = new DrawBoard();
        JPanel controlPanel = createControlPanel();
        JPanel chatPanel = createChatPanel();
        add(drawBoard);
        add(controlPanel, BorderLayout.WEST);
        add(chatPanel, BorderLayout.EAST);

        remoteDrawBoard = createRemoteCanvas();

        setJMenuBar(createMenuBar());
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private RemoteDrawBoard createRemoteCanvas(){
        try {
            RemoteDrawBoard remoteCanvas = new RemoteDrawBoard();
            remoteCanvas.setDrawBoard(this.drawBoard);
            return remoteCanvas;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(100, getHeight()));
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
                    JOptionPane.showMessageDialog(this, "Invalid font size.", "Error", JOptionPane.ERROR_MESSAGE);
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
                Color newColor = JColorChooser.showDialog(null, "Choose a color", drawBoard.getCurrentColor());
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
                String msg = messageField.getText();
                if (msg.contains("/") || msg.contains("\n")){
                    JOptionPane.showMessageDialog(null,
                            "You cannot send \"/\" or have a new line.",
                            "Chat-box Error", JOptionPane.ERROR_MESSAGE);
                } else if (!msg.isEmpty()) {
                    Date dNow = new Date( );
                    SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                    String chat = chatArea.getText()+userName+" ("+ft.format(dNow) +"): "+msg;
                    chatArea.setText(chat + "\n\n");
                    // replace this line with your actual server's broadcast method
                    // server.broadcast("chat/0/"+chat);
                    messageField.setText("");
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
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As");

        // add items to 'File' menu
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);

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
                drawBoard.clearWhiteboard(); // Clear the whiteboard
            }
        });

        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        controlMenu.add(newItem);
        controlMenu.add(closeItem);
        return controlMenu;
    }

    public DrawBoard getDrawBoard() {
        return drawBoard;
    }

    public static void main(String[] args){
        WhiteBoard whiteBoard = new WhiteBoard("Manager");
    }

}
