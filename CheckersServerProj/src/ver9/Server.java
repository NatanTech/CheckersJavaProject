package ver9;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Server -דוגמה לשרת משחק רשת מרובה משחקים .
 * ---------------------------------------------------------------------------
 * by Amitay Agay (beitarsenal7@gmail.com) 10/11/2021
 */
public class Server
{
    // constants
    public static final String SERVER_WIN_TITLE = "Checkers Server";
    private static final Dimension SERVER_WIN_SIZE = new Dimension(580, 400);

    private static final Color SERVER_LOG_BGCOLOR = Color.BLACK;
    private static final Color SERVER_LOG_FGCOLOR = Color.GREEN;
    public static final Font SERVER_LOG_FONT = new Font("Consolas", Font.PLAIN, 16); // Font.MONOSPACED

    // תכונות
    private JFrame frmWin;
    private JTextArea areaLog;
    private String serverIP;
    private int serverPort;
    private boolean serverSetupOK, serverRunOK;
    private ServerSocket serverSocket;
    private int autoClientID;
    private ArrayList<Player> onlinePlayers;
    private ArrayList<Game> games;
    private Player waitingPlayer;

    /**
     * Constructor for Checkers server.
     */
    public Server()
    {
        autoClientID = 0;
        waitingPlayer = null;
        onlinePlayers = new ArrayList();
        games = new ArrayList();
        createServerGUI();
        setupServer();
    }

    // Run the server - wait for clients
    // to connect & handle them
    public void runServer()
    {
        if(serverSetupOK)
        {
            String serverAddress = "(" + serverIP + ":" + serverPort + ")";
            log("SERVER" + serverAddress + " Setup & Running!\n");
            frmWin.setTitle(SERVER_WIN_TITLE + " " + serverAddress);

            serverRunOK = true;

            // loop while server running OK
            while(serverRunOK)
            {
                AppSocket socketToClient = waitForClient();  // (blocking)

                if(socketToClient == null)
                {
                    serverRunOK = false;
                }
                else
                {
                    handleClient(socketToClient);
                }
            }
        }

        closeServer("runServer() finished!");

        System.out.println("**** runServer() finished! ****");
    }

    // Wait for client to connect.
    // Return the client socket or null if serverSocket closed.
    private AppSocket waitForClient()
    {

        Socket socketToClientMsg, socketToClientCmd;
        AppSocket appSocket;
        System.out.println("Waiting for client ...");

        try
        {
            // Wait for a new client to connect. return client socket.
            socketToClientMsg = serverSocket.accept(); // blocking method
            socketToClientCmd = serverSocket.accept(); // blocking method
            appSocket = new AppSocket(socketToClientMsg, socketToClientCmd);

        }
        catch(IOException exp)
        {
            socketToClientMsg = null;
            appSocket = null;
        }

        return appSocket;
    }

