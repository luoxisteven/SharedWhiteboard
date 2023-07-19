package Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    private static final String developerInfo = "Developed by Xi Luo, 1302954";
    private static final String taskInfo = "COMP90015 Distributed Systems";
    private static final String copyrightInfo = "@Copyright: The University of Melbourne";
    private static final String timeInfo = "2023.5";
    private Server server;

    public ServerLoginGUI(String IP, int port, String userName, Server server) {

        super("Shared Whiteboard Server @Xi Luo, 1302954");
        this.server = server;

        // Create IP address label
        JLabel ipLabel = new JLabel("Server IP :");
        ipLabel.setBounds(20, 120, 100, 30);
        add(ipLabel);

        // Create IP address text field
        ipField = new JTextField(IP);
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

        setSize(500, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Login with the IP address and port of the server
        String ip = ipField.getText();
        int port = Integer.parseInt(portField.getText());
        if (e.getSource() == loginButton) {
            {

            }
        }
    }
}