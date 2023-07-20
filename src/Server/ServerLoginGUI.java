package Server;

        import javax.swing.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.rmi.AlreadyBoundException;
        import java.rmi.RemoteException;

/**
 * Server Login GUI
 *
 * COMP90015 Distributed Systems, Sem1, 2023
 * @author Xi Luo, 1302954, luoxl7@student.unimelb.edu.au
 * @version jdk18.0.2
 */
public class ServerLoginGUI extends JFrame implements ActionListener {

    // JFrame GUI-related variables
    private JTextField ipField;
    private JTextField portField;
    private JTextField nameField;
    private JButton loginButton;
    private static final String GUITitle = "Shared Whiteboard Server @Xi Luo, 1302954";
    private static final String developerInfo = "Developed by Xi Luo, 1302954";
    private static final String taskInfo = "COMP90015 Distributed Systems";
    private static final String copyrightInfo = "@Copyright: The University of Melbourne";
    private static final String timeInfo = "2023.5";

    public ServerLoginGUI(String address, int port, String userName) {

        super(GUITitle);

        // Create IP address label
        JLabel ipLabel = new JLabel("Server IP :");
        ipLabel.setBounds(20, 120, 100, 30);
        add(ipLabel);

        // Create IP address text field
        ipField = new JTextField(address);
        ipField.setBounds(80, 120, 200, 30);
        add(ipField);

        // Create port label
        JLabel portLabel = new JLabel("Port :");
        portLabel.setBounds(48, 150, 100, 30);
        add(portLabel);

        // Create text field for port
        portField = new JTextField(String.valueOf(port));
        portField.setBounds(80, 150, 200, 30);
        add(portField);

        // Create username label
        JLabel nameLabel = new JLabel("Username :");
        nameLabel.setBounds(11, 180, 100, 30);
        add(nameLabel);

        // Create text field for username
        nameField = new JTextField(userName);
        nameField.setBounds(80, 180, 200, 30);
        add(nameField);

        // Create login button
        loginButton = new JButton("Login");
        loginButton.setBounds(180, 210, 100, 30);
        loginButton.addActionListener(this);
        add(loginButton);

        // Create label for developer info
        JLabel developerInfoLabel = new JLabel(developerInfo);
        developerInfoLabel.setBounds(200, 300, 200, 20);
        add(developerInfoLabel);

        // Create label for task description
        JLabel taskInfoLabel = new JLabel(taskInfo);
        taskInfoLabel.setBounds(200, 320, 300, 20);
        add(taskInfoLabel);

        // Create label for copy right info
        JLabel copyrightLabel = new JLabel(copyrightInfo);
        copyrightLabel.setBounds(200, 340, 300, 20);
        add(copyrightLabel);

        // Create label of time info
        JLabel timeLabel = new JLabel(timeInfo);
        timeLabel.setBounds(400, 360, 300, 20);
        add(timeLabel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String userName = nameField.getText();
        String serverAddress = ipField.getText();
        try {
            int serverPort = Integer.parseInt(portField.getText());
            if (e.getSource() == loginButton) {
                try {
                    new Server(serverAddress,serverPort,userName);
                    dispose();
                    // Provide user feedback on success
                } catch (RemoteException | AlreadyBoundException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Please check for the IP and Port for the server.\n",
                            "Failed to create server", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null,
                    "Please enter a valid number for the server port.\n",
                    "Invalid Port Number", JOptionPane.ERROR_MESSAGE);
        }
    }


}