    // handle client in a separate thread
    private void handleClient(AppSocket socketToClient)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                PlayerNet player = login(socketToClient);
                System.out.println(player);
                if(player != null)
                {
                    handleCmdSocket(player);
                    synchronized(onlinePlayers)
                    {
                        onlinePlayers.add(player);
                    }

                    log("User " + player.getId() + " connected!");
                    if(player.isWithAI())
                    {
                        //log("with AI selected");
                        if(!player.getId().startsWith("GUEST#"))
                        {
                            sendUnfinishedGamesToChoose(player);
                        }

                        else
                        {
                            PlayerAI AI = new PlayerAI();
                            createNewGameAndRun(player, AI);
                        }
                    }

                    else
                    {
                        if(waitingPlayer != null)
                        {
                            createNewGameAndRun(waitingPlayer, player);
                            waitingPlayer = null;
                        }
                        else
                        {
                            waitingPlayer = player;
                        }
                    }
                }
            }

        }).start();
    }

    // Enter the 2 players to new game and starts
    // the running of the game
    private void createNewGameAndRun(Player player1, Player player2)
    {
        Game game = new Game(player1, player2);
        synchronized(games)
        {
            games.add(game);
        }

        log("Game session started: " + player1.getId() + " VS " + player2.getId());
        game.runGame(false);
    }

    // if player has unfinished games the func sends them
    // to him to choose one of them (or new game)
    private void sendUnfinishedGamesToChoose(Player player)
    {
        try
        {
            ArrayList<String> allBoards = DB.selectUnfinishedBoards(player.getId());
            //log("array boards size: " + allBoards.size());
            if(allBoards.size() > 1)
            {
                //log("load boards to player " + player.getId());
                player.selectUnfinishedGames(allBoards);
            }

            else
            {
                PlayerAI AI = new PlayerAI();
                createNewGameAndRun(player, AI);
            }

        }
        catch(SQLException ex)
        {
            System.out.println("can't run the SQL query!");
        }
    }

    /**
     * Gets new playerNet that made login successfully
     *
     * @param clientSocket AppSocket to client (to send and receive messages)
     * @return PlayerNet from the login of client. null if client choosed cancel
     */
    public PlayerNet login(AppSocket clientSocket)
    {
        clientSocket.writeMessage(new Message(Constants.LOGIN));
        while(true)
        {
            String clientID = null;

            Message msg = clientSocket.readMessage();
            String answer = msg.getSubject();
            LoginDetails login = msg.getLogin();
            System.out.println("msg: " + msg);

            if(answer.startsWith(Constants.CANCEL_EXIT))
            {
                clientSocket.close();
                //log("User disconnected!");
                return null;
            }

            if(answer.startsWith(Constants.LOGIN_AS_GUEST))
            {
                clientID = "GUEST#" + autoClientID++;
                clientSocket.writeMessage(new Message("Welcome " + clientID));
                System.out.println("ID: " + clientID + ", AI: " + login.isWithAI());
            }

            if(answer.startsWith(Constants.LOGIN_AS_USER))
            {
                String un = login.getUserName();
                String pw = login.getPassword();
                boolean withAI = login.isWithAI();
                System.out.println("Login as user: un = " + un + ", pw = " + pw
                        + ", AI = " + withAI);
                //client.writeMessage(new Message("welcome " + un));

                //check un & pw in DB
                try
                {
                    if(playerIsOnline(un))
                    {
                        clientSocket.writeMessage(new Message(Constants.ALREADY_ONLINE_MSG));
                    }

                    if(DB.isUserExists(un, pw))
                    {
                        clientID = un;
                        clientSocket.writeMessage(new Message("Welcome " + clientID));
                    }

                    else
                    {
                        System.out.println("User " + un + " invalid!");
                        clientSocket.writeMessage(new Message(Constants.LOGIN_FAILED_MSG));
                    }

                }
                catch(SQLException exp)
                {
                    System.out.println("please try again!");
                }

            }

            if(clientID != null)
            {
                System.out.println("clientSocket = " + clientSocket + ", with AI = " + login.isWithAI() + ", clientID = " + clientID);
                PlayerNet player = new PlayerNet(clientSocket, login.isWithAI(), clientID);
                return player;
            }
        }
    }

    // handle requests that comes from cmd socket
    private void handleCmdSocket(PlayerNet player)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                AppSocket cmd = player.getSocketToClient();
                String un = player.getId();
                while(true)
                {
                    Message msg = cmd.readCommand();
                    String subject = msg.getSubject();
                    System.out.println("handleCmd " + un + ": " + msg);

                    if(subject.equals(Constants.CANCEL_EXIT))
                    {
                        System.out.println("player " + un + " do cancel exit!");
//                        if(un.endsWith("disconnected"))
//                        {
//                            String newUn = un.substring(0, un.indexOf(' '));
//                            un = newUn;
//                        }

                        log("User " + un + " disconnected!");

                        if(waitingPlayer != null)
                        {
                            if(waitingPlayer.getId().equals(un))
                            {
                                waitingPlayer = null;
                            }
                        }

                        else
                        {
                            playerDisconnected(player);
                            sendYourOpponentExit(player);
                        }

                        synchronized(onlinePlayers)
                        {
                            onlinePlayers.remove(player);
                        }

                        cmd.close();
                        System.out.println("break from cmd thread of " + player.getId());
                        break;
                    }

                    else
                    {
                        try
                        {
                            processCmdFromClient(msg, player);
                        }
                        catch(SQLException ex)
                        {
                            System.out.println("can't analyze cmd message...");
                        }
                    }
                }

            }
        }).start();
    }

    // Analyzes the messages that came to server
    // help func for handleCmdSocket function
    private void processCmdFromClient(Message msg, PlayerNet player) throws SQLException
    {
        String subject = msg.getSubject();
        AppSocket cmd = player.getSocketToClient();
        String un = player.getId();

        if(subject.equals(Constants.SAVE_AND_EXIT))
        {
            Board board = player.getModel().getLogicBoard();
            String strBoard = board.boardToStr();
            String playerTurnId = getPlayerTurnByPlayer(un);
            DB.saveBoardToDB(un, strBoard, playerTurnId);
            synchronized(onlinePlayers)
            {
                onlinePlayers.remove(player);
            }

            playerSavedGame(player);
            log("Player " + un + " saved game and exit!");
        }

        if(subject.equals(Constants.ALL_GAMES_PLAYED))
        {
            ArrayList<GameInfo> allGames = DB.allGamesPlayed(un);
            Message allGamesMsg = new Message(Constants.ALL_GAMES_PLAYED);
            allGamesMsg.setGameInfo(allGames);
            cmd.writeMessage(allGamesMsg);
        }

        if(subject.equals(Constants.GAMES_PLAYED_LAST_WEEK))
        {
            ArrayList<GameInfo> gamesLastWeek = DB.gamesPlayedLastWeek(un);
            Message gameMsg = new Message(Constants.GAMES_PLAYED_LAST_WEEK);
            gameMsg.setGameInfo(gamesLastWeek);
            cmd.writeMessage(gameMsg);
        }

        if(subject.equals(Constants.GAMES_PLAYED_VS_AI))
        {
            ArrayList<GameInfo> gamesVsAI = DB.gamesVsAI(un);
            Message gameMsg = new Message(Constants.GAMES_PLAYED_VS_AI);
            gameMsg.setGameInfo(gamesVsAI);
            cmd.writeMessage(gameMsg);
        }

        if(subject.equals(Constants.TOP5))
        {
            System.out.println("in top5 server!");
            ArrayList<GameInfo> top5 = DB.top5();
            Message top5Msg = new Message(Constants.TOP5);
            top5Msg.setGameInfo(top5);
            System.out.println(top5Msg);
            cmd.writeMessage(top5Msg);
        }

        if(subject.equals(Constants.DELETE_SAVED_GAMES))
        {
            String delete = DB.deleteSavedGames(un);
            if(delete.equals("delete complete!"))
            {
                cmd.writeMessage(new Message(Constants.DELETE_SAVED_GAMES));
            }
            else
            {
                cmd.writeMessage(new Message(Constants.DELETE_SAVED_GAMES_FAILED));
            }
        }

        if(subject.equals(Constants.FORFEIT))
        {
            makeForfeit(un);
        }

        if(subject.equals(Constants.CHANGE_PASSWORD))
        {
            String[] pws = msg.getChangePw();
            String user = pws[0], currentPw = pws[1], newPw = pws[2];
            if(DB.isUserExists(user, currentPw))
            {
                log("Player " + user + " change password from " + currentPw + " to " + newPw);
                DB.changePassword(user, newPw);
                cmd.writeMessage(new Message(Constants.CHANGE_PASSWORD));
            }

            else
            {
                cmd.writeMessage(new Message(Constants.WRONG_PASSWORD));
            }
        }

        if(subject.equals(Constants.UNFINISHED_BOARD_SELECTED))
        {
            String dateBoard = msg.getDateBoard();
            if(dateBoard.equals("New game"))
            {
                PlayerAI AI = new PlayerAI();
                createNewGameAndRun(player, AI);
            }

            else
            {
                String playerTurn = DB.getPlayerTurn(dateBoard);
                String board = DB.getBoard(un, dateBoard);
                Board modelBoard = new Board();
                modelBoard.strToBoard(board);
                Model model = new Model();
                model.setModel(modelBoard);
                System.out.println("date selected: " + dateBoard);
                System.out.println("playerTurn: " + playerTurn);
                System.out.println("board of date: " + board);
                System.out.println("modelBoard: ");
                modelBoard.printBoard();
                DB.deleteUnfinishedGameContinued(dateBoard);
                continueUnfinishedGameAndRun(player, model, playerTurn);
            }
        }
    }

    /**
     * Continue game that was saved on DB and run
     *
     * @param player
     * @param model
     * @param playerTurn
     */
    public void continueUnfinishedGameAndRun(Player player, Model model, String playerTurn)
    {
        System.out.println("continue game");
        PlayerAI AI = new PlayerAI();
        Player currentPlayerTurn = null;
        if(playerTurn.equals("AI"))
        {
            currentPlayerTurn = AI;
        }
        else
        {
            currentPlayerTurn = player;
        }
        Game game = new Game(player, AI, model, currentPlayerTurn);
        synchronized(games)
        {
            games.add(game);
        }

        game.runGame(true);
    }

    // search the game that specific player
    // decided to save the game and remove him
    // from games list
    private void playerSavedGame(Player player)
    {
        for(Game game : games)
        {
            Player p1 = game.getP1()/*, p2 = game.getP2()*/;
            String playerUn = player.getId(), unP1 = p1.getId()/*, unP2 = p1.getId()*/;
            if(unP1.equals(playerUn) /*|| unP2.equals(playerUn)*/)
            {
                game.stopRunGame();
//                synchronized(games)
//                {
//                    games.remove(game);
//                }

                synchronized(games)
                {
                    games.remove(game);
                }
                return;
            }
        }
    }

    // handle player disconnected
    // (make thread of game finish)
    private void playerDisconnected(Player player)
    {
        System.out.println("in playerDisconnected. player: " + player.getId());
        for(Game game : games)
        {
            Player p1 = game.getP1(), p2 = game.getP2();
            String un1 = p1.getId(), un2 = p2.getId();
            if(un1.equals(player.getId()))
            {
                game.playerDisconnected(un1);
                game.stopRunGame();
                log("Game session ended: " + un1 + " vs " + un2);
                return;
            }

            if(un2.equals(player.getId()))
            {
                game.playerDisconnected(un2);
                game.stopRunGame();
                log("Game session ended: " + un1 + " vs " + un2);
                return;
            }
        }
    }

    /**
     * send for opponent of un that he made forfeit
     *
     * @param un
     */
    public void makeForfeit(String un)
    {
        for(Game game : games)
        {
            Player p1 = game.getP1(), p2 = game.getP2();
            String p1ID = p1.getId(), p2ID = p2.getId();
            if(p1ID.equals(un))
            {
                p2.yourOpponentExit();
                playerDisconnected(p1);
                synchronized(games)
                {
                    games.remove(game);
                }

                synchronized(onlinePlayers)
                {
                    onlinePlayers.remove(p1);
                    onlinePlayers.remove(p2);
                }

                break;
            }

            if(p2ID.equals(un))
            {
                p1.yourOpponentExit();
                playerDisconnected(p2);
                synchronized(games)
                {
                    games.remove(game);
                }

                synchronized(onlinePlayers)
                {
                    onlinePlayers.remove(p1);
                    onlinePlayers.remove(p2);
                }
                break;
            }
        }
    }

    //search the game playedID is playing and returns
    // the current player turn
    private String getPlayerTurnByPlayer(String playerID)
    {
        for(Game game : games)
        {
            String p1 = game.getP1().getId(), p2 = game.getP2().getId();
            if(p1.equals(playerID) || p2.equals(playerID))
            {
                return game.getCurrentPlayerTurn().getId();
            }
        }
        return null;
    }

    //send for opponent of player that he disconnected
    private void sendYourOpponentExit(Player player)
    {
        System.out.println("send your opponent exit...");
        String unPlayer = player.getId();
        for(Game game : games)
        {
            Player player1 = game.getP1(), player2 = game.getP2();
            String un1 = player1.getId(), un2 = player2.getId();

            if(un1.equals(unPlayer))
            {
                System.out.println("send to player " + un2);
                player2.yourOpponentExit();
                synchronized(games)
                {
                    games.remove(game);
                }

                return;
            }

            if(un2.equals(unPlayer))
            {
                System.out.println("send to player " + un1);
                player1.yourOpponentExit();
                synchronized(games)
                {
                    games.remove(game);
                }
                return;
            }
        }
    }

    // returns player ID is online
    private boolean playerIsOnline(String ID)
    {
        for(Player player : onlinePlayers)
        {
            if(player.getId().equals(ID))
            {
                return true;
            }
        }
        return false;
    }

    // asks if the server want to close, if yes
    //close him
    private void exitServer()
    {
        int option = JOptionPane.showConfirmDialog(frmWin, "Do you realy want to EXIT ?", "Server Exit", JOptionPane.YES_NO_OPTION);

        if(option == JOptionPane.OK_OPTION)
        {
            stopServer();
        }
    }

    // stop the running of server
    private void stopServer()
    {
        try
        {
            // This will throw cause an Exception on serverSocket.accept() in waitForClient() method
            serverSocket.close();

            // close all threads & clients
            for(int i = 0; i < onlinePlayers.size(); i++)
            {
                Player player = onlinePlayers.get(i);
                player.serverClosed();
                //onlinePlayers.remove(i);
            }

            synchronized(onlinePlayers)
            {
                onlinePlayers.clear();
            }

            log("Server Stoped!");
        }
        catch(IOException ex)
        {
            System.out.println("server stopped!");
        }
    }

    // close the server
    private void closeServer(String cause)
    {
        if(serverSocket != null && !serverSocket.isClosed())
        {
            stopServer();
        }

        log("Server Closed!");

        frmWin.dispose(); // close GUI
    }

    // setup Server Address(IP&Port) and create the ServerSocekt
    private void setupServer()
    {
        try
        {
            serverPort = -1;
            serverIP = InetAddress.getLocalHost().getHostAddress(); // get Computer IP

            String port = JOptionPane.showInputDialog(frmWin, "Enter Server PORT Number:",
                    Constants.SERVER_DEFAULT_PORT);

            if(port == null) // check if Cancel button was pressed
            {
                serverPort = -1;
            }
            else
            {
                serverPort = Integer.parseInt(port);
            }

            // Setup Server Socket ...
            serverSocket = new ServerSocket(serverPort);

            serverSetupOK = true;
        }
        catch(Exception exp)
        {
            serverSetupOK = false;
            String serverAddress = serverIP + ":" + serverPort;
            log("Can't setup Server Socket on " + serverAddress + "\n" + "Fix the problem & restart the server.", exp, frmWin);
        }

        System.out.println("**** setupServer() finished! ****");
    }

    // create server GUI
    private void createServerGUI()
    {
        frmWin = new JFrame();
        frmWin.setSize(SERVER_WIN_SIZE);
        frmWin.setAlwaysOnTop(true);
        frmWin.setTitle(SERVER_WIN_TITLE);
        frmWin.setLocationRelativeTo(null);
        frmWin.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frmWin.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                exitServer();
            }
        });

        // create displayArea
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setFont(SERVER_LOG_FONT);
        areaLog.setMargin(new Insets(5, 5, 5, 5));
        areaLog.setBackground(SERVER_LOG_BGCOLOR);
        areaLog.setForeground(SERVER_LOG_FGCOLOR);

        areaLog.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_PAGE_UP)
                {
                    Font font = areaLog.getFont();
                    Font biggerFont = new Font(font.getFamily(), font.getStyle(), font.getSize() + 1);
                    areaLog.setFont(biggerFont);
                }
                if(e.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
                {
                    Font font = areaLog.getFont();
                    Font smallerFont = new Font(font.getFamily(), font.getStyle(), font.getSize() - 1);
                    areaLog.setFont(smallerFont);
                }
            }
        });

        // panel for send message
