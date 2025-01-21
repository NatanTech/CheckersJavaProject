package ver8;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Client דוגמה ללקוח פשוט .
 * ---------------------------------------------------------------------------
 * by Amitay Agay (beitarsenal7@gmail.com) 20/10/2021
 */
public class Client
{
    // constatns
    private static final String teacherIP = "192.168.21.239";
    public static final String CLIENT_WIN_TITLE = "checkers Client";
    private static final Dimension CLIENT_WIN_SIZE = new Dimension(700, 400);
    private static final Color CLIENT_LOG_BGCOLOR = Color.LIGHT_GRAY;
    private static final Color CLIENT_LOG_FGCOLOR = Color.BLACK;
    private static final Font CLIENT_LOG_FONT = new Font("Consolas", Font.PLAIN, 20); // Font.MONOSPACED

    // for Client
    private boolean clientSetupOK, clientRunOK;
    private int serverPort;
    private String serverIP;
    private Socket socketToServer;
    private Socket socketToCommands;
    private AppSocket clientSocket;
    private View view;
    private String userName;

    /**
     * Constractor for Client.
     */
    public Client()
    {
        createClientGUI();
        setupClient();
    }

    /**
     * Start run of client
     *
     * @throws IOException if can't read or write message
     */
    public void runClient() throws IOException
    {
        if(clientSetupOK)
        {
            //processCmdFromServer();
            clientRunOK = true;

            // loop while client running OK
            while(clientRunOK)
            {
                // wait for a message from server or null if socket closed
                Message msg = clientSocket.readMessage();
                System.out.println("in processMsg: " + msg);

                if(msg.getSubject().equals(Constants.CANCEL_EXIT))
                {
                    System.out.println("finish listening for messages. user " + userName);
                    clientRunOK = false;
                }

                else
                {
                    proccessMsgFromServer(msg);
                }
            }
            System.out.println("break from loop");
        }
        if(!userName.endsWith("disconnected"))
        {
            closeClient();
        }

        System.out.println("**** runClient() finished! ****");
    }

    /**
     * send to server that SAVE & EXIT button pressed and stop the run of client
     */
    public void saveAndExitPressed()
    {
        Message msg = new Message(Constants.SAVE_AND_EXIT);
        msg.setLogin(new LoginDetails(userName, null, false));
        clientSocket.writeCommand(msg);
        stopClient();
    }

    // read the message and does things according to
    // the message sent
    private void proccessMsgFromServer(Message msg) throws IOException
    {
        String subject = msg.getSubject();

        if(subject.equals(Constants.LOGIN))
        {
            login("");
        }

        if(subject.equals(Constants.SERVER_CLOSED))
        {
            view.serverClosed();
            this.stopClient();
        }

        if(subject.equals(Constants.GAME_OVER))
        {
            GameInfo info = msg.getGameInfo().get(0);
            String gameOverMsg = info.getGeneralStats();
            view.gameOver(gameOverMsg);
            countDown(gameOverMsg);
        }

        if(subject.equals(Constants.WAIT_TURN))
        {
            view.setLabelMsg("Wait turn...");
            view.setBoardButtonsEnabled(false);
            //view.setDisabledIcons();
        }

        if(subject.equals(Constants.INIT_GAME))
        {
            msg.getBoard().printBoard();
            view.setup(msg.getBoard());
            view.initGame();
            //view.setDisabledIcons();
        }

        if(subject.equals(Constants.UNLOCK_SAVE_GAME_BUTTON))
        {
            view.unlockSaveGameButton();
        }

        if(subject.equals(Constants.YOU_WHITE))
        {
            String title = view.getWin().getTitle();
            String newTitle = title + " (white)";
            view.getWin().setTitle(newTitle);
        }

        if(subject.equals(Constants.YOU_BLACK))
        {
            String title = view.getWin().getTitle();
            String newTitle = title + " (black)";
            view.getWin().setTitle(newTitle);
        }

        if(subject.equals(Constants.YOUR_TURN))
        {
            view.setLabelMsg("Play turn!");
            view.setBoardButtonsEnabled(false);
            ArrayList<Location> locsToUnlock = msg.getLocs();
            //view.setDisabledIcons();
            view.setEnabledList(locsToUnlock, true);
        }

        if(subject.equals(Constants.OPTIONS_FOR_MOVE))
        {
            ArrayList<Move> posMoves = msg.getPosMoves();
            view.paintAllpossibleMoves(posMoves);
        }

        if(subject.equals(Constants.UPDATE_VIEW))
        {
            view.cleanAllBorders();
            msg.getBoard().printBoard();
            Board board = msg.getBoard();
            view.updateBoard(board);
            view.deleteAllPaints();
            view.makeBorders(msg.getMove());
            //view.setDisabledPiece(msg.getMove());
            //view.setDisabledIcons();
        }

        if(subject.equals(Constants.DELETE_DRAWINGS))
        {
            view.deleteAllPaints();
        }

        if(subject.equals(Constants.YOUR_OPPONENT))
        {
            String title = view.getWin().getTitle();
            view.getWin().setTitle(title + msg.getLogin().getUserName());
        }

        if(subject.equals(Constants.ALL_GAMES_PLAYED))
        {
            view.printAllGamesPlayed(msg);
        }

        if(subject.equals(Constants.TOP5))
        {
            view.printTop5(msg);
        }

        if(subject.equals(Constants.GAMES_PLAYED_LAST_WEEK))
        {
            view.printGamesLastWeek(msg);
        }

        if(subject.equals(Constants.GAMES_PLAYED_VS_AI))
        {
            view.printGamesVsAI(msg);
        }

        if(subject.equals(Constants.YOUR_OPPONENT_EXIT))
        {
            view.yourOpponentExit();
        }

        if(subject.equals(Constants.CHANGE_PASSWORD))
        {
            view.passwordChanged();
        }

        if(subject.equals(Constants.WRONG_PASSWORD))
        {
            view.changePassword("Wrong password. please enter your password again");
        }

        if(subject.equals(Constants.DELETE_SAVED_GAMES))
        {
            view.deleteSavedGamesSuccess();
        }

        if(subject.equals(Constants.DELETE_SAVED_GAMES_FAILED))
        {
            view.deleteSavedGamesFailed();
        }

//        if(subject.equals(Constants.DELETE_USER))
//        {
//            view.deleteUserSuccess();
//        }
//
//        if(subject.equals(Constants.DELETE_USER_FAILED))
//        {
//            view.deleteUserFailed();
//        }
        if(subject.equals(Constants.SELECT_UNFINISHED_BOARD))
        {
            ArrayList<String> allBoards = msg.getBoards();
            selectUnfinishedBoard(allBoards);
        }

        if(subject.equals(Constants.UNLOCK_REGISTER_OPTIONS))
        {
            view.setEnebledRegisterOptions(true);
        }

    }

