package ver6;

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
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
public class Server
{
    // constants
    public static final String SERVER_WIN_TITLE = "Checkers Server";
    private static final Dimension SERVER_WIN_SIZE = new Dimension(580, 400);

    private static final Color SERVER_LOG_BGCOLOR = Color.BLACK;
    private static final Color SERVER_LOG_FGCOLOR = Color.WHITE;
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


    public Server()
    {
        autoClientID = 0;
        waitingPlayer = null;
        onlinePlayers = new ArrayList();
        games = new ArrayList();
        createServerGUI();
        setupServer();
    }

    // main
    public static void main(String args[])
    {
        Server server = new Server();
        server.runServer();

        System.out.println(" Server main() finished! ");
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
                AppSocket socketToClient = waitForClient();  

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

        System.out.println("runServer() finished!");
    }

    // Wait for client to connect.
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
                            PlayerAI AI = new PlayerAI();
                            createNewGameAndRun(player, AI);
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
        if(player1.getId()==-1000)
            log("Game started: AI" + " VS " + player2.getId());
        else
            if(player2.getId()==-1000)
            log("Game started: AI" + " VS " + player1.getId());
        else
        log("Game started: " + player1.getId() + " VS " + player2.getId());
        game.runGame();
    }
    
    //get the detailes about a new player that login
    public PlayerNet login(AppSocket clientSocket)
    {
        clientSocket.writeMessage(new Message(Message.ENTER));
        while(true)
        {
            int clientID = 0;

            Message msg = clientSocket.readMessage();
            String answer = msg.getSubject();
            PlayerType login = msg.getLogin();
            System.out.println("msg: " + msg);

            if(answer.startsWith(Message.CANCEL_EXIT))
            {
                clientSocket.close();
                return null;
            }

            if(answer.startsWith(Message.ENTER_PLAY))
            {
                clientID = ++autoClientID;
                msg=new Message("Welcome " + clientID);
                login.setID_player(clientID);
                msg.setLogin(login);
                clientSocket.writeMessage(msg);
                System.out.println("ID: " + clientID + ", AI: " + login.getWithAI());
            }

            if(clientID != 0)
            {
                System.out.println("clientSocket = " + clientSocket + ", with AI = " + login.getWithAI() + ", clientID = " + clientID);
                PlayerNet player = new PlayerNet(clientSocket, login.getWithAI(), clientID);
                return player;
            }
        }
    }

    // handle requests that comes from cmd socket in seperate thread
    private void handleCmdSocket(PlayerNet player)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                AppSocket cmd = player.getSocketToClient();
                int ID = player.getId();
                while(true)
                {
                    Message msg = cmd.readCommand();
                    String subject = msg.getSubject();
                    System.out.println("handleCmd " + ID + ": " + msg);

                    if(subject.equals(Message.CANCEL_EXIT))
                    {
                        System.out.println("player " + ID + " do cancel exit!");
                        log("User " + ID + " disconnected!");

                        if(waitingPlayer != null)
                        {
                            if(waitingPlayer.getId()==ID)
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
        int un = player.getId();
        if(subject.equals(Message.QUIT))
        {
            makeQuit(un);
        }
    }
    
    // handle player disconnected
    // (make thread of game finish)
    private void playerDisconnected(Player player)
    {
        System.out.println("in playerDisconnected. player: " + player.getId());
        for(Game game : games)
        {
            Player p1 = game.getPlayer1(), p2 = game.getPlayer2();
            int ID1 = p1.getId(), ID2 = p2.getId();
            if(ID1==(player.getId()))
            {
                game.playerDisconnected(ID1);
                game.stopRunGame();
                if(ID2==-1000)
                    log("Game ended: " +"AI Player vs " + ID1);
                else
                log("Game ended: " + ID1 + " vs " + ID2);
                return;
            }

            if(ID2== (player.getId()))
            {
                game.playerDisconnected(ID2);
                game.stopRunGame();
                if(ID1==-1000)
                    log("Game ended: " +"AI Player vs " + ID2);
                else
                log("Game ended: " + ID1 + " vs " + ID2);
                return;
            }
        }
    }
    
    //stoping the game and delete him from games because on player has quit
    public void makeQuit(int ID)
    {
        for(Game game : games)
        {
            Player p1 = game.getPlayer1(), p2 = game.getPlayer2();
            int p1ID = p1.getId(), p2ID = p2.getId();
            if(p1ID==ID)
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

            if(p2ID==(ID))
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
    private int getPlayerTurnByPlayer(int playerID)
    {
        for(Game game : games)
        {
            int p1 = game.getPlayer1().getId(), p2 = game.getPlayer2().getId();
            if(p1==(playerID) || p2==(playerID))
            {
                return game.getCurrentState().getCurrentPlayer().getId();
            }
        }
        return -1;
    }

    //send for opponent of player that he disconnected
    private void sendYourOpponentExit(Player player)
    {
        System.out.println("send your opponent exit...");
        int unPlayer = player.getId();
        for(Game game : games)
        {
            Player player1 = game.getPlayer1(), player2 = game.getPlayer2();
            int un1 = player1.getId(), un2 = player2.getId();

            if(un1==(unPlayer))
            {
                System.out.println("send to player " + un2);
                player2.yourOpponentExit();
                synchronized(games)
                {
                    games.remove(game);
                }

                return;
            }

            if(un2==(unPlayer))
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

    // checkink if player ID is online
    private boolean playerIsOnline(int ID)
    {
        for(Player player : onlinePlayers)
        {
            if(player.getId()==(ID))
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
        int option = JOptionPane.showConfirmDialog(frmWin, "are you sure ?", "Server Exit", JOptionPane.YES_NO_OPTION);

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
                    Message.SERVER_DEFAULT_PORT);

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

        System.out.println("setupServer() finished!");
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
        
        JPanel pnlButtons = new JPanel(new FlowLayout(0, 2, 1));
//        JPanel pnlButtons = new JPanel(new GridLayout(1, 1));

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

        pnlButtons.add(btnOnlineGames);
        pnlButtons.add(btnOnlinePlayers);
//        pnlButtons.add(btnPlayersInDB);
        pnlButtons.add(btnClear);

        // add all components to window
        frmWin.add(new JScrollPane(areaLog), BorderLayout.CENTER);
        frmWin.add(pnlButtons, BorderLayout.SOUTH);

        // show window
        frmWin.setVisible(true);

        System.out.println("createServerGUI() finished!");
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
    private void log(int msg)
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
            int un1 = game.getPlayer1().getId(), un2 = game.getPlayer2().getId();
            String gameStr = un1 + " VS " + un2 + "\n";
            allGames += gameStr;
        }
        log(allGames);
    }

}
