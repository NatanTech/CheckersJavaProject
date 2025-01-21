package ver1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * ChatClient דוגמה ללקוח צאט פשוט .
 * ---------------------------------------------------------------------------
 * by Ilan Perez (ilanperets@gmail.com) 20/10/2021
 */
public class Client
{
    // constatns

    public static final String CLIENT_WIN_TITLE = "checkers Client";
    private static final Dimension CLIENT_WIN_SIZE = new Dimension(700, 400);
    private static final Color CLIENT_LOG_BGCOLOR = Color.LIGHT_GRAY;
    private static final Color CLIENT_LOG_FGCOLOR = Color.BLACK;
    private static final Font CLIENT_LOG_FONT = new Font("Consolas", Font.PLAIN, 20); // Font.MONOSPACED

    // for GUI
    private JTextArea areaLog;
    private JFrame frmWin;
    private JTextField filedMsg;
    private JButton btnSend;

    // for Client
    private boolean clientSetupOK, clientRunOK;
    private int serverPort;
    private String serverIP;
    private Socket socketToServer;
    private AppSocket clientSocket;

    /**
     * Constractor for Chat Client.
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
            log("Connected to Server(" + clientSocket.getRemoteAddress() + ")");
            log("CLIENT(" + clientSocket.getLocalAddress() + ") Setup & Running!\n");
            frmWin.setTitle(CLIENT_WIN_TITLE + " (" + clientSocket.getLocalAddress() + ") " /* + chatter.getId()*/);
            setChatMsgSendEnable(true);

            clientRunOK = true;

            // loop while client running OK
            while(clientRunOK)
            {
                // wait for a message from server or null if socket closed
                String msg = clientSocket.readMessage().getSubject();

                if(msg == null)
                {
                    clientRunOK = false;
                }
                else
                {
                    if(msg.equals("#login"))
                    {
                        login("");
                    }
                    proccessMsgFromServer(msg);
                }
            }
        }
        closeClient();

        System.out.println("**** runClient() finished! ****");
    }

    private void proccessMsgFromServer(String msg)
    {
        log(msg);
    }

    private void setupClient()
    {
        try
        {
            // set the Server Adress (DEFAULT IP&PORT)
            serverPort = Constants.SERVER_DEFAULT_PORT;
            serverIP = InetAddress.getLocalHost().getHostAddress(); // IP of this computer

            // get Server Address from user
            String serverAddress = JOptionPane.showInputDialog(frmWin, "Enter SERVER Address [IP : PORT]", serverIP + " : " + serverPort);

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
            clientSocket = new AppSocket(socketToServer);
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
            log("Client can't connect to Server(" + serverAddress + ")", exp, frmWin);
        }
    }

    private void login(String errMsg) throws IOException
    {
        Message msg = loginDialog(errMsg);
        chooseLoginOption(msg);
        if(msg.getSubject().equalsIgnoreCase("Cancel & Exit"))
        {
            return;
        }
        msg = clientSocket.readMessage();
        System.out.println("subject: " + msg.getSubject());
        String subject = msg.getSubject();

        if(subject.startsWith("Welcome"))
        {
            frmWin.dispose();
        }
        else
        {
            login(subject);
        }
    }

    public static Message loginDialog(String errMsg) throws IOException
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
        Message msg = new Message(status, login);

        if(status.equals("Cancel & Exit"))
        {
            optionPane.setVisible(false);
        }

        // שליפת שם המשתמש והסיסמה והשדות הקלט
        System.out.println("un: " + login.getUserName());
        System.out.println("pw: " + login.getPassword());
        System.out.println("Play with AI: " + login.isWithAI());
        return msg;
    }

    private void chooseLoginOption(Message msg)
    {
        String status = msg.getSubject();
        LoginDetails login = msg.getLogin();
        System.out.println("subject: " + status + ", loginDetails: " + login);
        switch(status)
        {

            case "Login":
                System.out.println("send login details to server!");
                clientSocket.writeMessage(new Message(Constants.LOGIN_AS_USER, login));
                break;

            case "Guest":
                System.out.println("send login guest to server!");
                clientSocket.writeMessage(new Message(Constants.LOGIN_AS_GUEST, login));
                break;

            case "Cancel & Exit":
                System.out.println("send cancel to server!");
                clientSocket.writeMessage(new Message(Constants.CANCEL_EXIT));
                break;
        }
    }

    // call when window X pressed
    private void exitClient()
    {
        int option = JOptionPane.showConfirmDialog(frmWin, "Do you realy want to Exit?", "Client Exit", JOptionPane.YES_NO_OPTION);

        if(option == JOptionPane.OK_OPTION)
        {
            stopClient();
        }
    }

    private void stopClient()
    {
        clientSocket.close(); // will throw 'SocketException' and unblock I/O. see close() API
        closeClient();
        log("Client " /*+ appSocket.getId()*/ + " Stoped!");
    }

    private void closeClient()
    {
        if(clientSocket != null && clientSocket.isConnected())
        {
            String msg = "The connection with the Server is LOST!\nClient will be close...";
            JOptionPane.showMessageDialog(frmWin, msg, "Chat Client Error", JOptionPane.ERROR_MESSAGE);
            stopClient();
        }

        clientSocket.writeMessage(new Message(Constants.CANCEL_EXIT));
        log("Client Closed!");

        // close GUI
        frmWin.dispose();

        // close client
        //System.exit(0);
    }

    private void sendMsgToServer()
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                clientSocket.writeMessage(new Message(filedMsg.getText()));
                filedMsg.setText("");
                filedMsg.requestFocusInWindow();
            }
        });
        thread.start();
    }

    private void createClientGUI()
    {
        frmWin = new JFrame("Chat Client");
        frmWin.setSize(CLIENT_WIN_SIZE);
        frmWin.setAlwaysOnTop(true);
        frmWin.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frmWin.setLocationRelativeTo(null);

        frmWin.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                exitClient();
            }
        });

        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setFont(CLIENT_LOG_FONT);
        areaLog.setMargin(new Insets(5, 5, 5, 5));
        areaLog.setBackground(CLIENT_LOG_BGCOLOR);
        areaLog.setForeground(CLIENT_LOG_FGCOLOR);

        // panel for send message
        JPanel pnlSendMsg = new JPanel(new BorderLayout());
        JLabel lblMsg = new JLabel(" Your Message: ");

        filedMsg = new JTextField();
        filedMsg.setForeground(Color.BLUE);
        filedMsg.addActionListener(new ActionListener()
        {
            @Override   // called when ENTER Key was hit
            public void actionPerformed(ActionEvent event)
            {
                sendMsgToServer();
            }
        });

        btnSend = new JButton("SEND");
        btnSend.addActionListener(new ActionListener()
        {
            @Override   // called when Mouse Clicked on the BUTTON
            public void actionPerformed(ActionEvent ae)
            {
                sendMsgToServer();
            }
        });
        pnlSendMsg.add(lblMsg, BorderLayout.WEST);
        pnlSendMsg.add(filedMsg, BorderLayout.CENTER);
        pnlSendMsg.add(btnSend, BorderLayout.EAST);

        frmWin.add(new JScrollPane(areaLog), BorderLayout.CENTER);
        frmWin.add(pnlSendMsg, BorderLayout.SOUTH);
        frmWin.setVisible(true);

        // disable send msg
        setChatMsgSendEnable(false);

        System.out.println("**** createClientGUI() finished! ****");
    }

    private void setChatMsgSendEnable(boolean status)
    {
        filedMsg.setEnabled(status);
        btnSend.setEnabled(status);

        if(status)
        {
            filedMsg.requestFocusInWindow();
        }
    }

    private void log(String msg)
    {
        areaLog.append(msg + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
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
    public static void main(String[] args) throws IOException
    {
        Client client = new Client();
        client.runClient();

        System.out.println("**** ChatClient main() finished! ****");
    }
}
