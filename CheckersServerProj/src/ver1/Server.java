package ver1;

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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Server -דוגמה לשרת משחק רשת מרובה משחקים .
 * ---------------------------------------------------------------------------
 * by Ilan Perez (ilanperets@gmail.com) 10/11/2021
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
    private int autoChatterID;
    private ArrayList<String> onlinePlayers;

    /**
     * Constructor for ChatServer.
     */
    public Server()
    {
        createServerGUI();
        setupServer();

        autoChatterID = 0;
        onlinePlayers = new ArrayList();
    }

    // Run the server - wait for clients to connect & handle them
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
                Socket socketToClient = waitForClient();  // (blocking)

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
    private Socket waitForClient()
    {
        Socket socketToClient;

        System.out.println("Waiting for client ...");

        try
        {
            // Wait for a new client to connect. return client socket.
            socketToClient = serverSocket.accept(); // blocking method
        }
        catch(IOException exp)
        {
            socketToClient = null;
        }

        return socketToClient;
    }

    // handle client in a separate thread
    private void handleClient(Socket socketToClient)
    {

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                AppSocket appSocket = new AppSocket(socketToClient);
                appSocket.writeMessage(new Message("#login"));
                while(true)
                {
                    PlayerNet player1 = login(appSocket);
                    System.out.println("userName after login attempt: " + player1);
                    if(player1.getId() != null)
                    {
                        onlinePlayers.add(player1.getId());
                        break;
                    }
                }

//                    Message msg = (Message)chatter.readMessage();
//
//                    if (msg == null) // check if chatter disconnected
//                        break;
//
//                    proccessMsgFromClient(chatter, msg);
//                }
//
//                // disconnect chatter
//                log(chatter.getId() + " Disconnected!");
//                appSocketsList.remove(chatter);
//                chatter.close();
//
//                System.out.println("Guest#" + chatter.getId() + " Thread finished!");
            }
        }).start();
    }

    public PlayerNet login(AppSocket client)
    {
        String clientID = null;

        Message msg = client.readMessage();
        String answer = msg.getSubject();
        LoginDetails login = msg.getLogin();
        System.out.println("msg: " + msg);

        if(answer.startsWith(Constants.CANCEL_EXIT))
        {
            client.close();
            log("User disconnected!");
            return null;
        }

        if(answer.startsWith(Constants.LOGIN_AS_GUEST))
        {
            clientID = "GUEST#" + autoChatterID++;
            client.writeMessage(new Message("Welcome " + clientID));
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
                    client.writeMessage(new Message(Constants.ALREADY_ONLINE_MSG));
                }

                if(DB.isUserExists(un, pw))
                {
                    clientID = un;
                    log("User " + un + " connected!");
                    client.writeMessage(new Message("Welcome " + clientID));
                }

                else
                {
                    System.out.println("User " + un + " invalid!");
                    client.writeMessage(new Message(Constants.LOGIN_FAILED_MSG));
                }

            }
            catch(SQLException exp)
            {
                exp.printStackTrace();
            }
//
//        }
//        client.setId(clientID);
//        appSocketsList.add(client);  // add AppSocket object to chattersList
//        log("Client(" + client.getRemoteAddress() + ") Connected as " + clientID);

        }
        PlayerNet player = new PlayerNet(client, clientID);
        return player;
    }

    private boolean playerIsOnline(String ID)
    {
        for(String id : onlinePlayers)
        {
            if(id.equals(ID))
            {
                return true;
            }
        }
        return false;
    }

