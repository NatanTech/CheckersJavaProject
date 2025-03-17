package ver6;

import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Client לקוח רזה.
 * by Natan tzuberi (tzuberinat@gmail.com) 10/10/2022
 */
public class Client
{
    // constatns
    private static final String DEFAULT_IP = "192.168.21.239";
    public static final String CLIENT_WIN_TITLE = "checkers Client";
    private static final Dimension CLIENT_WIN_SIZE = new Dimension(700, 400);

    // for Client
    private int serverPort;
    private String serverIP;
    private Socket socketToServer;
    private Socket socketToCommands;
    private AppSocket clientSocket;
    private boolean setupCheck, keppRuning;
    private View view;
    private int userID;

 
    public Client()
    {
        createClientGUI();
        setupClient();
    }
    // main
    public static void main(String[] args) throws IOException
    {
        Client client = new Client();

        client.runClient();
        System.out.println("Client main() finished!");
    }

    public void runClient() throws IOException
    {
        if(setupCheck)
        {
            keppRuning = true;

            while(keppRuning)
            {
                // wait for a message from server or null if socket closed
                Message msg = clientSocket.readMessage();
                System.out.println("in processMsg: " + msg);

                if(msg.getSubject().equals(Message.CANCEL_EXIT))
                {
                    System.out.println("finish listening for messages. user " + userID);
                    keppRuning = false;
                }

                else
                {
                    proccessMsgFromServer(msg);
                }
            }
            System.out.println("break from loop");
        }
        if(userID==0)
        {
            closeClient();
        }

        System.out.println(" runClient() finished! ");
    }

    // read the message and does things according to
    // the message sent
    private void proccessMsgFromServer(Message msg) throws IOException
    {
        String subject = msg.getSubject();

        if(subject.equals(Message.ENTER))
        {
            login("welcom to checkers game!");
        }

        if(subject.equals(Message.SERVER_CLOSED))
        {
            view.serverClosed();
            this.stopClient();
        }

        if(subject.equals(Message.GAME_OVER))
        {
            String gameOverMsg=msg.getGameOverMsg();
            view.gameOver(gameOverMsg);
            countDown(gameOverMsg);
        }

        if(subject.equals(Message.WAIT_TURN))
        {
            view.setLabelMsg("Wait turn...");
            view.setBoardButtonsEnabled(false);
        }

        if(subject.equals(Message.INIT_GAME))
        {
            msg.getBoard().printBoard();
            view.setup(msg.getBoard());
            view.initGame();
        }

        if(subject.equals(Message.YOU_WHITE))
        {
            String title = view.getWin().getTitle();
            String newTitle = title + " your color is WHITE(or the hollow circle)****";
            view.getWin().setTitle(newTitle);
        }

        if(subject.equals(Message.YOU_BLACK))
        {
            String title = view.getWin().getTitle();
            String newTitle = title + " your color is BLACKE(or the full circle****";
            view.getWin().setTitle(newTitle);
        }

        if(subject.equals(Message.YOUR_TURN))
        {
            view.setLabelMsg("Play turn!");
            view.setBoardButtonsEnabled(false);
            ArrayList<Location> locsToUnlock = msg.getPosLocs();
            view.setEnabledList(locsToUnlock, true);
        }

        if(subject.equals(Message.OPTIONS_FOR_MOVE))
        {
            ArrayList<Move> posMoves = msg.getPosMoves();
            view.paintAllpossibleMoves(posMoves);
        }

        if(subject.equals(Message.UPDATE_VIEW))
        {
            view.cleanAllBorders();
            msg.getBoard().printBoard();
            Board board = msg.getBoard();
            view.updateBoard(board);
            view.deleteAllPaints();
            view.makeBorders(msg.getMove());
        }

        if(subject.equals(Message.DELETE_DRAWINGS))
        {
            view.deleteAllPaints();
//            view.clearLabel2Msg();
        }

        if(subject.equals(Message.YOUR_OPPONENT))
        {
            String title = view.getWin().getTitle();
            view.getWin().setTitle( "(ID-->"+userID+") "+title );
        }

        if(subject.equals(Message.YOUR_OPPONENT_EXIT))
        {
            view.yourOpponentExit();
        }

    }

