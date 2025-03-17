package ver6;

import java.io.Serializable;

/**
 * by Natan Tzuberi (tzuberinat@gmail.com) 10/110/2022
 */
//saving the id of a player and his chooice about ai 
public class PlayerType implements Serializable
{
    private int ID_player; 
    private boolean withAI; 

    public PlayerType(int ID, boolean withAI)
    {
        this.ID_player = ID;
        this.withAI = withAI;
    }

    public PlayerType(boolean withAI)
    {
        this(0, withAI);
    }

    public PlayerType(PlayerType login)
    {
        this.ID_player = login.ID_player;
        this.withAI = login.withAI;
    }

    public int getID_player()
    {
        return ID_player;
    }

    public void setID_player(int ID_player)
    {
        this.ID_player = ID_player;
    }

    public boolean getWithAI()
    {
        return withAI;
    }

    public void setWithAI(boolean withAI)
    {
        this.withAI = withAI;
    }

    @Override
    public String toString()
    {
        return "LoginDetails{" + "userName=" + ID_player + ", withAI=" + withAI + '}';
    }

}