    /**
     * sends message to server with request of statistic of all games played
     */
    public void AllGamesPlayed()
    {
        clientSocket.writeCommand(new Message(Constants.ALL_GAMES_PLAYED));
    }

    /**
     * sends message to server with request of statistic of all games played
     * last week
     */
    public void gamesPlayedLastWeek()
    {
        clientSocket.writeCommand(new Message(Constants.GAMES_PLAYED_LAST_WEEK));
    }

    /**
     * sends message to server with request of statistic of all games played VS
     * AI
     */
    public void gamesPlayedVsAI()
    {
        clientSocket.writeCommand(new Message(Constants.GAMES_PLAYED_VS_AI));
    }

    /**
     * sends message to server with request of statistic of top 5 of the game
     */
    public void Top5()
    {
        clientSocket.writeCommand(new Message(Constants.TOP5));
    }

    /**
     * sends message to server with request to delete this user
     */
    public void deleteUser()
    {
        clientSocket.writeCommand(new Message(Constants.DELETE_USER));
    }

    /**
     * sends message to server with request to delete all unfinished games of
     * this user
     */
    public void deleteSavedGames()
    {
        clientSocket.writeCommand(new Message(Constants.DELETE_SAVED_GAMES));
    }

    /**
     * sends message to server that this user want to forfeit
     */
    public void Forfeit()
    {
        clientSocket.writeCommand(new Message(Constants.FORFEIT));
        clientRunOK = false;
        userName = userName + " disconnected";
        stopClient();
    }

    /**
     * sends message to server with change password details
     *
     * @param currentPw
     * @param newPw
     */
    public void changePassword(String currentPw, String newPw)
    {
        String[] changePw =
        {
            userName, currentPw, newPw
        };

        Message msg = new Message(Constants.CHANGE_PASSWORD);
        msg.setChangePw(changePw);
        clientSocket.writeCommand(msg);
    }

    /**
     * sends to server the unfinished board selected to continue
     *
     * @param dateBoards
     */
    public void selectUnfinishedBoard(ArrayList<String> dateBoards)
    {
        String boardSelected = view.selectUnfinishedBoard(dateBoards);
        Message msg = new Message(Constants.UNFINISHED_BOARD_SELECTED);
        msg.setDateBoard(boardSelected);
        System.out.println("board in msg: " + msg.getDateBoard());
        System.out.println("unfinished board selected! message sent:\n" + msg);
        clientSocket.writeCommand(msg);
    }