    public void Quit()
    {
        clientSocket.writeCommand(new Message(Message.QUIT));
        keppRuning = false;
        stopClient();
    }

    // make setup to client
    private void setupClient()
    {
        try
        {
            // set the Server Adress (DEFAULT IP&PORT)
            serverPort = Message.SERVER_DEFAULT_PORT;
            serverIP = InetAddress.getLocalHost().getHostAddress(); // IP from this computer

            // get Server Address from user
            String serverAddress = JOptionPane.showInputDialog(view.getWin(), "Enter SERVER Address [IP : PORT]", serverIP + " : " + serverPort);

            if(serverAddress == null)
            {
                closeClient();
            }

            serverAddress = serverAddress.replace(" ", ""); // remove all spaces
            serverIP = serverAddress.substring(0, serverAddress.indexOf(":"));
            serverPort = Integer.parseInt(serverAddress.substring(serverAddress.indexOf(":") + 1));

            socketToServer = new Socket(serverIP, serverPort);
            socketToCommands = new Socket(serverIP, serverPort);
            clientSocket = new AppSocket(socketToServer, socketToCommands);
            setupCheck = true;
        }
        catch(Exception exp)
        {
            setupCheck = false;
            String serverAddress = serverIP + ":" + serverPort;
        }
    }

    // count down from 10 when the game is ended
    private void countDown(String subject)
    {
        for(int i = 7; i > 0; i--)
        {
            view.setLabelMsg(subject + " new game starting in " + i + "s");
            sleepInSeconds(1);
        }
        clientSocket.writeMessage(new Message(Message.COUNT_DOWN_FINISHED));
    }

    private void login(String errMsg) throws IOException
    {
        Message msg = view.loginDialog(errMsg);
        String subject = msg.getSubject();
        if(subject.equalsIgnoreCase("Cancel & Exit"))
        {
            return;
        }
        clientSocket.writeMessage(msg);
        msg = clientSocket.readMessage();
        System.out.println("subject: " + msg.getSubject());
        subject = msg.getSubject();
        userID=msg.getLogin().getID_player();
    }

    public void boardButtonPressed(Location loc)
    {
        Message msg = new Message("click");
        msg.setLocOfPress(loc);
        clientSocket.writeMessage(msg);
    }

    public boolean exitClient()
    {
        int option = JOptionPane.showConfirmDialog(view.getWin(), "Are you sure?", "Client Exit", JOptionPane.YES_NO_OPTION);

        if(option == JOptionPane.YES_OPTION)
        {
            System.out.println(" stopClient() ");
            stopClient();
            return true;
        }
        return false;
    }

    public void stopClient()
    {
        keppRuning = false;
        //userName = userName + " disconnected";
        System.out.println("in stopClient!");
        if(clientSocket!=null)
        {
        clientSocket.writeCommand(new Message(Message.CANCEL_EXIT));
        clientSocket.close(); // will throw 'SocketException' and unblock I/O. see close() API
        }
        
        closeClient();
    }

    private void closeClient()
    {
        System.out.println("in closeClient!");
        if(clientSocket != null && clientSocket.isConnected())
        {
            String msg = "The connection with the Server is LOST!\nClient will be close...";
            JOptionPane.showMessageDialog(view.getWin(), msg, "Checkers Client Error", JOptionPane.ERROR_MESSAGE);
            stopClient();
            clientSocket.writeCommand(new Message(Message.CANCEL_EXIT));
        }

        view.close();
        System.out.println("finish closeClient!");

    }

    private void sleepInSeconds(int seconds)
    {
        try
        {
            Thread.sleep(seconds * 1000);
        }
        catch(InterruptedException ex)
        {
            System.out.println("can't make sleeping!");
        }
    }

        // create the client GUI
    private void createClientGUI()
    {
        view = new View(this);
    }
}