//        JPanel pnlActions = new JPanel(new BorderLayout());
//
//        JLabel lblMsg = new JLabel(" Message: ");
//        JTextField filedMsg = new JTextField("");
//        filedMsg.setForeground(Color.BLUE);
//        filedMsg.addActionListener(new ActionListener()
//        {
//            @Override   // called when ENTER Key was hit
//            public void actionPerformed(ActionEvent event)
//            {
//                //sendMsgToAllClients(msgField.getText());
//                System.out.println("sendMsgToAllClients: " + filedMsg.getText());
//            }
//        });
//
        JPanel pnlButtons = new JPanel(new FlowLayout(0, 2, 1));
        JButton btnOnlineGames = new JButton("ONLINE GAMES");
        btnOnlineGames.addActionListener(new ActionListener()
        {
            @Override   // called when Mouse Clicked on the BUTTON
            public void actionPerformed(ActionEvent ae)
            {
                allGamesOnline();
            }
        });

        JButton btnClear = new JButton("CLEAR");
        btnClear.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                areaLog.setText("");
            }
        });
        JButton btnOnlinePlayers = new JButton("ONLINE PLAYERS");
        btnOnlinePlayers.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                showOnlinePlayers();
            }
        });

        JButton btnPlayersInDB = new JButton("ALL DATA PLAYERS");
        btnPlayersInDB.addActionListener(new ActionListener()
        {
            @Override   // called when Mouse Clicked on the BUTTON
            public void actionPerformed(ActionEvent ae)
            {
                String allPlayers = allPlayersInDB();
                log(allPlayers);
            }
        });

        pnlButtons.add(btnOnlineGames);
        pnlButtons.add(btnOnlinePlayers);
        pnlButtons.add(btnPlayersInDB);
        pnlButtons.add(btnClear);

        // add all components to window
        frmWin.add(new JScrollPane(areaLog), BorderLayout.CENTER);
        frmWin.add(pnlButtons, BorderLayout.SOUTH);

        // show window
        frmWin.setVisible(true);

        System.out.println("**** createServerGUI() finished! ****");
    }

    // show on server GUI all players that online
    private void showOnlinePlayers()
    {
        if(onlinePlayers.isEmpty())
        {
            log("no one is online!");
            return;
        }

        log("ONLINE PLAYERS:");
        for(Player player : onlinePlayers)
        {
            log(player.getId());
        }
    }

    // show messages on server GUI
    private void log(String msg)
    {
        areaLog.append(msg + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
        //System.out.println(msg);
    }

    // show exception on log
    private static void log(String msg, Exception ex, JFrame... win)
    {
        String title = "Runtime Exception: " + msg;

        System.out.println("\n>> " + title);
        System.out.println(">> " + new String(new char[title.length()]).replace('\0', '-'));

        String errMsg = ">> " + ex.toString() + "\n";
        for(StackTraceElement element : ex.getStackTrace())
        {
            errMsg += ">>> " + element + "\n";
        }
        System.out.println(errMsg);

        if(win.length != 0)
        {
            // bring the window into front (DeIconified)
            win[0].setVisible(true);
            win[0].toFront();
            win[0].setState(JFrame.NORMAL);

            // popup dialog with the error message
            JOptionPane.showMessageDialog(win[0], msg + "\n\n" + errMsg, "Exception Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // show on server log all games online
    private void allGamesOnline()
    {
        if(games.isEmpty())
        {
            log("No games online right now!");
            return;
        }

        String allGames = "ONLINE GAMES:\n";
        for(Game game : games)
        {
            String un1 = game.getP1().getId(), un2 = game.getP2().getId();
            String gameStr = un1 + " VS " + un2 + "\n";
            allGames += gameStr;
        }
        log(allGames);
    }

    // Gets all register players that
    // exists in DB
    private String allPlayersInDB()
    {
        ArrayList<GameInfo> allPlayers = DB.getAllPlayers();
        String res = "";
        for(GameInfo game : allPlayers)
        {
            String un = game.getUn(), pw = game.getPw();
            res += "UN: " + un + "\t PW: " + pw + "\n";
        }
        return res;
    }

    // main
    public static void main(String args[])
    {
        Server server = new Server();
        server.runServer();

        System.out.println("**** Server main() finished! ****");
    }

}