    // make setup to client
    private void setupClient()
    {
        try
        {
            // set the Server Adress (DEFAULT IP&PORT)
            serverPort = Constants.SERVER_DEFAULT_PORT;
            serverIP = InetAddress.getLocalHost().getHostAddress(); // IP of this computer

            // get Server Address from user
            String serverAddress = JOptionPane.showInputDialog(view.getWin(), "Enter SERVER Address [IP : PORT]", serverIP + " : " + serverPort);

            // check if Cancel button was pressed
            if(serverAddress == null)
            {
                closeClient();
            }

            // get server IP & PORT from input string
            serverAddress = serverAddress.replace(" ", ""); // remove all spaces
            serverIP = serverAddress.substring(0, serverAddress.indexOf(":"));
            serverPort = Integer.parseInt(serverAddress.substring(serverAddress.indexOf(":") + 1));

            // Setup connection to SERVER Address
            socketToServer = new Socket(serverIP, serverPort);
            socketToCommands = new Socket(serverIP, serverPort);
            clientSocket = new AppSocket(socketToServer, socketToCommands);
            // wait for the server to send welcom message
//            String welcomeMsg = appSocket.readMessage();
//            String clientId = welcomeMsg.substring(welcomeMsg.indexOf(" ")+1);
//            chatter.setId(clientId);

            clientSetupOK = true;
        }
        catch(Exception exp)
        {
            clientSetupOK = false;
            String serverAddress = serverIP + ":" + serverPort;
            //log("Client can't connect to Server(" + serverAddress + ")", exp, view.getWin());
        }
    }

    // does cont down when game over to new
    // game that will start
    private void countDown(String subject)
    {
        for(int i = 7; i > 0; i--)
        {
            view.setLabelMsg(subject + " new game will start in " + i + "s");
            sleepInSeconds(1);
        }
        clientSocket.writeMessage(new Message(Constants.COUNT_DOWN_FINISHED));
    }

    // sends login details that client entered
    // in login dialog
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

        if(subject.startsWith("Welcome"))
        {
            view.setBoardButtonsEnabled(false);
            view.setLabelMsg("wait for partner...");
            String id = subject.substring(8);
            view.getWin().setTitle("checkers/ " + id);
            userName = subject.substring(subject.indexOf(' ') + 1);
            if(!userName.startsWith("GUEST"))
            {
                view.setEnebledRegisterOptions(true);
            }
        }
        else
        {
            login(subject);
        }
    }

    /**
     * sends to server the location selected to make move
     *
     * @param loc
     */
    public void boardButtonPressed(Location loc)
    {
        Message msg = new Message("click");
        msg.setLoc(loc);
        clientSocket.writeMessage(msg);
    }

    /**
     * shows dialog that ask if you want close client. if yes, close the client
     * and stop his running
     */
    public void exitClient()
    {
        int option = JOptionPane.showConfirmDialog(view.getWin(), "Do you really want to Exit?", "Client Exit", JOptionPane.YES_NO_OPTION);

        if(option == JOptionPane.YES_OPTION)
        {
            System.out.println("***** stopClient() *******");
            stopClient();
        }
    }

    /**
     * stops the running of the client
     */
    public void stopClient()
    {
        clientRunOK = false;
        userName = userName + " disconnected";
        System.out.println("in stopClient!");
        clientSocket.writeCommand(new Message(Constants.CANCEL_EXIT));
        clientSocket.close(); // will throw 'SocketException' and unblock I/O. see close() API
        closeClient();
    }

    // closes the client and send to server that
    // he closed
    private void closeClient()
    {
        System.out.println("in closeClient!");
        if(clientSocket != null && clientSocket.isConnected())
        {
            String msg = "The connection with the Server is LOST!\nClient will be close...";
            JOptionPane.showMessageDialog(view.getWin(), msg, "Checkers Client Error", JOptionPane.ERROR_MESSAGE);
            stopClient();
        }

        clientSocket.writeCommand(new Message(Constants.CANCEL_EXIT));
        //log("Client Closed!");

        // close GUI
        view.getWin().dispose();
        System.out.println("finish closeClient!");

        // close client
        //System.exit(0);
    }

    // create the client GUI
    private void createClientGUI()
    {
        view = new View(this);
    }

    // Thread.sleep in seconds (instead of milis)
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

//    private void opponentExit()
//    {
//        int option = JOptionPane.showConfirmDialog(view.getWin(), "Your opponent disconnected- you win technically!", "opponent disconnected", JOptionPane.OK_OPTION);
//
//        if(option == JOptionPane.OK_OPTION)
//        {
//            view.getWin().dispose();
//        }
//    }
    // main
    public static void main(String[] args) throws IOException
    {
        Client client = new Client();
        client.runClient();
        //System.out.println("view size: " + view.getWin().getSize());
        System.out.println("**** Client main() finished! ****");
    }
}
