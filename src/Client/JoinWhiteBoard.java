package Client;

import Whiteboard.WhiteBoard;

public class JoinWhiteBoard {
    public static final String DEFAULT_IP = "localhost";
    public static final int DEFAULT_PORT = 4444;
    public static final String DEFAULT_USERNAME = "Client";
    private String serverAddress;
    private int serverPort;
    private String userName;

    public JoinWhiteBoard(){
        serverAddress = DEFAULT_IP;
        serverPort = DEFAULT_PORT;
        userName = DEFAULT_USERNAME;
    }

    public void parseArgs(String args[]) {
        try {
            serverAddress = args[0];
            serverPort = Integer.parseInt(args[1]);
            userName = args[2];
        } catch (Exception e) {
            System.out.println("Warning: Please check for the params.");
            System.out.println("java -jar JoinWhiteBoard.jar <IP> <Port> <Username>");
        }
    }

    public void init(){
        WhiteBoard whiteBoard = new WhiteBoard(userName);
        ClientInternet clientInternet = new ClientInternet();

    }

    public static void main(String[] args){
        JoinWhiteBoard joinWhiteBoard = new JoinWhiteBoard();
        joinWhiteBoard.parseArgs(args);
        joinWhiteBoard.init();
    }

}
