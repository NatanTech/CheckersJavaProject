
package chatserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laptop
 */
public class AppSocket 
{
    private ObjectInputStream is;
    private ObjectOutputStream os;
    private Guest currentGuest;

    public AppSocket() {
    }
    
    public AppSocket( Guest currentGuest) throws IOException {
        this.currentGuest=currentGuest;
        // get the input & output streams from socket
        this.os = new ObjectOutputStream(this.currentGuest.getSocket().getOutputStream());
        this.is = new ObjectInputStream(this.currentGuest.getSocket().getInputStream()); 
    }
    public void sendMsg(Massage msg) throws IOException
    {
        os.writeUTF(msg.getSubject());    
    }
    public void sendMsg(String msg) throws IOException
    {
        os.writeUTF(msg);    
    }
    public Massage readMsg() throws IOException
    {
        Massage msgFromClient=new Massage();
        Thread t = new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {
                    while(true)
                    {
                        msgFromClient.setSubject(is.readUTF());  
                    }
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(AppSocket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t.start();
        
        return msgFromClient;
    }
    public void setCurrentGuest(Guest currentGuest) {
        this.currentGuest=currentGuest;
    }

    public Guest getCurrentGuest() {
        return currentGuest;
    }

    public ObjectInputStream getIs() {
        return is;
    }
    public ObjectOutputStream getOs() {
        return os;
    }
    @Override
    public String toString() {
        return "appSocket-->"+ "\n cuurentSoucket: " + currentGuest.getSocket() + "\n chatterAutoID: " + currentGuest.getChatterId() + "\nis: " + is + "\n os: " + os + '}';
    }
    public void disConnectClient() throws IOException
    {
        this.currentGuest.getSocket().close();
        this.is.close();
        this.os.close();
    }
}
