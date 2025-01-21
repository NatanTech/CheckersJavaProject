package ver2_Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Client דוגמה ללקוח פשוט .
 * ---------------------------------------------------------------------------
 * by Ilan Perez (ilanperets@gmail.com) 20/10/2021
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
                System.out.println(msg);

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
        }
        closeClient();

        System.out.println("**** runClient() finished! ****");
    }

//    private void processCmdFromServer()
//    {
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                while(true)
//                {
//                    Message msg = clientSocket.readCommand();
//                    String subject = msg.getSubject();
//                    System.out.println(subject);
//                    if(msg == null)
//                    {
//                        break;
//                    }
//
//                }
//            }
//        }).start();
//
//    }
    public void saveAndExitPressed()
    {
        Message msg = new Message(Constants.SAVE_AND_EXIT);
        msg.setLogin(new LoginDetails(userName, null, false));
        clientSocket.writeCommand(msg);
    }

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

        if(subject.startsWith("Game over"))
        {
            view.gameOver(subject);
            countDown(subject);
        }

        if(subject.equals(Constants.WAIT_TURN))
        {
            view.setLabelMsg("Wait turn...");
            view.setBoardButtonsEnabled(false);
        }

        if(subject.equals(Constants.INIT_GAME))
        {
            view.setup(msg.getBoard());
            view.getBtnSaveGame().setEnabled(false);
            view.getForfeitMenuItem().setEnabled(true);
        }

        if(subject.equals(Constants.UNLOCK_SAVE_GAME_BUTTON))
        {
            view.getBtnSaveGame().setEnabled(true);
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
            view.setEnabledList(locsToUnlock, true);
        }

        if(subject.equals(Constants.OPTIONS_FOR_MOVE))
        {
            ArrayList<Move> posMoves = msg.getPosMoves();
            view.paintAllpossibleMoves(posMoves);
        }

        if(subject.equals(Constants.UPDATE_VIEW))
        {
            Board board = msg.getBoard();
            System.out.println("print board in process message before updateView");
            board.printBoard();
            view.updateBoard(board);
            view.deleteAllDraws();
        }

        if(subject.equals(Constants.DELETE_DRAWS))
        {
            view.deleteAllDraws();
        }

        if(subject.startsWith(" vs")) //todo const
        {
            String title = view.getWin().getTitle();
            view.getWin().setTitle(title + subject);
        }

        if(subject.equals(Constants.ALL_GAMES_PLAYED))
        {
            view.printAllGamesPlayed(msg);
        }

        if(subject.equals(Constants.TOP5))
        {
            view.printTop5(msg);
        }

        if(subject.equals(Constants.YOUR_OPPONENT_EXIT))
        {
            view.yourOpponentExit();
            this.stopClient();
        }

        if(subject.equals(Constants.CHANGE_PASSWORD))
        {
            view.passwordChanged();
        }

        if(subject.equals(Constants.WRONG_PASSWORD))
        {
            view.changePasswordDialog("Wrong password. please enter your password again");
        }

        if(subject.equals(Constants.SELECT_UNFINISHED_BOARD))
        {
            ArrayList<String> allBoards = msg.getBoards();
//            for(int i = 0; i < allBoards.size(); i++)
//            {
//                System.out.println("board#" + i + ": " + allBoards.get(i));
//            }
            //System.out.println(allBoards.get(4));
            view.selectUnfinishedBoard(allBoards);
        }

        if(subject.equals(Constants.UNLOCK_REGISTER_OPTIONS))
        {
            view.setEnebledRegisterOptions(true);
        }

    }

    public void AllGamesPlayed()
    {
        clientSocket.writeCommand(new Message(Constants.ALL_GAMES_PLAYED));
    }

    public void Top5()
    {
        clientSocket.writeCommand(new Message(Constants.TOP5));
    }

    public void deleteUser()
    {
        clientSocket.writeCommand(new Message(Constants.DELETE_USER));
    }

    public void deleteSavedGames()
    {
        clientSocket.writeCommand(new Message(Constants.DELETE_SAVED_GAMES));
    }

    public void Forfeit()
    {
        clientSocket.writeCommand(new Message(Constants.FORFEIT));
    }

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

    public void selectUnfinishedGame(String board)
    {
        Message msg = new Message(Constants.SELECT_UNFINISHED_BOARD);
        msg.setStats(board);
        clientSocket.writeCommand(msg);
    }

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

    private void countDown(String subject)
    {
        for(int i = 7; i > 0; i--)
        {
            view.setLabelMsg(subject + " new game will start in " + i + "s");
            sleepInSeconds(1);
        }
        clientSocket.writeMessage(new Message(Constants.COUNT_DOWN_FINISHED));
    }

    private void login(String errMsg) throws IOException
    {
        Message msg = loginDialog(errMsg);
        String subject = msg.getSubject();
        if(subject.equalsIgnoreCase("Cancel & Exit"))
        {
            return;
        }
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
        }
        else
        {
            login(subject);
        }
    }

    public Message loginDialog(String errMsg) throws IOException
    {

        // הצגת תמונת כניסה
        ImageIcon loginIcon = new ImageIcon(Client.class.getResource("/assets/login.png"));
        loginIcon = new ImageIcon(loginIcon.getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH));
        JLabel lblSplash = new JLabel(loginIcon);
        lblSplash.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));

        // שדות קלט עבור קליטת נתונים
        JLabel errLabel = new JLabel(errMsg);          // להצגת הודעת שגיאה
        errLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errLabel.setForeground(Color.RED);
        errLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD | Font.ITALIC, 12));

        JTextField unField = new JTextField("");   // לקליטת שם המשתמש
        unField.setForeground(Color.BLUE);

        unField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                errLabel.setText(" ");
            }
        });

        JTextField pwField = new JTextField("");   // לקליטת הסיסמה
        pwField.setForeground(Color.BLUE);
        pwField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                errLabel.setText(" ");
            }
        });

        JCheckBox aiCheckBox = new JCheckBox("Play with AI", false);
        Object[] inputFields =
        {
            lblSplash,
            errLabel,
            "Enter User Name",
            unField,
            "Enter Password",
            pwField,
            aiCheckBox,
        };

        JOptionPane optionPane = new JOptionPane(inputFields, -1, 1, null, new Object[]
        {
            "Login", "Guest", "Cancel & Exit"
        });

        JDialog dialog = optionPane.createDialog("Login");
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);  // WILL BLOCK the program
        dialog.dispose();   // close dialog

        String userName = unField.getText(), password = pwField.getText();
        boolean withAI = aiCheckBox.isSelected();
        String status = (String) optionPane.getValue();

        LoginDetails login = new LoginDetails(userName, password, withAI);
        Message msg = new Message(status);

        System.out.println("subject: " + status + ", loginDetails: " + login);
        switch(status)
        {

            case "Login":
                System.out.println("send login details to server!");
                msg.setSubject(Constants.LOGIN_AS_USER);
                msg.setLogin(login);
                clientSocket.writeMessage(msg);
                break;

            case "Guest":
                System.out.println("send login guest to server!");
                msg.setSubject(Constants.LOGIN_AS_GUEST);
                msg.setLogin(login);
                clientSocket.writeMessage(msg);
                break;

            case "Cancel & Exit":
                System.out.println("send cancel to server!");
                clientSocket.writeMessage(new Message(Constants.CANCEL_EXIT));
                optionPane.setVisible(false);
                break;
        }
        // שליפת שם המשתמש והסיסמה והשדות הקלט
        System.out.println("un: " + login.getUserName());
        System.out.println("pw: " + login.getPassword());
        System.out.println("Play with AI: " + login.isWithAI());
        return msg;

    }

    public void boardButtonPressed(Location loc)
    {
        Message msg = new Message("click");
        msg.setLoc(loc);
        clientSocket.writeMessage(msg);
    }

    public void exitClient()
    {
        int option = JOptionPane.showConfirmDialog(view.getWin(), "Do you really want to Exit?", "Client Exit", JOptionPane.YES_NO_OPTION);

        if(option == JOptionPane.YES_OPTION)
        {
            stopClient();
        }
    }

    public void stopClient()
    {
        clientSocket.writeCommand(new Message(Constants.CANCEL_EXIT));
        clientSocket.close(); // will throw 'SocketException' and unblock I/O. see close() API
        closeClient();
        //log("Client " /*+ appSocket.getId()*/ + " Stoped!");
    }

    private void closeClient()
    {
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

        // close client
        //System.exit(0);
    }

    private void createClientGUI()
    {
        view = new View(this);
    }

    private void sleepInSeconds(int seconds)
    {
        try
        {
            Thread.sleep(seconds * 1000);
        }
        catch(InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

    private void opponentExit()
    {
        int option = JOptionPane.showConfirmDialog(view.getWin(), "Your opponent disconnected- you win technically!", "opponent disconnected", JOptionPane.OK_OPTION);

        if(option == JOptionPane.OK_OPTION)
        {
            view.getWin().dispose();
        }
    }

    // main
    public static void main(String[] args) throws IOException
    {
        Client client = new Client();
        client.runClient();

        System.out.println("**** ChatClient main() finished! ****");
    }
}
