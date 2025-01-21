package ver5_final;

import java.io.*;
import java.net.Socket;

/**
 * AppSocket -טיפוס המייצג שקע תקשורת המאפשר העברת הודעות ברשת בין השרת ללקוח .
 * ---------------------------------------------------------------------------
 * by Ilan Peretz(ilanperets@gmail.com) 10/11/2021
 */
public class AppSocket
{
    private Socket msgSocket;           // Message socket
    private ObjectOutputStream msgOS;   // Output stream to SEND Messages
    private ObjectInputStream msgIS;    // Input stream to GET Messages

    private Socket cmdSocket;           // command socket
    private ObjectOutputStream cmdOS;   // Output stream to SEND commands
    private ObjectInputStream cmdIS;    // Input stream to GET commands

    /**
     * Constructor to create new AppSocket
     *
     * @param socketMsg
     * @param socketCmd
     */
    public AppSocket(Socket socketMsg, Socket socketCmd)
    {
        this.msgSocket = socketMsg;
        this.cmdSocket = socketCmd;
        try
        {
            // Create MESSAGE streams. Output Stream must be create FIRST!
            // ------------------------------------------------------------
            msgOS = new ObjectOutputStream(socketMsg.getOutputStream());
            msgIS = new ObjectInputStream(socketMsg.getInputStream());
            cmdOS = new ObjectOutputStream(socketCmd.getOutputStream());
            cmdIS = new ObjectInputStream(socketCmd.getInputStream());
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Sends message by msgSocket OS
     *
     * @param msg
     */
    public void writeMessage(Message msg)
    {
        try
        {
            msgOS.writeObject(msg);
            msgOS.flush(); // send object now! (dont wait)
        }
        catch(Exception ex)
        {
        }
    }

    /**
     * Reads message by msgSocket IS
     *
     * @return
     */
    public Message readMessage()
    {
        Message msg = null;
        try
        {
            msg = (Message) msgIS.readObject();
        }
        catch(Exception ex)
        {
            System.out.println("can't read message...");
            msg = new Message(Constants.CANCEL_EXIT);
            //ex.printStackTrace();
        }
        return msg;
    }

    /**
     * Sends messages by cmdSocket OS
     *
     * @param msg
     */
    public void writeCommand(Message msg)
    {
        try
        {
            cmdOS.writeObject(msg);
            cmdOS.flush(); // send object now! (dont wait)
        }
        catch(Exception ex)
        {
        }
    }

    /**
     * Reads messages by cmdSocket IS
     *
     * @return
     */
    public Message readCommand()
    {
        Message msg = null;
        try
        {
            msg = (Message) cmdIS.readObject();
        }
        catch(Exception ex)
        {
            msg = new Message(Constants.CANCEL_EXIT);
            //ex.printStackTrace();
        }
        return msg;
    }

    /**
     * Closes the AppSocket
     */
    public void close()
    {
        try
        {
            msgSocket.close();  // will close the IS & OS streams
            cmdSocket.close();  // will close the IS & OS streams
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * getLocalSocketAddress of msgSocket
     *
     * @return
     */
    public String getLocalAddress()
    {
        return msgSocket.getLocalSocketAddress().toString().substring(1);
    }

    /**
     * getRemoteSocketAddress of msgSocket
     *
     * @return
     */
    public String getRemoteAddress()
    {
        return msgSocket.getRemoteSocketAddress().toString().substring(1);
    }

    /**
     * Checks if the msgSocket is connected
     *
     * @return
     */
    public boolean isConnected()
    {
        return msgSocket != null && !msgSocket.isClosed();
    }

    /**
     * Checks if the msgSocket is connected
     *
     * @return
     */
    public boolean isClosed()
    {
        return msgSocket != null && msgSocket.isClosed();
    }
}
