package ver6;

import java.io.*;
import java.net.Socket;

/**
 * AppSocket -שקע תקשורת המאפשר העברת הודעות ברשת בין השרת ללקוח .
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
public class AppSocket
{
    private Socket msgSocket;           // Message socket
    private ObjectOutputStream msgOS;   // Output stream 
    private ObjectInputStream msgIS;    // Input stream 
    
    private Socket cmdSocket;           // command socket
    private ObjectOutputStream cmdOS;   // Output stream for commands
    private ObjectInputStream cmdIS;    // Input stream for commands


    public AppSocket(Socket socketMsg, Socket socketCmd)
    {
        this.msgSocket = socketMsg;
        this.cmdSocket = socketCmd;
        try
        {
            // Create MESSAGE streams. 
            msgOS = new ObjectOutputStream(socketMsg.getOutputStream());
            msgIS = new ObjectInputStream(socketMsg.getInputStream());
            cmdOS = new ObjectOutputStream(socketCmd.getOutputStream());
            cmdIS = new ObjectInputStream(socketCmd.getInputStream());
        }
        catch(IOException ex)
        {
            System.out.println("can't make appSocket");
        }
    }

    //Sends message with OS
    public void writeMessage(Message msg)
    {
        try
        {
            msgOS.writeObject(msg);
            msgOS.flush(); // send object now
        }
        catch(Exception ex)
        {
        }
    }

    //Reads message with IS
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
            msg = new Message(Message.CANCEL_EXIT);
        }
        return msg;
    }

    //Sends command with OS
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

    //Reads command with IS
    public Message readCommand()
    {
        Message msg = null;
        try
        {
            msg = (Message) cmdIS.readObject();
        }
        catch(Exception ex)
        {
            msg = new Message(Message.CANCEL_EXIT);
        }
        return msg;
    }

    //Closes every AppSocket
    public void close()
    {
        try
        {
            msgSocket.close();  // will close the IS & OS streams
            cmdSocket.close();  // will close the IS & OS streams
        }
        catch(IOException ex)
        {
            System.out.println("can't close appSocket");
        }
    }

    //getLocalSocketAddress of msgSocket
    public String getLocalAddress()
    {
        return msgSocket.getLocalSocketAddress().toString().substring(1);
    }

    //getRemoteSocketAddress of msgSocket
    public String getRemoteAddress()
    {
        return msgSocket.getRemoteSocketAddress().toString().substring(1);
    }

    //Checks if the msgSocket is connected
    public boolean isConnected()
    {
        return msgSocket != null && !msgSocket.isClosed();
    }

    //Checks if the msgSocket is connected
    public boolean isClosed()
    {
        return msgSocket != null && msgSocket.isClosed();
    }
}
