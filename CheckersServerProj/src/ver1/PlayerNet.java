package ver1;

import java.io.Serializable;

/**
 * PlayerNet.
 * ---------------------------------------------------------------------------
 * by Ilan Peretz (ilanperets@gmail.com) 10/11/2021
 */
public class PlayerNet extends Player implements Serializable
{
    private AppSocket socketToClient;

    public PlayerNet(AppSocket socketToClient, String id)
    {
        super(id);
        this.socketToClient = socketToClient;
    }

    public AppSocket getSocketToClient()
    {
        return socketToClient;
    }

    public void setSocketToClient(AppSocket socketToClient)
    {
        this.socketToClient = socketToClient;
    }

    @Override
    public String toString()
    {
        return "PlayerNet{" + super.toString() + ", socketToClient=" + socketToClient + '}';
    }

    @Override
    public Move getMove()
    {
        // with socket do...
        socketToClient.writeMessage(new Message("#GET_MOVE"/*, model.getBoard()*/));
        Message moveMsg = socketToClient.readMessage();
                
        return moveMsg.getMove();
    }

    @Override
    public void waitTurn()
    {
        socketToClient.writeMessage(new Message("#WAIT_TURN"));
    }

    @Override
    public void gameOver()
    {
        socketToClient.writeMessage(new Message("#GAME_OVER"));
    }
}
