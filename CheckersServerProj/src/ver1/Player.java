package ver1;

import java.io.Serializable;

/**
 * Player.
 * ---------------------------------------------------------------------------
 * by Ilan Peretz (ilanperets@gmail.com) 10/11/2021
 */
public abstract class Player implements Serializable
{
    private String id;
    //private Player partner; ?
    //private Model model ?
    
    public Player(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public abstract Move getMove();
    public abstract void waitTurn();
    public abstract void gameOver();
    
    @Override
    public String toString()
    {
        return "Player{" + "id=" + id + '}';
    }
    
}