package chatserver;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * ChatServer to run Server program.
 * 
 * @author ILAN PERETZ
 * @version 07/09/2022
 */
public class ChatServer
{
    public static ServerSocket serverSocket;
    public static JTextArea output;
    public static JTextField input;
    public static int SendingNum;
    public static ArrayList<Guest> clientsList=new ArrayList<>();
    public static int guestNum;
    public static void main(String[] args) throws IOException 
    {
        Guest newGuest = null;
        createGUI();
        log("SERVER wait for client...");
        initServer();
        // server running
        while (true)
        {
            
            // wait for a client to connect
            newGuest.setSocket(serverSocket.accept()); // block operation
            handleClient(newGuest);
        }
    }
    private static void initServer() throws IOException
    {
        guestNum=0;
        String serverIP = InetAddress.getLocalHost().getHostAddress();
        int serverPort = 1284;
        
        // create the server socket on the local machin IP & serverPort 
        serverSocket = new  ServerSocket(serverPort); 
//        ChatServer.log("Server RUNNIG on " + serverIP + ":" + serverPort + "\n");
        output.append("Serve RUNNIG on " + serverIP + ":" + serverPort + "\n");

    }
    private static void handleClient(Guest newG) throws IOException
    {   
        log("New client connected from: " + newG.getSocket().getRemoteSocketAddress() + "\n");
        newG.setChatterId(++guestNum);
        clientsList.add(newG); 
        newG.guestAppSocket.sendMsg("welcom chatter #"+guestNum);
        Massage newMsgFromClient;
        newMsgFromClient=newG.guestAppSocket.readMsg();
        //checking if client want to DisConnect
        if(newMsgFromClient.equals("bye"))
        {
            for (int i = 0; i < clientsList.size(); i++) 
            {
                if(newG.getSocket()==clientsList.get(i).getSocket())
                {
                    clientsList.remove(i);
                    newG.guestAppSocket.disConnectClient();
                }
            }
        }
        ShareMsgFromClientWithEveryOne(newMsgFromClient, newG);
    }
     private static void createGUI()
    {
        output = new JTextArea();
        output.setBackground(Color.BLACK);
        output.setForeground(Color.GREEN);
        output.setEditable(false);
        output.setFont(new Font(Font.SERIF, Font.BOLD, 16));
        
        input = new JTextField("");
        JButton send = new JButton("SEND");
        send.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    sendBroadcastMsgToAllClients();
                } catch (IOException ex) {
                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });

        JFrame win = new JFrame("Server");
        win.setAlwaysOnTop(true);
        win.setLayout(new BorderLayout());
        win.setSize(400, 400);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.add(input,BorderLayout.CENTER);
        pnl.add(send,BorderLayout.EAST);
        win.add(output, BorderLayout.CENTER);
        win.add(pnl, BorderLayout.SOUTH);
        win.setVisible(true);
        //close client
    }
    private static void ShareMsgFromClientWithEveryOne(Massage msg,Guest currentGuest) throws IOException
    {
        Thread sendMsgForEveryOne=new Thread(new Runnable()
        {
            @Override
            public void run() 
            {
                for (int i = 0; i<clientsList.size() ; i++) 
                {
                   
                        if(currentGuest.getSocket()==clientsList.get(i).getSocket())
                        {
                            System.out.println("cuurentSoucket: "+currentGuest.getChatterId()+"\n equal to: "+ clientsList.get(i).getChatterId());
                            try {
                                currentGuest.guestAppSocket.sendMsg("[me:]"+msg);
                            } catch (IOException ex) {
                                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else
                        {
                            try {
                                clientsList.get(i).guestAppSocket.sendMsg("[Guest #"+clientsList.get(i).getChatterId()+":]"+msg);
                            } catch (IOException ex) {
                                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                }
            }
        });
        sendMsgForEveryOne.start();
    }
    public static void sendBroadcastMsgToAllClients() throws IOException
    {
        Thread sendMsgToClient=new Thread(new Runnable()
        {
            @Override
            public void run() 
            {
                for (int i = 0; i<clientsList.size() ; i++) 
                {
                    try 
                    {
                        AppSocket newAppSocket=null;
                        newAppSocket.setCurrentGuest(clientsList.get(i));
                        newAppSocket.sendMsg("[server:]"+WriteMsgForClients());
                    } 
                    catch (IOException ex) 
                    {
                        Logger.getLogger("exp from send Msg For Avesryone");
                    }
                }
            }
        });
        sendMsgToClient.start();
    }
    public static void log(String str)
    {
        System.out.println(str);
        output.append(str + "\n");
    }
    public static String WriteMsgForClients()
    {
        String str;
        str=(input.getText());
        System.out.println(str);
        return str;
    }


    }

