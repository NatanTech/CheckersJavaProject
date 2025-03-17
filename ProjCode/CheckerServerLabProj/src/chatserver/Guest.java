
package chatserver;

import java.net.Socket;

/**
 *
 * @author Laptop
 */
public class Guest 
{
    public int ChatterId;
    public Socket socket;
    public AppSocket guestAppSocket=new AppSocket();
    public Guest(int ChatterId, Socket socket) {
        this.ChatterId = ChatterId;
        this.socket = socket;
    }
    public int getChatterId() {
        return ChatterId;
    }

    public void setChatterId(int ChatterId) {
        this.ChatterId = ChatterId;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    
}
