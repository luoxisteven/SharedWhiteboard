package Server;

public class CreateWhiteBoard {
    public static final String DEFAULT_IP = "localhost";
    public static final int DEFAULT_PORT = 4444;
    public static final String DEFAULT_USERNAME = "Manager";
    private String serverAddress;
    private int serverPort;
    private String userName;

    private CreateWhiteBoard(){
        serverAddress = DEFAULT_IP;
        serverPort = DEFAULT_PORT;
        userName = DEFAULT_USERNAME;
    }

    private void parseArgs(String args[]) {
        try {
            serverAddress = args[0];
            serverPort = Integer.parseInt(args[1]);
            userName = args[2];
        } catch (Exception e) {
            System.out.println("Warning: Please check for the params.");
            System.out.println("java -jar CreateWhiteBoard.jar <IP> <Port> <Username>");
        }
    }

    private void createSever(){
        Server server = new Server(serverAddress, serverPort, userName);
    }

    public static void main(String args[]) {
        CreateWhiteBoard createWhiteBoard = new CreateWhiteBoard();
        createWhiteBoard.parseArgs(args);
        createWhiteBoard.createSever();
    }

}