//    private void proccessMsgFromClient(AppSocket fromClient, Message message)
//    {
//        System.out.println("proccessMsgFromClient - message=" + message);
//        String msg = message.getSubject();
//
//        log("[" + fromClient.getId() + "]: " + msg);
//
//        // check if chatter want to get ONLINE chatters?
//        if(msg.equals("ONLINE?"))
//        {
//            String online = "\n" + appSocketsList.size() + " Chatter(s) Online:\n";
//            for(int i = 0; i < appSocketsList.size(); i++)
//            {
//                online += "> " + appSocketsList.get(i).getId();
//                if(appSocketsList.get(i) == fromClient)
//                {
//                    online += " (You)";
//                }
//                online += "\n";
//            }
//            fromClient.writeMessage(new Message(online));
//            return;
//        }
//
//        // check if chatter want to send a PRIVATE message?
//        if(msg.startsWith("GUEST#"))
//        {
//            // send private msg to this guest #
//            String id = msg.substring(0, msg.indexOf(" "));
//            String text = msg.substring(msg.indexOf(" ") + 1);
//
//            for(int i = 0; i < appSocketsList.size(); i++)
//            {
//                if(appSocketsList.get(i).getId().equals(id))
//                {
//                    appSocketsList.get(i).writeMessage("[Private " + fromClient.getId() + "]: " + text);
//                    break;
//                }
//            }
//            return;
//        }
//
//        // send chatter message to all online chatters
//        for(int i = 0; i < appSocketsList.size(); i++)
//        {
//            AppSocket otherClient = appSocketsList.get(i);
//
//            String clientId = fromClient.getId();
//            if(otherClient == fromClient)
//            {
//                clientId = "Me";
//            }
//
//            otherClient.writeMessage("[" + clientId + "]: " + msg);
//        }
//
//    }
    private void exitServer()
    {
        int option = JOptionPane.showConfirmDialog(frmWin, "Do you realy want to EXIT ?", "Server Exit", JOptionPane.YES_NO_OPTION);

        if(option == JOptionPane.OK_OPTION)
        {
            stopServer();
        }
    }

    private void stopServer()
    {
        try
        {
            // This will throw cause an Exception on serverSocket.accept() in waitForClient() method
            serverSocket.close();

            // close all threads & clients
            for(int i = 0; i < onlinePlayers.size(); i++)
            {
                onlinePlayers.remove(i);
            }

            log("Server Stoped!");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

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
        JPanel pnlActions = new JPanel(new BorderLayout());

        JLabel lblMsg = new JLabel(" Message: ");
        JTextField filedMsg = new JTextField("");
        filedMsg.setForeground(Color.BLUE);
        filedMsg.addActionListener(new ActionListener()
        {
            @Override   // called when ENTER Key was hit
            public void actionPerformed(ActionEvent event)
            {
                //sendMsgToAllClients(msgField.getText());
                System.out.println("sendMsgToAllClients: " + filedMsg.getText());
            }
        });

        JPanel pnlButtons = new JPanel(new FlowLayout(0, 2, 1));
        JButton btnSend = new JButton("SEND ALL");
        btnSend.addActionListener(new ActionListener()
        {
            @Override   // called when Mouse Clicked on the BUTTON
            public void actionPerformed(ActionEvent ae)
            {
                System.out.println("sendMsgToAllClients: " + filedMsg.getText());
                //sendMsgToAllClients(msgField.getText());
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
        JButton btnOnline = new JButton("ONLINE");
        btnOnline.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                //showOnlineChatters();
            }
        });
        pnlButtons.add(btnSend);
        pnlButtons.add(btnOnline);
        pnlButtons.add(btnClear);

        pnlActions.add(lblMsg, BorderLayout.WEST);
        pnlActions.add(filedMsg, BorderLayout.CENTER);
        pnlActions.add(pnlButtons, BorderLayout.EAST);

        // add all components to window
        frmWin.add(new JScrollPane(areaLog), BorderLayout.CENTER);
        frmWin.add(pnlActions, BorderLayout.SOUTH);

        // show window
        frmWin.setVisible(true);

        System.out.println("**** createServerGUI() finished! ****");
    }

//    private void showOnlineChatters()
//    {
//        String online = "\n" + appSocketsList.size() + " Chatter(s) Online";
//        if(appSocketsList.isEmpty())
//        {
//            online += "!\n";
//        }
//        else
//        {
//            online += ":\n";
//        }
//        for(int i = 0; i < appSocketsList.size(); i++)
//        {
//            online += "> " + appSocketsList.get(i).getId() + "\n";
//        }
//        log(online);
//    }
    private void log(String msg)
    {
        areaLog.append(msg + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
        System.out.println(msg);
    }

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

    // main
    public static void main(String args[])
    {
        Server chatServer = new Server();
        chatServer.runServer();

        System.out.println("**** ChatServer main() finished! ****");
    }
}
